import { ref, computed } from 'vue'
import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { fetchClient } from '@/lib/api/client'
import type { components } from '@/lib/api/schema'

type ChatMessage = components['schemas']['ChatMessageDto']
type ChatMessageRequest = components['schemas']['ChatMessageRequestDto']

export function useChatMessages() {
  const queryClient = useQueryClient()
  const currentConversation = ref<{ otherUserId: number; listingId: number } | null>(null)
  const newMessage = ref('')
  const lastTimestamp = ref(Date.now())

  const conversationsQuery = useQuery({
    queryKey: ['conversations'],
    queryFn: async () => {
      return await fetchClient.GET('/api/chat/conversations', {}).then((res) => {
        console.log('conversations', res.data)
        return res.data
      })
    },
    // refetchInterval: 10000,
  })

  const messagesQuery = useQuery({
    queryKey: ['messages', currentConversation],
    queryFn: async () => {
      if (!currentConversation.value) return []

      const { otherUserId, listingId } = currentConversation.value
      const response = await fetchClient
        .GET('/api/chat/conversation/{listingId}/{otherUserId}', {
          params: {
            path: {
              listingId,
              otherUserId,
            },
          },
        })
        .then((res) => res.data)

      if (response && response.length > 0) {
        const newestMessage = response[0]
        lastTimestamp.value = new Date(newestMessage.timestamp).getTime()
      }

      return response || []
    },
    enabled: computed(() => !!currentConversation.value),
  })

  const newMessagesQuery = useQuery({
    queryKey: ['newMessages', currentConversation, lastTimestamp],
    queryFn: async () => {
      if (!currentConversation.value) return []

      const { otherUserId, listingId } = currentConversation.value
      try {
        const response = await fetchClient
          .GET('/api/chat/poll/{listingId}/{otherUserId}', {
            params: {
              path: {
                listingId,
                otherUserId,
              },
              query: {
                lastTimestamp: lastTimestamp.value,
              },
            },
          })
          .then((res) => res.data)

        if (response && response.length > 0) {
          const newestMessage = response[0]
          lastTimestamp.value = new Date(newestMessage.timestamp).getTime()

          queryClient.setQueryData(
            ['messages', currentConversation.value],
            (oldData: ChatMessage[] | undefined) => {
              return [...response, ...(oldData || [])]
            },
          )
        }

        return response || []
      } catch (error) {
        if (error instanceof Error && error.name === 'AbortError') {
          return []
        }
        throw error
      }
    },
    refetchInterval: 1000,
    enabled: computed(() => !!currentConversation.value),
    refetchOnWindowFocus: false,
    notifyOnChangeProps: ['data'],
  })

  const sendMessageMutation = useMutation({
    mutationFn: async (content: string) => {
      if (!currentConversation.value || !content.trim()) return null

      const { otherUserId, listingId } = currentConversation.value

      const request: ChatMessageRequest = {
        content,
        recipientId: otherUserId,
        listingId,
      }

      return await fetchClient
        .POST('/api/chat/send', {
          body: request,
        })
        .then((res) => res.data)
    },
    onSuccess: (data) => {
      if (!data) return

      lastTimestamp.value = new Date(data.timestamp).getTime()

      queryClient.setQueryData(
        ['messages', currentConversation.value],
        (oldData: ChatMessage[] | undefined) => {
          return [data, ...(oldData || [])]
        },
      )

      newMessage.value = ''
    },
  })

  function setConversation(otherUserId: number, listingId: number) {
    currentConversation.value = { otherUserId, listingId }
    lastTimestamp.value = Date.now()
  }

  function sendMessage() {
    if (!newMessage.value.trim()) return
    sendMessageMutation.mutate(newMessage.value)
  }

  return {
    conversationsQuery,
    messagesQuery,
    newMessagesQuery,

    currentConversation,
    newMessage,

    setConversation,
    sendMessage,

    conversations: computed(() => conversationsQuery.data.value || []),
    messages: computed(() => messagesQuery.data.value || []),
    isLoadingConversations: computed(() => conversationsQuery.isLoading.value),
    isLoadingMessages: computed(() => messagesQuery.isLoading.value),
    isSendingMessage: computed(() => sendMessageMutation.isPending.value),
  }
}

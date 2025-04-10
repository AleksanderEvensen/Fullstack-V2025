import { ref, computed } from 'vue'
import { useInfiniteQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { fetchClient } from '@/lib/api/client'
import type { components } from '@/lib/api/schema'

type ChatMessageRequest = components['schemas']['ChatMessageRequestDto']
type PageConversationSummaryDto = components['schemas']['PageConversationSummaryDto']
type PageChatMessageDto = components['schemas']['PageChatMessageDto']

export function useChatMessages() {
  const queryClient = useQueryClient()
  const currentConversation = ref<{ otherUserId: number; listingId: number } | null>(null)
  const newMessage = ref('')

  // Get all user conversations with infinite query
  const conversationsQuery = useInfiniteQuery({
    queryKey: ['conversations', 'infinite'],
    queryFn: async ({ pageParam = 0 }) => {
      const response = await fetchClient.GET('/api/chat/conversations', {
        params: {
          query: {
            page: pageParam,
            size: 10,
          },
        },
      })
      return response.data as PageConversationSummaryDto
    },
    getNextPageParam: (lastPage) => {
      if (lastPage.last) return undefined

      return (lastPage.number ?? 0) + 1
    },
    initialPageParam: 0,
    refetchInterval: 5000, // Refetch every 5 seconds instead of polling
  })

  // Get messages for the current conversation with infinite query
  const messagesQuery = useInfiniteQuery({
    queryKey: ['messages', 'infinite', currentConversation],
    queryFn: async ({ pageParam = 0 }) => {
      if (!currentConversation.value) return { content: [], last: true } as PageChatMessageDto

      const { otherUserId, listingId } = currentConversation.value
      const response = await fetchClient.GET('/api/chat/conversation/{listingId}/{otherUserId}', {
        params: {
          path: {
            listingId,
            otherUserId,
          },
          query: {
            page: pageParam,
            size: 20,
          },
        },
      })
      return response.data as PageChatMessageDto
    },
    getNextPageParam: (lastPage) => {
      if (!lastPage) return undefined

      if (lastPage.last) return undefined

      return (lastPage.number ?? 0) + 1
    },
    initialPageParam: 0,
    enabled: computed(() => !!currentConversation.value),
    refetchInterval: 3000, // Refetch every 3 seconds instead of polling
  })

  // Send a new message
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

      // Invalidate the messages query to trigger a refetch
      queryClient.invalidateQueries({
        queryKey: ['messages', 'infinite', currentConversation.value],
      })

      // Clear message input
      newMessage.value = ''
    },
  })

  function setConversation(otherUserId: number, listingId: number) {
    currentConversation.value = { otherUserId, listingId }

    // Reset any existing query data for the new conversation
    queryClient.resetQueries({ queryKey: ['messages', 'infinite', currentConversation.value] })
  }

  function sendMessage() {
    if (!newMessage.value.trim()) return
    sendMessageMutation.mutate(newMessage.value)
  }

  function fetchNextConversationsPage() {
    if (conversationsQuery.hasNextPage.value) {
      conversationsQuery.fetchNextPage()
    }
  }

  function fetchNextMessagesPage() {
    if (messagesQuery.hasNextPage.value) {
      messagesQuery.fetchNextPage()
    }
  }

  // Compute flattened arrays from infinite query pages
  const conversations = computed(() => {
    return conversationsQuery.data.value?.pages.flatMap((page) => page.content ?? []) ?? []
  })

  const messages = computed(() => {
    return messagesQuery.data.value?.pages.flatMap((page) => page.content ?? []) ?? []
  })

  return {
    conversationsQuery,
    messagesQuery,
    currentConversation,
    newMessage,

    setConversation,
    sendMessage,
    fetchNextConversationsPage,
    fetchNextMessagesPage,

    conversations,
    messages,
    hasMoreConversations: computed(() => conversationsQuery.hasNextPage.value),
    hasMoreMessages: computed(() => messagesQuery.hasNextPage.value),
    isLoadingConversations: computed(() => conversationsQuery.isLoading.value),
    isFetchingNextConversations: computed(() => conversationsQuery.isFetchingNextPage.value),
    isLoadingMessages: computed(() => messagesQuery.isLoading.value),
    isFetchingNextMessages: computed(() => messagesQuery.isFetchingNextPage.value),
    isSendingMessage: computed(() => sendMessageMutation.isPending.value),
  }
}

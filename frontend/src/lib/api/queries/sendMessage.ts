import { QueryClient, useMutation } from '@tanstack/vue-query'
import type { components } from '../schema'
import { fetchClient } from '../client'

type ChatMessageRequest = components['schemas']['ChatMessageRequestDto']
export function useSendMessage() {
  const queryClient = new QueryClient()
  return useMutation({
    mutationFn: async (input: ChatMessageRequest) => {
      const response = await fetchClient.POST('/api/chat/send', {
        body: input,
      })
      return response.data
    },
    onSuccess: (data) => {
      if (!data) return

      queryClient.invalidateQueries({
        queryKey: [
          'messages',
          'infinite',
          { otherUserId: data.recipient.id, listingId: data.listingId },
        ],
      })
    },
  })
}

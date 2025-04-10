import { useInfiniteQuery } from '@tanstack/vue-query'
import { fetchClient } from '../client'
import type { components } from '@/lib/api/schema'

type PageConversationSummaryDto = components['schemas']['PageConversationSummaryDto']

export function getInfiniteConversations(pageSize = 10) {
  return useInfiniteQuery({
    queryKey: ['conversations', 'infinite', pageSize],
    queryFn: async ({ pageParam = 0 }) => {
      const response = await fetchClient.GET('/api/chat/conversations', {
        params: {
          query: {
            page: pageParam,
            size: pageSize,
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
  })
}

export function getInfiniteMessages(listingId: number, otherUserId: number) {
  return useInfiniteQuery({
    queryKey: ['messages', 'infinite', listingId, otherUserId],
    queryFn: async ({ pageParam = 0 }) => {
      const response = await fetchClient.GET('/api/chat/conversation/{listingId}/{otherUserId}', {
        params: {
          path: {
            listingId,
            otherUserId,
          },
          query: {
            page: pageParam,
          },
        },
      })
      return response.data as components['schemas']['PageChatMessageDto']
    },
    getNextPageParam: (lastPage) => {
      if (!lastPage) return undefined

      if (lastPage.last) return undefined

      return (lastPage.number ?? 0) + 1
    },
    initialPageParam: 0,
  })
}

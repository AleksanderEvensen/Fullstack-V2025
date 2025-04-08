import { useQuery } from '@tanstack/vue-query'
import type { paths } from '../schema'
import { fetchClient } from '@/lib/api/client'

export const LISTING_QUERY_KEY = 'listings'

type ListingInput = paths['/api/listings']['get']['parameters']['query']
export function getListings(input: ListingInput) {
  return useQuery({
    queryKey: [LISTING_QUERY_KEY],
    queryFn: async () => {
      const response = await fetchClient.GET('/api/listings', {
        params: {
          query: input,
        },
      })
      return response.data
    },
  })
}

export function getListing(id: number) {
  return useQuery({
    queryKey: [LISTING_QUERY_KEY, id],
    queryFn: async () => {
      const response = await fetchClient.GET('/api/listings/{id}', {
        params: {
          path: {
            id,
          },
        },
      })
      return response.data
    },
  })
}

type ListingSearchInput = paths['/api/listings/search']['get']['parameters']['query']
export function searchListings(input: ListingSearchInput) {
  return useQuery({
    queryKey: [LISTING_QUERY_KEY, 'search'],
    queryFn: async () => {
      const response = await fetchClient.GET('/api/listings/search', {
        params: {
          query: input,
        },
      })
      return response.data
    },
  })
}

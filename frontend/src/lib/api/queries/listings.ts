import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
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
    queryKey: [LISTING_QUERY_KEY, 'search', input],
    queryFn: async () => {
      const response = await fetchClient.GET('/api/listings/search', {
        params: {
          query: {
            ...input,
          },
        },
      })
      return response.data
    },
  })
}

type Listing = paths['/api/listings']['post']['requestBody']['content']['application/json']
export function useCreateListing() {
  return useMutation({
    mutationFn: async (listing: Listing) => {
      const response = await fetchClient.POST('/api/listings', {
        body: listing,
      })
      return response.data
    },
  })
}

export function useBookmarkListing() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: async (id: number) => {
      const response = await fetchClient.POST('/api/bookmarks/{listingId}', {
        params: {
          path: {
            listingId: id,
          },
        },
      })
      return response.data
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LISTING_QUERY_KEY] })
    },
  })
}

export function useUnbookmarkListing() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: async (id: number) => {
      const response = await fetchClient.DELETE('/api/bookmarks/{listingId}', {
        params: {
          path: {
            listingId: id,
          },
        },
      })
      return response.data
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [LISTING_QUERY_KEY] })
    },
  })
}

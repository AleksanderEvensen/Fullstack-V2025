import { useQuery } from '@tanstack/vue-query'
import { fetchClient } from '@/lib/api/client'

export const CATEGORIES_QUERY_KEY = 'categories'
export function getCategories() {
  return useQuery({
    queryKey: [CATEGORIES_QUERY_KEY],
    queryFn: async () => {
      const response = await fetchClient.GET('/api/categories')
      return response.data
    },
  })
}

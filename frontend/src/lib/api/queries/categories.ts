import { useMutation, useQuery } from '@tanstack/vue-query'
import { fetchClient } from '@/lib/api/client'
import type { paths } from '../schema'

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

type Category = paths['/api/categories']['post']['requestBody']['content']['application/json']
export function useCreateCategory() {
  return useMutation({
    mutationFn: async (category: Category) => {
      const response = await fetchClient.POST('/api/categories', {
        body: category,
      })
      return response.data
    },
  })
}

import type { paths } from '@/lib/api/schema'
import { ref, computed, watch } from 'vue'
import { useUrlSearchParams } from '@vueuse/core'
import { searchListings } from '@/lib/api/queries/listings'
import type { components } from '@/lib/api/schema'

type ListingSearchParams = paths['/api/listings/search']['get']['parameters']['query']
type ListingDto = components['schemas']['ListingDto']
type PageListingDto = components['schemas']['PageListingDto']

export function useListingSearch() {
  // State management
  const searchParams = ref<Partial<ListingSearchParams>>({
    page: 0,
    size: 20,
    sortBy: 'createdAt',
    sortDirection: 'DESC',
  })

  // URL parameters handling
  const queryParams = useUrlSearchParams('history', {
    removeFalsyValues: true,
    removeNullishValues: true,
  })

  // Query results
  const listings = ref<ListingDto[]>([])
  const pagination = ref({
    pageNumber: 0,
    totalPages: 0,
    totalElements: 0,
    size: 20,
    isFirstPage: true,
    isLastPage: true,
  })
  const isLoading = ref(false)
  const isError = ref(false)

  const { data, isLoading: queryLoading, isError: queryError } = searchListings(searchParams.value)

  watch(data, (newData) => {
    if (newData) {
      updateResultsFromResponse(newData)
    }
  })

  watch(queryLoading, (loading) => {
    isLoading.value = loading
  })

  watch(queryError, (error) => {
    isError.value = !!error
  })

  const updateSearch = (params: Partial<ListingSearchParams>) => {
    searchParams.value = {
      ...searchParams.value,
      ...params,
    }

    if (params) {
      Object.entries(params as Record<string, any>).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== '') {
          queryParams[key] = String(value)
        } else {
          delete queryParams[key]
        }
      })
    }
  }

  const initFromUrlParams = () => {
    const params: Record<string, any> = {}

    Object.entries(queryParams).forEach(([key, value]) => {
      if (!value) return

      const stringValue =
        typeof value === 'string' ? value : Array.isArray(value) ? value[0] : String(value)

      switch (key) {
        case 'q':
        case 'title':
        case 'description':
        case 'condition':
        case 'manufacturer':
        case 'model':
        case 'modelYear':
        case 'sortBy':
        case 'sortDirection':
          params[key] = stringValue
          break
        case 'minPrice':
        case 'maxPrice':
          params[key] = parseFloat(stringValue)
          break
        case 'categoryId':
        case 'sellerId':
        case 'defectsCount':
        case 'modificationsCount':
          params[key] = parseInt(stringValue, 10)
          break
        case 'page':
        case 'size':
          params[key] = parseInt(stringValue, 10)
          break
        default:
          break
      }
    })

    if (Object.keys(params).length > 0) {
      updateSearch(params as Partial<ListingSearchParams>)
    }
  }

  const updateResultsFromResponse = (response: PageListingDto) => {
    listings.value = response.content || []
    pagination.value = {
      pageNumber: response.pageable?.pageNumber || 0,
      totalPages: response.totalPages || 0,
      totalElements: response.totalElements || 0,
      size: response.size || 20,
      isFirstPage: response.first || false,
      isLastPage: response.last || false,
    }
  }

  const isEmpty = computed(() => !isLoading.value && !isError.value && listings.value.length === 0)

  initFromUrlParams()
  watch(
    () => queryParams.q,
    (newValue) => {
      if (newValue !== undefined) {
        const stringQuery =
          typeof newValue === 'string'
            ? newValue
            : Array.isArray(newValue)
              ? newValue[0]
              : String(newValue)
        updateSearch({ q: stringQuery })
      }
    },
  )

  return {
    searchParams,
    listings,
    pagination,
    isLoading,
    isError,
    isEmpty,
    updateSearch,
  }
}

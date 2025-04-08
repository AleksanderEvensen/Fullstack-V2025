// Define the API response type for listings
export interface ListingResponse {
  id: number
  title: string
  price: number
  originalPrice?: number
  condition: string
  description: string
  categoryId: number
  modelYear?: string
  manufacturer?: string
  model?: string
  serialNumber?: string
  purchaseDate?: string
  usageDuration?: string
  defects: string[]
  modifications: string[]
  reasonForSelling?: string
  images: string[]
  createdAt: string
  seller: {
    id: number
    username: string
    avatar?: string
  }
}

// Define interface for search form params
export interface ListingSearchParams {
  q?: string
  title?: string
  description?: string
  categoryId?: number
  condition?: string
  minPrice?: number
  maxPrice?: number
  modelYear?: string
  manufacturer?: string
  model?: string
  sellerId?: number
  defectsCount?: number
  modificationsCount?: number
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: string
}

// Define the pagination response
export interface PaginatedResponse<T> {
  content: T[]
  pageable: {
    pageNumber: number
    pageSize: number
    sort: {
      empty: boolean
      sorted: boolean
      unsorted: boolean
    }
    offset: number
    paged: boolean
    unpaged: boolean
  }
  last: boolean
  totalElements: number
  totalPages: number
  size: number
  number: number
  sort: {
    empty: boolean
    sorted: boolean
    unsorted: boolean
  }
  first: boolean
  numberOfElements: number
  empty: boolean
}

// Type for categories
export interface Category {
  id: number
  name: string
  description: string
  translationString: string
  icon: string
}

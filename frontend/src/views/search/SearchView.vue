<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { MapPin, FunnelIcon } from 'lucide-vue-next'
import { ref, computed } from 'vue'
import { useUrlSearchParams } from '@vueuse/core'
import SearchFilter from './components/SearchFilter.vue'
import type { paths } from '@/lib/api/schema'
import ProductCard from '../home/components/ProductCard.vue'
import { useListingSearch } from './composables/useListingSearch'

type ListingSearchParams = paths['/api/listings/search']['get']['parameters']['query']
const queryParams = useUrlSearchParams('history', {
  removeFalsyValues: true,
  removeNullishValues: true,
})

const {
  searchParams,
  listings,
  pagination,
  isLoading,
  isError,
  updateSearch
} = useListingSearch()

const filtersPanelOpen = ref(true)
const sortOptions = [
  { value: 'createdAt,DESC', label: 'Newest first' },
  { value: 'createdAt,ASC', label: 'Oldest first' },
  { value: 'price,ASC', label: 'Price: Low to High' },
  { value: 'price,DESC', label: 'Price: High to Low' },
]
const sortBy = ref('createdAt,DESC')

const toggleFiltersPanel = () => {
  filtersPanelOpen.value = !filtersPanelOpen.value
}

const handleFilter = (params: Partial<ListingSearchParams>) => {
  if (params) {
    if (params.condition && !['NEW', 'LIKE_NEW', 'VERY_GOOD', 'GOOD', 'ACCEPTABLE'].includes(params.condition)) {
      delete params.condition
    }
    updateSearch(params)
  }

  Object.entries(params ?? {}).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      queryParams[key] = String(value)
    } else {
      delete queryParams[key]
    }
  })
}

const handleSortChange = (value: unknown) => {
  if (typeof value === 'string') {
    sortBy.value = value
    if (value && value.includes(',')) {
      const [sort, direction] = value.split(',')
      updateSearch({ sortBy: sort, sortDirection: direction })
      queryParams.sortBy = sort
      queryParams.sortDirection = direction
    }
  }
}

const initFromUrlParams = () => {
  const params: Record<string, any> = {}

  Object.entries(queryParams).forEach(([key, value]) => {
    const stringValue = typeof value === 'string' ? value : Array.isArray(value) ? value[0] : String(value)

    switch (key) {
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
        params.categoryId = parseInt(stringValue, 10)
        break
      case 'page':
      case 'size':
        params[key] = parseInt(stringValue, 10)
        break
      default:
        break
    }
  })

  if (params.sortBy && params.sortDirection) {
    sortBy.value = `${params.sortBy},${params.sortDirection}`
  }

  if (Object.keys(params).length > 0) {
    updateSearch(params as Partial<ListingSearchParams>)
  }
}

initFromUrlParams()

const totalCountMessage = computed(() => {
  return `${pagination.value.totalElements} ${pagination.value.totalElements === 1 ? 'result' : 'results'}`
})

const handleSearchQueryChange = () => {
  const query = queryParams.q
  if (query !== undefined) {
    const stringQuery = typeof query === 'string' ? query : Array.isArray(query) ? query[0] : String(query)
    handleFilter({ q: stringQuery })
  }
}
</script>

<template>
  <div class="container">
    <header class="search-header">
      <h1 class="search-title">Find what your heart desires</h1>
      <span class="search-count">{{ totalCountMessage }}</span>
    </header>

    <div class="search-query">
      <Input
        :model-value="typeof queryParams.q === 'string' ? queryParams.q : Array.isArray(queryParams.q) ? queryParams.q[0] : ''"
        @input="(event: Event) => queryParams.q = (event.target as HTMLInputElement).value"
        placeholder="Search for items..." class="search-input" @keyup.enter="handleSearchQueryChange" />
      <Button @click="handleSearchQueryChange">Search</Button>
    </div>

    <div class="search-container">
      <!-- Left sidebar with filters -->
      <SearchFilter v-if="filtersPanelOpen"
        @filter="(params) => handleFilter(params as Partial<ListingSearchParams>)" />

      <!-- Right side with search results -->
      <div class="search-results">
        <div class="search-controls">
          <Button variant="outline" @click="toggleFiltersPanel">
            <FunnelIcon :size="20" />
            <span class="control-text">{{ filtersPanelOpen ? 'Hide Filters' : 'Show Filters' }}</span>
          </Button>

          <Button variant="outline" class="map-button">
            <MapPin :size="20" />
            <span class="control-text">View on Map</span>
          </Button>

          <Select v-model="sortBy as any" class="sort-select">
            <SelectTrigger>
              <SelectValue placeholder="Sort by" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem v-for="option in sortOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>

        <!-- Loading state -->
        <div v-if="isLoading" class="loading-state">
          <p>Loading results...</p>
        </div>

        <!-- Error state -->
        <div v-else-if="isError" class="error-state">
          <p>Sorry, there was an error loading the listings. Please try again.</p>
          <Button @click="initFromUrlParams">Retry</Button>
        </div>

        <!-- Empty state -->
        <div v-else-if="listings.length === 0" class="empty-state">
          <p>No listings match your search criteria. Try adjusting your filters.</p>
        </div>

        <!-- Results list -->
        <div v-else class="search-results-list">
          <ProductCard v-for="(item, index) in listings" :key="index" :product="item" />
        </div>

        <!-- Pagination -->
        <div v-if="pagination.totalPages > 1" class="pagination">
          <Button variant="outline" size="sm" :disabled="pagination.isFirstPage"
            @click="updateSearch({ page: pagination.pageNumber - 1 })">
            Previous
          </Button>

          <span class="page-info">
            Page {{ pagination.pageNumber + 1 }} of {{ pagination.totalPages }}
          </span>

          <Button variant="outline" size="sm" :disabled="pagination.isLastPage"
            @click="updateSearch({ page: pagination.pageNumber + 1 })">
            Next
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 2rem 0;
  max-width: 1200px;
  margin: 0 auto;
}

.search-header {
  margin-bottom: 1.5rem;
}

.search-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
}

.search-count {
  font-size: 0.875rem;
  color: var(--muted-foreground);
}

.search-query {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.search-input {
  flex: 1;
}

.search-container {
  display: flex;
  gap: 1.5rem;
}

.search-results {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.search-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.map-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.control-text {
  display: none;
}

.sort-select {
  margin-left: auto;
}

.loading-state,
.error-state,
.empty-state {
  padding: 3rem;
  text-align: center;
  background-color: var(--background);
  border: 1px solid var(--border);
  border-radius: var(--radius);
}

.error-state button {
  margin-top: 1rem;
}

.search-results-list {
  display: grid;
  gap: 1rem;
  grid-template-columns: 1fr;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
}

.page-info {
  font-size: 0.875rem;
}

/* Media queries for responsive design */
@media (min-width: 640px) {
  .control-text {
    display: inline;
  }

  .search-results-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .search-results-list {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
  }

  .sort-select {
    margin-left: 0;
    width: 100%;
  }

  .search-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .map-button,
  .sort-select {
    width: 100%;
  }
}
</style>

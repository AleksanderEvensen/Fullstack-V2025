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
import { ref, computed, watch } from 'vue'
import { useUrlSearchParams, watchDebounced } from '@vueuse/core'
import SearchFilter from './components/SearchFilter.vue'
import type { paths } from '@/lib/api/schema'
import ProductCard from '../home/components/ProductCard.vue'
import { searchListings } from '@/lib/api/queries/listings'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n()

type ListingSearchParams = paths['/api/listings/search']['get']['parameters']['query']
const queryParams = useUrlSearchParams<NonNullable<ListingSearchParams>>('history', {
  removeFalsyValues: true,
  removeNullishValues: true,
  initialValue: {
    sortBy: 'createdAt',
    sortDirection: 'DESC',
    size: 10,
  }
})

const filtersPanelOpen = ref(true)
const sortBy = ref('createdAt,DESC')

const localQuery = ref(queryParams.q)

const sortOptions = [
  { value: 'createdAt,DESC', label: t('search.sort.newest') },
  { value: 'createdAt,ASC', label: t('search.sort.oldest') },
  { value: 'price,ASC', label: t('search.sort.priceLowHigh') },
  { value: 'price,DESC', label: t('search.sort.priceHighLow') },
]

watch(sortBy, (newSortBy) => {
  const [field, direction] = newSortBy.split(',')
  queryParams['sortBy'] = field
  queryParams['sortDirection'] = direction
  queryParams.page = 0
})

watchDebounced(localQuery, (newQuery) => {
  queryParams.q = newQuery
}, {
  debounce: 500,
  immediate: true,
})

const debouncedQueryParams = ref(queryParams)
watchDebounced(queryParams, (newQueryParams) => {
  debouncedQueryParams.value = newQueryParams
}, {
  debounce: 500,
  immediate: true,
})

const { data, isLoading, isError } = searchListings(debouncedQueryParams.value)



const totalCountMessage = computed(() => {
  return t('search.resultsCount', {
    count: data.value?.totalElements || 0
  });
})

const goToPage = (page: number) => {
  queryParams.page = page
}

const goToPreviousPage = computed(() => {
  return () => {
    if (!data.value?.first) {
      goToPage((data.value?.pageable?.pageNumber || 0) - 1)
    }
  }
})

const goToNextPage = computed(() => {
  return () => {
    if (!data.value?.last) {
      goToPage((data.value?.pageable?.pageNumber || 0) + 1)
    }
  }
})

const handleFilterChange = (filters: NonNullable<ListingSearchParams>) => {
  Object.entries(filters).forEach(([key, value]) => {
    (queryParams as Record<string, unknown>)[key] = value;
  })
}

</script>

<template>
  <div class="container">
    <header class="search-header">
      <h1 class="search-title">{{ t('search.title') }}</h1>
      <span class="search-count">{{ totalCountMessage }}</span>
    </header>

    <div class="search-query">
      <Input :placeholder="t('search.searchPlaceholder')" class="search-input" v-model="localQuery" />
    </div>

    <div class="search-container">
      <SearchFilter v-if="filtersPanelOpen" @filter-change="handleFilterChange" />

      <div class="search-results">
        <div class="search-controls">
          <div class="search-controls-left">
            <Button variant="outline" @click="filtersPanelOpen = !filtersPanelOpen">
              <FunnelIcon :size="20" />
              <span class="control-text">{{ filtersPanelOpen ? t('search.hideFilters') : t('search.showFilters')
              }}</span>
            </Button>

            <Button variant="outline" class="map-button">
              <MapPin :size="20" />
              <span class="control-text">{{ t('search.viewOnMap') }}</span>
            </Button>
          </div>

          <div class="sort-select">
            <Select v-model="sortBy">
              <SelectTrigger>
                <SelectValue :placeholder="sortBy" />
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
        </div>

        <div v-if="isLoading" class="loading-state">
          <p>{{ t('common.loading') }}</p>
        </div>

        <div v-else-if="isError" class="error-state">
          <p>{{ t('search.errorLoading') }}</p>
        </div>

        <div v-else-if="data?.content?.length === 0" class="empty-state">
          <p>{{ t('search.noResults') }}</p>
        </div>

        <div v-else class="search-results-list">
          <ProductCard v-for="(item, index) in data?.content" :key="index" :product="item" />
        </div>

        <div v-if="data?.totalPages && data?.totalPages > 1" class="pagination">
          <Button variant="outline" size="sm" :disabled="data?.first" @click="goToPreviousPage">
            {{ t('common.previous') }}
          </Button>

          <span class="page-info">
            {{ t('search.pagination', { current: (data?.pageable?.pageNumber || 0) + 1, total: data?.totalPages }) }}
          </span>

          <Button variant="outline" size="sm" :disabled="data?.last" @click="goToNextPage">
            {{ t('common.next') }}
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 2rem 2rem;
  max-width: 1500px;
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
  justify-content: space-between;
}

.map-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.control-text {
  display: none;
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

.search-controls-left {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
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

@media (max-width: 850px) {
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

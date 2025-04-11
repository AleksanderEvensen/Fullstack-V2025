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
import { MapPin, FunnelIcon, X, House } from 'lucide-vue-next'
import { ref, computed, watch } from 'vue'
import { useUrlSearchParams, watchDebounced } from '@vueuse/core'
import SearchFilter from './components/SearchFilter.vue'
import type { paths } from '@/lib/api/schema'
import ProductCard from '../home/components/ProductCard.vue'
import { useInfiniteListings } from '@/lib/api/queries/listings'
import { useTypedI18n } from '@/i18n'
import { MapboxMap, MapboxMarker, MapboxNavigationControl } from '@studiometa/vue-mapbox-gl'
import type { Map } from 'mapbox-gl'
import { MAPBOX_API_TOKEN } from '@/lib/utils'
import mapboxgl from 'mapbox-gl'

const { t } = useTypedI18n()

type ListingSearchParams = paths['/api/listings/search']['get']['parameters']['query']
const queryParams = useUrlSearchParams<Omit<NonNullable<ListingSearchParams>, 'page'>>('history', {
  removeFalsyValues: true,
  removeNullishValues: true,
  initialValue: {
    sortBy: 'createdAt',
    sortDirection: 'DESC',
    size: 10,
  },
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
  // Don't set page anymore
})

watchDebounced(
  localQuery,
  (newQuery) => {
    queryParams.q = newQuery
  },
  {
    debounce: 500,
    immediate: true,
  },
)

const debouncedQueryParams = ref(queryParams)
watchDebounced(
  queryParams,
  (newQueryParams) => {
    debouncedQueryParams.value = newQueryParams
  },
  {
    debounce: 500,
    immediate: true,
  },
)

// Replace regular query with infinite query
const { data, fetchNextPage, hasNextPage, isFetchingNextPage, isLoading, isError } =
  useInfiniteListings(debouncedQueryParams.value)

// Combine all pages of data into a single array for display
const allListings = computed(() => {
  if (!data.value) return []
  return data.value.pages.flatMap((page) => page?.content ?? [])
})

// Get total count from first page
const totalElements = computed(() => data.value?.pages[0]?.totalElements || 0)

const totalCountMessage = computed(() => {
  return t('search.resultsCount', {
    count: totalElements.value,
  })
})

const handleFilterChange = (filters: NonNullable<ListingSearchParams>) => {
  Object.entries(filters).forEach(([key, value]) => {
    if (key !== 'page') {
      // Skip page parameter
      ;(queryParams as Record<string, unknown>)[key] = value
    }
  })
}

const mapViewOpen = ref(false)
const mapRef = ref<Map | null>(null)

// Toggle map view dialog
function toggleMapView(forced?: boolean) {
  if (arguments.length) {
    mapViewOpen.value = Boolean(forced)
  } else {
    mapViewOpen.value = !mapViewOpen.value
  }
}

watch(mapViewOpen, (v) => {
  const dialogElem = document.querySelector('#mapDialog') as HTMLDialogElement
  if (v) {
    dialogElem.showModal()

    if (mapViewOpen.value) {
      // Calculate bounds on next tick after map is loaded
      setTimeout(fitMapBounds, 20)
    }
  } else {
    dialogElem.close()
  }
})

// Fit map to show all markers
function fitMapBounds() {
  if (!mapRef.value || !allListings.value.length) return

  const bounds = new mapboxgl.LngLatBounds()
  let hasValidCoordinates = false
  allListings.value.forEach((item) => {
    if (item.longitude && item.latitude) {
      bounds.extend([item.longitude, item.latitude])
      hasValidCoordinates = true
    }
  })

  if (hasValidCoordinates) {
    mapRef.value.fitBounds(bounds, {
      padding: 50,
      maxZoom: 15,
      animate: false,
    })
  }
}

function handleMapCreated(map: Map) {
  mapRef.value = map
}

// Get listing coordinates for markers
const listingMarkers = computed(() => {
  if (!allListings.value.length) return []

  return allListings.value
    .filter((item) => item.longitude && item.latitude)
    .map((item) => ({
      id: item.id,
      longitude: item.longitude,
      latitude: item.latitude,
      title: item.title || 'Listing',
      price: item.price,
    }))
})
</script>

<template>
  <main class="search-page">
    <div class="container">
      <header class="search-header">
        <h1 class="search-title">{{ t('search.title') }}</h1>
        <span class="search-count">{{ totalCountMessage }}</span>
      </header>

      <div class="search-query">
        <Input
          :placeholder="t('search.searchPlaceholder')"
          class="search-input"
          v-model="localQuery"
        />
      </div>

      <div class="search-container">
        <SearchFilter v-if="filtersPanelOpen" @filter-change="handleFilterChange" />

        <div class="search-results">
          <div class="search-controls">
            <div class="search-controls-left">
              <Button variant="outline" @click="filtersPanelOpen = !filtersPanelOpen">
                <FunnelIcon :size="20" />
                <span class="control-text">{{
                  filtersPanelOpen ? t('search.hideFilters') : t('search.showFilters')
                }}</span>
              </Button>

              <Button variant="outline" class="map-button" @click="toggleMapView">
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
                    <SelectItem
                      v-for="option in sortOptions"
                      :key="option.value"
                      :value="option.value"
                    >
                      {{ option.label }}
                    </SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>
          </div>

          <div v-if="isLoading && !allListings.length" class="loading-state">
            <p>{{ t('common.loading') }}</p>
          </div>

          <div v-else-if="isError" class="error-state">
            <p>{{ t('search.errorLoading') }}</p>
          </div>

          <div v-else-if="allListings.length === 0" class="empty-state">
            <p>{{ t('search.noResults') }}</p>
          </div>

          <div v-else class="search-results-list">
            <ProductCard
              v-for="(item, index) in allListings"
              :key="item.id || index"
              :product="item"
            />
          </div>

          <div v-if="hasNextPage" class="load-more-container">
            <Button
              variant="outline"
              :disabled="isFetchingNextPage"
              @click="fetchNextPage"
              class="load-more-button"
            >
              {{ isFetchingNextPage ? t('common.loadingMore') : t('common.loadMore') }}
            </Button>
          </div>
        </div>
      </div>

      <dialog id="mapDialog">
        <div class="map-view">
          <div class="map-header">
            <h2>{{ t('search.mapView') }}</h2>
            <Button variant="ghost" size="icon" @click="toggleMapView(false)" class="close-button">
              <X :size="20" />
            </Button>
          </div>

          <div class="map-container">
            <MapboxMap
              style="height: 60vh; max-height: 500px"
              :access-token="MAPBOX_API_TOKEN"
              map-style="mapbox://styles/mapbox/streets-v12"
              @mb-created="handleMapCreated"
            >
              <MapboxMarker
                v-for="marker in listingMarkers"
                :key="marker.id"
                :lng-lat="[marker.longitude, marker.latitude]"
              >
                <div class="map-marker">
                  <House />
                </div>
              </MapboxMarker>

              <MapboxNavigationControl position="top-right" />
            </MapboxMap>
          </div>

          <div class="map-footer">
            <span>{{ totalCountMessage }}</span>
          </div>
        </div>
      </dialog>
    </div>
  </main>
</template>

<style scoped>
#mapDialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 100;
  padding: 0;
  border-radius: var(--radius);
  border: 1px solid var(--border);
  background-color: var(--background);
  width: 90vw;
  max-width: 1200px;
}
#mapDialog::backdrop {
  background-color: rgba(0, 0, 0, 0.5);
}

.map-view {
  display: flex;
  flex-direction: column;
}

.map-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid var(--border);
}

.map-header h2 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.map-container {
  flex: 1;
  width: 100%;
  height: 100%;
  min-height: 400px;
  position: relative;
}

.map-footer {
  padding: 0.75rem 1rem;
  border-top: 1px solid var(--border);
  font-size: 0.875rem;
  color: var(--muted-foreground);
}

.close-button {
  cursor: pointer;
}

.map-marker {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  color: white;
  background-color: var(--primary);
  border-radius: 50%;
  cursor: pointer;
  transition: transform 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.map-marker:hover {
  transform: scale(1.1);
}

.marker-popup {
  padding: 0.5rem;
  max-width: 220px;
}

.marker-popup h3 {
  margin: 0.5rem 0;
  font-size: 1rem;
  font-weight: 600;
}

.marker-popup p {
  margin: 0 0 0.75rem;
  font-size: 0.875rem;
  color: var(--muted-foreground);
}

.marker-image {
  width: 100%;
  height: 120px;
  border-radius: var(--radius);
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.marker-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

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

.load-more-container {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
  margin-bottom: 1rem;
}

.load-more-button {
  min-width: 120px;
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

  .map-view {
    width: 95vw;
    height: 90vh;
  }
}

@media (max-width: 480px) {
  .map-header h2 {
    font-size: 1rem;
  }

  .map-footer {
    padding: 0.5rem;
  }
}
</style>

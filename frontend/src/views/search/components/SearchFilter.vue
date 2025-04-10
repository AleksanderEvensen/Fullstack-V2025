<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Checkbox } from '@/components/ui/checkbox'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { ChevronRight, TrashIcon } from 'lucide-vue-next'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible'
import { SliderReusable } from '@/components/ui/slider'
import type { paths } from '@/lib/api/schema'
import { useDebounceFn, useUrlSearchParams } from '@vueuse/core'
import { getCategories } from '@/lib/api/queries/categories'
import { useTypedI18n } from '@/i18n'
import { MapboxMap, MapboxMarker, MapboxNavigationControl } from '@studiometa/vue-mapbox-gl'
import { MAPBOX_API_TOKEN } from '@/lib/utils'
import type { Map } from 'mapbox-gl'
import * as turf from '@turf/turf'

import { SliderRoot, SliderTrack, SliderThumb, SliderRange } from '@/components/ui/slider'

const { t } = useTypedI18n()

type ListingSearchParams = paths['/api/listings/search']['get']['parameters']['query']
type Condition = 'NEW' | 'LIKE_NEW' | 'VERY_GOOD' | 'GOOD' | 'ACCEPTABLE'

const emit = defineEmits<{
  'filter-change': [filters: NonNullable<ListingSearchParams>]
}>()

const urlParams = useUrlSearchParams<NonNullable<ListingSearchParams>>('history', {
  removeFalsyValues: true,
  removeNullishValues: true,
  writeMode: 'replace',
})

const filterByPrice = ref<boolean>(false)
const filterByYear = ref<boolean>(false)

const localFilters = reactive<NonNullable<ListingSearchParams>>({
  q: getUrlParamValue('q') as string | undefined,
  minPrice: getUrlParamValue('minPrice') as number | undefined,
  maxPrice: getUrlParamValue('maxPrice') as number | undefined,
  condition: getUrlParamValue('condition') as Condition | undefined,
  manufacturer: getUrlParamValue('manufacturer') as string | undefined,
  minModelYear: getUrlParamValue('minModelYear') as number | undefined,
  maxModelYear: getUrlParamValue('maxModelYear') as number | undefined,
  categoryName: getUrlParamValue('categoryName') as string | undefined,
  latitude: getUrlParamValue('latitude') as number | undefined,
  longitude: getUrlParamValue('longitude') as number | undefined,
  radiusKm: getUrlParamValue('radiusKm') as number | undefined,
})

const priceValue = ref<[number, number]>([
  localFilters.minPrice ? Number(localFilters.minPrice) : 0,
  localFilters.maxPrice ? Number(localFilters.maxPrice) : 10000,
])

const yearValue = ref<[number, number]>([
  localFilters.minModelYear ? Number(localFilters.minModelYear) : new Date().getFullYear() - 10,
  localFilters.maxModelYear ? Number(localFilters.maxModelYear) : new Date().getFullYear(),
])

const mapRef = ref<Map>()
const currentLocation = ref<{ lat: number; lon: number } | null>(localFilters.latitude && localFilters.longitude
  ? {
      lat: Number(localFilters.latitude),
      lon: Number(localFilters.longitude),
    }
  : null)
const locationRangeKm = ref<number[]>([
  localFilters.radiusKm ? Number(localFilters.radiusKm) : 5
])
function mapClicked(event: { lngLat: { lat: number; lng: number } }) {
  currentLocation.value = {
    lat: event.lngLat.lat,
    lon: event.lngLat.lng,
  }
}

function mapCreated(map: Map) {
  mapRef.value = map;
  map.jumpTo({ center: [8.99, 60.58], zoom: 5.0 })
  map.once('load', updateCircle);
}

watch(currentLocation, () => {
  
  if (currentLocation.value) {
    localFilters.latitude = currentLocation.value.lat
    localFilters.longitude = currentLocation.value.lon
  } else {
    localFilters.latitude = undefined
    localFilters.longitude = undefined
  }

  updateCircle()
}, { immediate: true });

watch(locationRangeKm, () => {
  if (locationRangeKm.value[0]) {
    if (locationRangeKm.value[0] === 5) {
      localFilters.radiusKm = undefined
    } else {
      localFilters.radiusKm = locationRangeKm.value[0]
    }
    
  }
  updateCircle()
}, { immediate: true })

function updateCircle() {
  if (!mapRef.value) return
  const map = mapRef.value
  const layerId = 'circle-layer'
  const sourceId = 'circle-source'

  if (!currentLocation.value) {
    map.removeSource(sourceId)
    map.removeLayer(layerId)
    return
  }

  const source = map.getSource(sourceId) as mapboxgl.GeoJSONSource | undefined

  const circleData = turf.circle(
    [currentLocation.value.lon, currentLocation.value.lat],
    locationRangeKm.value[0],
    { units: 'kilometers' },
  )

  if (source) {
    source.setData(circleData)
  } else {
    map.addSource(sourceId, {
      type: 'geojson',
      data: circleData,
    })

    map.addLayer({
      id: layerId,
      type: 'fill',
      source: sourceId,
      paint: {
        'fill-color': '#3887be',
        'fill-opacity': 0.2,
        'fill-outline-color': '#3887be',
      },
    })
  }
}

function clearMapMarker() {
  currentLocation.value = null
}

const currentYear: number = new Date().getFullYear()

watch(
  priceValue,
  (newValue) => {
    if (newValue && newValue.length === 2) {
      localFilters.minPrice = newValue[0]
      localFilters.maxPrice = newValue[1]
    }
  },
  { deep: true },
)

watch(
  yearValue,
  (newValue) => {
    if (newValue && newValue.length === 2) {
      localFilters.minModelYear = newValue[0]
      localFilters.maxModelYear = newValue[1]
    }
  },
  { deep: true },
)

const { data: categories } = getCategories()

function getUrlParamValue<K extends keyof NonNullable<ListingSearchParams>>(
  key: K,
): NonNullable<ListingSearchParams>[K] | undefined {
  return urlParams[key] as NonNullable<ListingSearchParams>[K] | undefined
}

const updateUrlParams = useDebounceFn(() => {
  if (!filterByPrice.value) {
    localFilters.minPrice = undefined
    localFilters.maxPrice = undefined
  }
  if (!filterByYear.value) {
    localFilters.minModelYear = undefined
    localFilters.maxModelYear = undefined
  }
  const payload: NonNullable<ListingSearchParams> = {
    ...localFilters,
    page: 0,
  }

  emit('filter-change', payload)
}, 500)

watch(localFilters, updateUrlParams, { deep: true })

watch(
  filterByPrice,
  (newValue: boolean) => {
    if (!newValue) {
      localFilters.minPrice = undefined
      localFilters.maxPrice = undefined
    } else {
      localFilters.minPrice = priceValue.value[0]
      localFilters.maxPrice = priceValue.value[1]
    }

    updateUrlParams()
  },
  { deep: true },
)

watch(
  filterByYear,
  (newValue: boolean) => {
    if (!newValue) {
      localFilters.minModelYear = undefined
      localFilters.maxModelYear = undefined
    } else {
      localFilters.minModelYear = yearValue.value[0]
      localFilters.maxModelYear = yearValue.value[1]
    }
    updateUrlParams()
  },
  { deep: true },
)

const clearAllFilters = (): void => {
  localFilters.q = ''
  localFilters.minPrice = undefined
  localFilters.maxPrice = undefined
  localFilters.condition = undefined
  localFilters.manufacturer = undefined
  localFilters.minModelYear = undefined
  localFilters.maxModelYear = undefined
  localFilters.categoryName = undefined
  localFilters.latitude = undefined
  localFilters.longitude = undefined
  localFilters.radiusKm = undefined
  updateUrlParams()
}

interface ConditionOption {
  label: string
  value: Condition
}

const conditionOptions: ConditionOption[] = [
  { label: t('product.conditionLabels.new'), value: 'NEW' },
  { label: t('product.conditionLabels.likeNew'), value: 'LIKE_NEW' },
  { label: t('product.conditionLabels.veryGood'), value: 'VERY_GOOD' },
  { label: t('product.conditionLabels.good'), value: 'GOOD' },
  { label: t('product.conditionLabels.acceptable'), value: 'ACCEPTABLE' },
]
</script>

<template>
  <div class="filters-sidebar">
    <div class="filters-header">
      <h3>{{ t('search.filters.title') }}</h3>
      <Button variant="ghost" size="sm" @click="clearAllFilters">{{
        t('search.filters.clearAll')
      }}</Button>
    </div>

    <div class="filter-option">
      <div class="filter-checkbox">
        <Checkbox id="price-filter" v-model="filterByPrice" />
        <label for="price-filter">{{ t('search.filters.priceRange') }}</label>
      </div>
      <div v-if="filterByPrice" class="filter-content">
        <div class="slider-container">
          <div class="price-values">
            <span>{{ t('search.filters.currency') }} {{ priceValue[0] }}</span>
            <span>{{ t('search.filters.currency') }} {{ priceValue[1] }}</span>
          </div>
          <SliderReusable
            v-model="priceValue"
            :min="0"
            :max="100000"
            :step="100"
            class="price-slider"
          />
        </div>
      </div>
    </div>

    <Collapsible :default-open="true">
      <CollapsibleTrigger as-child>
        <Button variant="ghost" class="category-collapse">
          <span>{{ t('search.filters.condition') }}</span>
          <ChevronRight class="category-collapse-icon" />
        </Button>
      </CollapsibleTrigger>
      <CollapsibleContent class="filter-container">
        <Select v-model="localFilters.condition" :placeholder="t('search.filters.selectCondition')">
          <SelectTrigger>
            <SelectValue :placeholder="localFilters.condition" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem
              v-for="option in conditionOptions"
              :key="option.value"
              :value="option.value"
            >
              {{ option.label }}
            </SelectItem>
          </SelectContent>
        </Select>
      </CollapsibleContent>
    </Collapsible>

    <Collapsible :default-open="true">
      <CollapsibleTrigger as-child>
        <Button variant="ghost" class="category-collapse">
          <span>{{ t('search.filters.category') }}</span>
          <ChevronRight class="category-collapse-icon" />
        </Button>
      </CollapsibleTrigger>
      <CollapsibleContent class="filter-container">
        <Select
          v-model="localFilters.categoryName"
          :placeholder="t('search.filters.selectCategory')"
        >
          <SelectTrigger>
            <SelectValue :placeholder="localFilters.categoryName" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem v-for="option in categories" :key="option.name" :value="option.name">
              {{ option.name }}
            </SelectItem>
          </SelectContent>
        </Select>
      </CollapsibleContent>
    </Collapsible>

    <Collapsible :default-open="true">
      <CollapsibleTrigger as-child>
        <Button variant="ghost" class="category-collapse">
          <span>{{ t('search.filters.manufacturer') }}</span>
          <ChevronRight class="category-collapse-icon" />
        </Button>
      </CollapsibleTrigger>
      <CollapsibleContent class="filter-container">
        <div class="manufacturer-filter">
          <Input
            v-model="localFilters.manufacturer"
            :placeholder="t('search.filters.searchManufacturer')"
          />
        </div>
      </CollapsibleContent>
    </Collapsible>

    <Collapsible :default-open="true">
      <CollapsibleTrigger as-child>
        <Button variant="ghost" class="category-collapse">
          <span>{{ t('search.filters.location') }}</span>
          <ChevronRight class="category-collapse-icon" />
        </Button>
      </CollapsibleTrigger>
      <CollapsibleContent class="filter-container">
        <div class="map-container">
          <MapboxMap
            style="height: 400px"
            :access-token="MAPBOX_API_TOKEN"
            map-style="mapbox://styles/mapbox/streets-v12"
            @mb-created="mapCreated"
            @mb-click="mapClicked"
          >
            <MapboxMarker
              v-if="currentLocation"
              :lng-lat="[currentLocation.lon, currentLocation.lat]"
            />
            <MapboxNavigationControl position="top-right" />
          </MapboxMap>
          <div v-if="currentLocation" class="marker-controls">
            <div class="marker-controls-title">
              <span class="text-xs">{{ t('search.filters.locationTitle') }}</span>
              <Button variant="destructive" @click="clearMapMarker">
                <TrashIcon />
              </Button>
            </div>
            <div class="">
              <span>{{ t('search.filters.radiusDisplay', {distance: locationRangeKm[0]}) }}</span>
              <SliderRoot v-model="locationRangeKm" :min="1" :max="50">
                <SliderTrack>
                  <SliderRange />
                </SliderTrack>
                <SliderThumb />
              </SliderRoot>
            </div>
          </div>
        </div>
      </CollapsibleContent>
    </Collapsible>

    <div class="filter-option">
      <div class="filter-checkbox">
        <Checkbox id="year-filter" v-model="filterByYear" />
        <label for="year-filter">{{ t('search.filters.yearRange') }}</label>
      </div>
      <div v-if="filterByYear" class="filter-content">
        <div class="slider-container">
          <div class="year-values">
            <span>{{ yearValue[0] }}</span>
            <span>{{ yearValue[1] }}</span>
          </div>
          <SliderReusable
            v-model="yearValue"
            :min="1950"
            :max="currentYear"
            :step="1"
            class="year-slider"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.filters-sidebar {
  width: 300px;
  flex-shrink: 0;
  border-right: 1px solid var(--border);
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filters-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.filters-header h3 {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

.filter-option {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  border-bottom: 1px solid var(--border-muted, #f1f1f1);
  padding-bottom: 0.5rem;
}

.filter-checkbox {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0;
}

.filter-checkbox label {
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
}

.filter-content {
  padding: 0 0.5rem 0.5rem;
}

.category-collapse {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding-left: 0;
  padding-right: 0;
  font-weight: 500;
}

.category-collapse-icon {
  transition: 0.1s ease;
}

.category-collapse[data-state='open'] > .category-collapse-icon {
  transform: rotate(90deg);
}

.filter-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 0.75rem 0;
  font-size: var(--font-size-sm);
}

.slider-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 0.5rem 0;
}

.price-values,
.year-values {
  display: flex;
  justify-content: space-between;
  font-size: 0.875rem;
  color: var(--muted-foreground);
}

.manufacturer-filter {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.map-container {
  display: flex;
  flex-direction: column;
}

.marker-controls {
  margin: var(--spacing);
  display: flex;
  flex-direction: column;
}

.marker-controls-title {
  display: flex;
  justify-content: space-between;
  align-items: center;

  button {
    aspect-ratio: 1/1 !important;
    height: calc(var(--spacing) * 6) !important;
    width: calc(var(--sapcing) * 6) !important;
    padding: 0 !important;

    svg {
      width: calc(var(--spacing) * 3) !important;
      height: calc(var(--spacing) * 3) !important;
    }
  }
}

@media (max-width: 850px) {
  .filters-sidebar {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid var(--border);
  }
}
</style>

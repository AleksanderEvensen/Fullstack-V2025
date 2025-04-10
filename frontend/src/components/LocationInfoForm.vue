<script setup lang="ts">
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { computed, ref, watch, onMounted } from 'vue'
import { useTypedI18n } from '@/i18n'
import { MapboxMap, MapboxMarker, MapboxNavigationControl } from '@studiometa/vue-mapbox-gl'
import { MAPBOX_API_TOKEN } from '@/lib/utils'
import { MapPinIcon, SearchIcon, TrashIcon } from 'lucide-vue-next'
import { searchGeocodeAdvanced } from '@/lib/api/geocoding'
import { watchDebounced } from '@vueuse/core'
import type { Map } from 'mapbox-gl'

const props = defineProps<{
  initialValues?: {
    streetName?: string;
    streetNumber?: string;
    city?: string;
    postalCode?: string;
    country?: string;
    latitude?: number;
    longitude?: number;
  }
}>()

const { t } = useTypedI18n()

const street = ref(props.initialValues?.streetName || '')
const city = ref(props.initialValues?.city || '')
const postalCode = ref(props.initialValues?.postalCode || '')
const streetNumber = ref(props.initialValues?.streetNumber || '')
const country = ref(props.initialValues?.country || 'Norway')

const mapRef = ref<Map | null>(null)
const currentLocation = ref<{ lat: number; lon: number } | null>(
  props.initialValues?.latitude && props.initialValues?.longitude
    ? { lat: props.initialValues.latitude, lon: props.initialValues.longitude }
    : null
)
const searchInProgress = ref(false)

const locationData = computed(() => ({
  street: street.value,
  postalcode: postalCode.value,
  city: city.value,
}))

// Update hidden form fields with the coordinates
watch(currentLocation, (newLocation) => {
  if (newLocation) {
    // Update latitude and longitude in parent form
    latitudeField.value?.setValue(newLocation.lat)
    longitudeField.value?.setValue(newLocation.lon)
  } else {
    latitudeField.value?.setValue(undefined)
    longitudeField.value?.setValue(undefined)
  }
})

// Watch for incoming initialValues changes
watch(() => props.initialValues, (newValues) => {
  if (newValues) {
    street.value = newValues.streetName || ''
    streetNumber.value = newValues.streetNumber || ''
    city.value = newValues.city || ''
    postalCode.value = newValues.postalCode || ''
    country.value = newValues.country || 'Norway'
    
    if (newValues.latitude && newValues.longitude) {
      currentLocation.value = {
        lat: newValues.latitude,
        lon: newValues.longitude
      }
    }
  }
}, { deep: true })

// Reference to form fields for updating
const latitudeField = ref()
const longitudeField = ref()

// Flyto address when map is created or address fields change
async function flyToAddress() {
  searchInProgress.value = true
  
  try {
    if (!locationData.value.street || !locationData.value.city) {
      return
    }

    if (!mapRef.value) return
    
    const [location] = await searchGeocodeAdvanced({
      ...locationData.value,
      country: country.value
    }) ?? []
    
    if (location) {
      currentLocation.value = {
        lat: +location.lat,
        lon: +location.lon,
      }
      
      mapRef.value.flyTo({
        center: [+location.lon, +location.lat],
        zoom: 14,
      })
    }
  } catch (error) {
    console.error('Error with geocoding:', error)
  } finally {
    searchInProgress.value = false
  }
}

// Watch for changes in address fields with debounce
watchDebounced(locationData, async () => {
  if (!locationData.value.street || !locationData.value.city) return
  flyToAddress()
}, { debounce: 1000 })

// Setup map when created
function mapCreated(map: Map) {
  mapRef.value = map
  map.on('click', handleMapClick)
  
  // If we have initial coordinates, center the map on them
  if (currentLocation.value && map) {
    map.once('load', () => {
      map.flyTo({
        center: [currentLocation.value!.lon, currentLocation.value!.lat],
        zoom: 14,
      })
    })
  }
}

// Let users set marker by clicking on map
function handleMapClick(event: { lngLat: { lat: number; lng: number } }) {
  if (!mapRef.value) return
  
  currentLocation.value = {
    lat: event.lngLat.lat,
    lon: event.lngLat.lng,
  }
}

// Clear the marker and location data
function clearLocation() {
  currentLocation.value = null
  latitudeField.value?.setValue(undefined)
  longitudeField.value?.setValue(undefined)
}

// Handle manual search button click
function handleSearch() {
  flyToAddress()
}

// Initialize map with initial coordinates
onMounted(() => {
  if (props.initialValues?.latitude && props.initialValues?.longitude && mapRef.value) {
    mapRef.value.flyTo({
      center: [props.initialValues.longitude, props.initialValues.latitude],
      zoom: 14,
    })
  }
})
</script>

<template>
  <div class="location-form">
    <!-- Address fields -->
    <div class="location-inputs">
      <FormField v-slot="{ componentField }" name="streetName">
        <FormItem>
          <FormLabel>{{ t('createListing.form.location.street') }}</FormLabel>
          <FormControl>
            <div class="input-with-icon">
              <MapPinIcon class="input-icon" />
              <Input v-bind="componentField" v-model="street" placeholder="Tollbugata" />
            </div>
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <FormField v-slot="{ componentField }" name="streetNumber">
        <FormItem>
          <FormLabel>{{ t('createListing.form.location.streetNumber') }}</FormLabel>
          <FormControl>
            <Input v-bind="componentField" v-model="streetNumber" placeholder="2" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <div class="city-postal-row">
        <FormField v-slot="{ componentField }" name="city">
          <FormItem class="city-field">
            <FormLabel>{{ t('createListing.form.location.city') }}</FormLabel>
            <FormControl>
              <Input v-bind="componentField" v-model="city" placeholder="Oslo" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="postalCode">
          <FormItem class="postal-field">
            <FormLabel>{{ t('createListing.form.location.postal') }}</FormLabel>
            <FormControl>
              <Input v-bind="componentField" v-model="postalCode" placeholder="0152" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>

      <FormField v-slot="{ componentField }" name="country">
        <FormItem>
          <FormLabel>{{ t('createListing.form.location.country') }}</FormLabel>
          <FormControl>
            <Input v-bind="componentField" v-model="country" disabled />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>

      <div class="search-button-container">
        <Button 
          type="button" 
          @click="handleSearch" 
          :disabled="searchInProgress || !street || !city"
          variant="outline"
        >
          <SearchIcon class="button-icon" />
          {{ t('createListing.form.location.search') }}
        </Button>
      </div>
    </div>

    <!-- Map display -->
    <div class="map-container">
      <MapboxMap
        style="height: 350px"
        class="map-display"
        :access-token="MAPBOX_API_TOKEN"
        map-style="mapbox://styles/mapbox/streets-v12"
        :center="[9.139, 60.687]"
        :zoom="5.0"
        @mb-created="mapCreated"
      >
        <MapboxMarker
          v-if="currentLocation"
          :lng-lat="[currentLocation.lon, currentLocation.lat]"
        />
        <MapboxNavigationControl position="top-right" />
      </MapboxMap>

      <div v-if="currentLocation" class="location-found">
        <div class="location-actions">
          <p class="location-message">{{ t('createListing.form.location.locationSet') }}</p>
          <Button 
            variant="destructive" 
            size="sm"
            @click="clearLocation" 
            type="button"
          >
            <TrashIcon class="button-icon" />
            {{ t('createListing.form.location.clear') }}
          </Button>
        </div>
        
        <!-- Hidden fields for latitude/longitude -->
        <FormField ref="latitudeField" v-slot="{ componentField }" name="latitude">
          <input type="hidden" v-bind="componentField" :value="currentLocation?.lat" />
        </FormField>

        <FormField ref="longitudeField" v-slot="{ componentField }" name="longitude">
          <input type="hidden" v-bind="componentField" :value="currentLocation?.lon" />
        </FormField>
      </div>
      
      <p v-else class="location-instruction">
        {{ t('createListing.form.location.instructions') }}
      </p>
    </div>
  </div>
</template>

<style scoped>
.location-form {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.location-inputs {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 3);
}

.input-with-icon {
  position: relative;
  width: 100%;
}

.input-icon {
  position: absolute;
  left: calc(var(--spacing) * 2);
  top: 50%;
  transform: translateY(-50%);
  color: var(--muted-foreground);
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

.input-with-icon input {
  padding-left: calc(var(--spacing) * 8);
  width: 100%;
}

.city-postal-row {
  display: flex;
  gap: calc(var(--spacing) * 3);
}

.city-field {
  flex: 2;
}

.postal-field {
  flex: 1;
}

.search-button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: calc(var(--spacing) * 2);
}

.button-icon {
  margin-right: calc(var(--spacing) * 2);
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

.map-container {
  border-radius: var(--radius);
  overflow: hidden;
  margin-top: calc(var(--spacing) * 2);
}

.map-display {
  border-radius: var(--radius);
  border: 1px solid var(--border);
}

.location-found {
  margin-top: calc(var(--spacing) * 2);
  padding: calc(var(--spacing) * 2);
  background-color: var(--accent);
  border-radius: var(--radius);
}

.location-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.location-message {
  color: var(--foreground);
  font-weight: var(--font-weight-medium);
  margin: 0;
}

.location-instruction {
  margin-top: calc(var(--spacing) * 2);
  padding: calc(var(--spacing) * 2);
  background-color: var(--muted);
  border-radius: var(--radius);
  color: var(--muted-foreground);
  text-align: center;
}

@media (max-width: 640px) {
  .city-postal-row {
    flex-direction: column;
    gap: calc(var(--spacing) * 3);
  }
}
</style>
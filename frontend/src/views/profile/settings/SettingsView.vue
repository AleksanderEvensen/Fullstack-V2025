<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useTypedI18n } from '@/i18n'
import { formatNameInitials, MAPBOX_API_TOKEN } from '@/lib/utils'
import { ArrowLeftIcon, Building2Icon, HouseIcon, MapPinIcon } from 'lucide-vue-next'
import { computed, ref, watch } from 'vue'
import { MapboxMap, MapboxMarker, MapboxNavigationControl } from '@studiometa/vue-mapbox-gl'
import { watchDebounced } from '@vueuse/core'
import { searchGeocodeAdvanced } from '@/lib/api/geocoding'
import { Map } from "mapbox-gl";
import { RouterLink } from 'vue-router'

const { t } = useTypedI18n()

const userInfo = ref({
  name: 'Ola Nordmann',
  avatar: 'https://avatar.iran.liara.run/public/33.jpg',
  phone: '+47 123 45 678',
  email: 'ola.nordmann@norge.no',
  address: {
    street: 'Tollbugata 2',
    city: 'Oslo',
    postal: '0152',
  },
});

const mapRef = ref<Map>()
const street = ref(userInfo.value.address.street);
const city = ref(userInfo.value.address.city);
const postalCode = ref(userInfo.value.address.postal);

const locationData = computed(() => ({
  street: street.value,
  postalcode: postalCode.value,
  city: city.value,
}));


const currentLocation = ref<{lat: number, lon: number} | null>(null);


async function flyToAddress(params: Parameters<typeof searchGeocodeAdvanced>[0]) {
  if (!mapRef.value) return;
  const [location] = await searchGeocodeAdvanced(params) ?? [];
  if (location) {
    currentLocation.value = {
      lat: +location.lat,
      lon: +location.lon,
    }
    mapRef.value.flyTo({
      center: [+location.lon, +location.lat],
      zoom: 14,
    });
  }
}

watchDebounced(locationData, async () => {
  if (!userInfo.value.address.street || !userInfo.value.address.city || !userInfo.value.address.postal) return;
  flyToAddress({
   ...locationData.value,
   country: "Norway", 
  });
}, {
  debounce: 1000,
});

watch(mapRef, () => {
  if (!mapRef.value) return;
  mapRef.value.once('load', () => {
    flyToAddress({
      ...locationData.value,
      country: "Norway",
    });
  })
})

const avatarImageSrc = ref(userInfo.value.avatar)
const profilePicture = ref<File | undefined>(undefined)
const profilePictureObjectURL = ref<string | undefined>(undefined)

watch(profilePicture, (newFile, oldFile) => {
  if (oldFile != null && profilePictureObjectURL.value) {
    URL.revokeObjectURL(profilePictureObjectURL.value)
  }
  profilePictureObjectURL.value = newFile ? URL.createObjectURL(newFile) : undefined
})

const profilePic = computed(() => {
  if (profilePictureObjectURL.value) return profilePictureObjectURL.value
  if (avatarImageSrc.value) return avatarImageSrc.value
  return undefined
})

function deleteProfilePicture() {
  profilePicture.value = undefined
  avatarImageSrc.value = ''
}

function changeProfilePicture(event: Event) {
  const target = event.target as HTMLInputElement
  profilePicture.value = target.files?.[0] ?? undefined
}
</script>

<template>
  <div class="container">
    <RouterLink to="/profile" class="profile-goback">
      <Button variant="outline">
        <ArrowLeftIcon />
        {{ t('common.goback') }}
      </Button>
    </RouterLink>
    <h1 class="settings-header">{{ t('profile.settings.greeting', { name: userInfo.name }) }}</h1>
    <p class="settings-description">{{ t('profile.settings.description') }}</p>
    <div class="profile-container">
      <Avatar class="profile-avatar">
        <AvatarImage :src="profilePic ?? ''" />
        <AvatarFallback class="profile-fallback">{{
          formatNameInitials(userInfo.name)
        }}</AvatarFallback>
      </Avatar>
      <div class="profile-info">
        <h2 class="text-xl setting-label">{{ t('profile.settings.profile-picture') }}</h2>
        <input
          @change="changeProfilePicture"
          style="display: none"
          id="profile-pic-input"
          accept=".jpg, .jpeg, .png"
          type="file"
        />
        <Button variant="outline" as="label" for="profile-pic-input">
          {{ t('profile.settings.changePicture') }}
        </Button>
        <Button v-if="profilePic" variant="destructive" @click="deleteProfilePicture">
          {{ t('profile.settings.deletePicture') }}
        </Button>
      </div>
    </div>
    <div class="split-container">
      <div class="settings-container">
        <label class="setting-label">
          {{ t('profile.settings.name') }}
          <Input type="text" :default-value="userInfo.name" autocomplete="name" />
        </label>

        <label class="setting-label">
          {{ t('profile.settings.email') }}
          <Input type="text" :default-value="userInfo.email" autocomplete="email" />
        </label>

        <label class="setting-label">
          {{ t('profile.settings.phone') }}
          <Input type="text" :default-value="userInfo.phone" autocomplete="tel" />
        </label>

        <label class="setting-label">
          {{ t('profile.settings.old-password') }}
          <Input type="text" />
        </label>
        <label class="setting-label">
          {{ t('profile.settings.new-password') }}
          <Input type="text" />
        </label>
        <label class="setting-label">
          {{ t('profile.settings.confirm-password') }}
          <Input type="text" />
        </label>
      </div>
      <div class="address-container">
        <label for="street" class="setting-label">
          {{ t('profile.settings.address.street') }}
          <div class="input-with-icon">
            <MapPinIcon />
            <Input
              id="street"
              type="text"
              placeholder="Tollbugate 1"
              v-model="street"
            />
          </div>
        </label>

        <div class="input-split">
          <label for="city" class="setting-label">
            {{ t('profile.settings.address.city') }}
            <div class="input-with-icon">
              <Building2Icon />
              <Input
                id="city"
                type="text"
                placeholder="Oslo"
                v-model="city"
              />
            </div>
          </label>
          <label for="postal" class="setting-label">
            {{ t('profile.settings.address.city') }}
            <Input
              id="postal"
              class="postal-input"
              type="text"
              placeholder="0 1 5 2"
              v-model="postalCode"
            />
          </label>
        </div>
        <MapboxMap
          style="height: 400px;"
          :access-token="MAPBOX_API_TOKEN"
          map-style="mapbox://styles/mapbox/streets-v12"
          :center="[9.139, 60.687]"
          :zoom="5.0"
          @mb-created="(map: Map) => mapRef = map"
        >
          <MapboxMarker v-if="currentLocation" :lng-lat="[currentLocation.lon, currentLocation.lat]">
            <div class="map-marker">
              <HouseIcon />
            </div>
          </MapboxMarker>
          <MapboxNavigationControl position="top-right" />
        </MapboxMap>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-goback {
  display: block;
  margin-top: calc(var(--spacing) * 4);
  .button {
    display: flex;
    gap: calc(var(--spacing) * 2);
  }
}
.settings-header {
  margin-top: calc(var(--spacing) * 2);
  font-size: var(--text-4xl);
  font-weight: var(--font-weight-semibold);
}

.settings-description {
  color: var(--muted-foreground);
}

.profile-container {
  margin-top: calc(var(--spacing) * 10);
  display: flex;
  gap: calc(var(--spacing) * 5);
  align-items: center;
}

.profile-avatar {
  width: calc(var(--spacing) * 30);
  height: calc(var(--spacing) * 30);

  .profile-fallback {
    font-size: var(--text-5xl);
  }
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing);
  align-self: flex-start;
}

.setting-label {
  font-weight: var(--font-weight-semibold);
  width: fit-content;
  input {
    font-weight: var(--font-weight-normal);
  }
}

.split-container {
  margin-top: calc(var(--spacing) * 8);
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 5);
}

.settings-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.address-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.input-with-icon {
  position: relative;

  svg {
    position: absolute;
    top: 50%;
    left: var(--spacing);
    transform: translateY(-50%);
    color: var(--muted-foreground);
    height: 60%;
  }

  input {
    padding-left: calc(var(--spacing) * 8);
  }
}

.input-split {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.postal-input {
  text-align: center;
  font-family: var(--font-mono);
  letter-spacing: var(--spacing);
}

.map-marker {
  display: grid;
  place-items: center;
  border-radius: 9999px;
  background-color: var(--primary);
  color: var(--primary-foreground);
  padding: var(--spacing);
  width: calc(var(--spacing) * 6);
  height: calc(var(--spacing) * 6);

  svg {
    width: calc(var(--spacing) * 4);
    height: calc(var(--spacing) * 4);
  }
}

@media (min-width: 900px) {
  .split-container {
    flex-direction: row;
    gap: calc(var(--spacing) * 50);
  }
}
@media (min-width: 500px) {
  .input-split {
    flex-direction: row;
    gap: calc(var(--spacing) * 4);
  }
}
</style>

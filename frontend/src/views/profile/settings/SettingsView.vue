<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useTypedI18n } from '@/i18n'
import { formatNameInitials, formatPictureUrl, MAPBOX_API_TOKEN } from '@/lib/utils'
import { ArrowLeftIcon, Building2Icon, HouseIcon, MapPinIcon } from 'lucide-vue-next'
import { computed, ref, watch, onMounted } from 'vue'
import { MapboxMap, MapboxMarker, MapboxNavigationControl } from '@studiometa/vue-mapbox-gl'
import { watchDebounced } from '@vueuse/core'
import { searchGeocodeAdvanced } from '@/lib/api/geocoding'
import { Map } from 'mapbox-gl'
import { RouterLink } from 'vue-router'
import { Card, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import CardContent from '@/components/ui/card/CardContent.vue'
import CardHeader from '@/components/ui/card/CardHeader.vue'
import { useAuthStore } from '@/stores/auth'
import {
  useMutateUserProfilePicture,
  useRemoveUserProfilePicture,
  useUpdateUserPassword,
  useUpdateUserAddress,
} from '@/lib/api/queries/user'
import { toast } from 'vue-sonner'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'

const { t } = useTypedI18n()
const userStore = useAuthStore()
const { mutate: mutateUserProfilePicture } = useMutateUserProfilePicture()
const { mutate: mutateRemoveUserProfilePicture } = useRemoveUserProfilePicture()
const { mutate: updatePassword } = useUpdateUserPassword()
const { mutate: updateAddress } = useUpdateUserAddress()

const mapRef = ref<Map>()
const street = ref(userStore.user?.address?.streetName)
const city = ref(userStore.user?.address?.city)
const postalCode = ref(userStore.user?.address?.postalCode)
const streetNumber = ref(userStore.user?.address?.streetNumber || '')
const country = ref(userStore.user?.address?.country || 'Norway')

const locationData = computed(() => ({
  street: street.value,
  postalcode: postalCode.value,
  city: city.value,
}))

const currentLocation = ref<{ lat: number; lon: number } | null>(null)

// Form schemas
const passwordSchema = z
  .object({
    currentPassword: z.string().optional(),
    newPassword: z.string().min(8, t('profile.settings.password-min-length')),
    confirmPassword: z.string(),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: t('profile.settings.passwords-not-match'),
    path: ['confirmPassword'],
  })

const addressSchema = z.object({
  streetName: z.string().min(1, t('profile.settings.address-required')),
  streetNumber: z.string().min(1, t('profile.settings.address-required')),
  city: z.string().min(1, t('profile.settings.address-required')),
  postalCode: z.string().min(1, t('profile.settings.address-required')),
  country: z.string().min(1, t('profile.settings.address-required')),
})

const passwordFormRef = ref<InstanceType<typeof Form> | null>(null)
const addressFormRef = ref<InstanceType<typeof Form> | null>(null)

async function flyToAddress(params: Parameters<typeof searchGeocodeAdvanced>[0]) {
  if (!mapRef.value) return
  const [location] = (await searchGeocodeAdvanced(params)) ?? []
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
}

watchDebounced(
  locationData,
  async () => {
    if (!locationData.value.street || !locationData.value.city || !locationData.value.postalcode)
      return
    flyToAddress({
      ...locationData.value,
      country: 'Norway',
    })
  },
  {
    debounce: 1000,
  },
)

watch(mapRef, () => {
  if (!mapRef.value) return
  mapRef.value.once('load', () => {
    flyToAddress({
      ...locationData.value,
      country: 'Norway',
    })
  })
})

const userProfileImageUrl = computed(() => formatPictureUrl(userStore.user?.profileImageUrl ?? ''))
const profilePicture = ref<File | undefined>(undefined)

async function deleteProfilePicture() {
  await mutateRemoveUserProfilePicture(undefined, {
    onSuccess: () => {
      userStore.fetchUser()
      toast.success(t('profile.settings.profile-picture-deleted'))
      profilePicture.value = undefined
    },
    onError: () => {
      toast.error(t('profile.settings.profile-picture-delete-failed'))
    },
  })
}

const user = computed(() => userStore.user)

function changeProfilePicture(event: Event) {
  const target = event.target as HTMLInputElement
  profilePicture.value = target.files?.[0] ?? undefined
  uploadProfilePicture()
}

async function uploadProfilePicture() {
  if (!profilePicture.value) return
  await mutateUserProfilePicture(profilePicture.value, {
    onSuccess: () => {
      userStore.fetchUser()
      toast.success(t('profile.settings.profile-picture-uploaded'))
    },
    onError: () => {
      toast.error(t('profile.settings.profile-picture-upload-failed'))
      profilePicture.value = undefined
    },
  })
}

async function handlePasswordUpdate(values: z.infer<typeof passwordSchema>) {
  await updatePassword(
    { currentPassword: values.currentPassword ?? '', newPassword: values.newPassword },
    {
      onSuccess: () => {
        toast.success(t('profile.settings.password-updated'))
        passwordFormRef.value?.resetForm()
      },
      onError: () => {
        toast.error(t('profile.settings.password-update-failed'))
      },
    },
  )
}

async function handleAddressUpdate(values: z.infer<typeof addressSchema>) {
  await updateAddress(values, {
    onSuccess: () => {
      toast.success(t('profile.settings.address-updated'))
      userStore.fetchUser()
    },
    onError: () => {
      toast.error(t('profile.settings.address-update-failed'))
    },
  })
}

// Add watchers for form values
watch([street, streetNumber, city, postalCode, country], () => {
  if (addressFormRef.value) {
    addressFormRef.value.setValues({
      streetName: street.value,
      streetNumber: streetNumber.value,
      city: city.value,
      postalCode: postalCode.value,
      country: country.value,
    })
  }
})

// Initialize form values
onMounted(() => {
  if (addressFormRef.value) {
    addressFormRef.value.setValues({
      streetName: street.value,
      streetNumber: streetNumber.value,
      city: city.value,
      postalCode: postalCode.value,
      country: country.value,
    })
  }
})
</script>

<template>
  <div class="container">
    <RouterLink to="/profile" class="profile-goback">
      <Button variant="outline">
        <ArrowLeftIcon />
        {{ t('common.goback') }}
      </Button>
    </RouterLink>

    <div class="settings-header">
      <h1>{{ t('profile.settings.greeting', { name: user?.name }) }}</h1>
      <p class="settings-description">{{ t('profile.settings.description') }}</p>
    </div>

    <div class="profile-container">
      <Avatar class="profile-avatar">
        <AvatarImage :src="userProfileImageUrl" />
        <AvatarFallback class="profile-fallback">{{
          formatNameInitials(user?.name ?? '')
        }}</AvatarFallback>
      </Avatar>
      <div class="profile-info">
        <div class="profile-actions">
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
          <Button v-if="user?.profileImageUrl" variant="destructive" @click="deleteProfilePicture">
            {{ t('profile.settings.deletePicture') }}
          </Button>
        </div>
      </div>
    </div>

    <div class="settings-grid">
      <Card class="settings-card">
        <CardHeader>
          <CardTitle>{{ t('profile.settings.personal-information') }}</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="settings-container">
            <div class="setting-group">
              <Label class="setting-label">{{ t('profile.settings.name') }}</Label>
              <Input type="text" :default-value="user?.name" autocomplete="name" disabled />
            </div>

            <div class="setting-group">
              <Label class="setting-label">{{ t('profile.settings.phone') }}</Label>
              <Input type="text" :default-value="user?.phoneNumber" autocomplete="tel" disabled />
            </div>

            <Form
              v-slot="{ meta, values, validate }"
              as=""
              :validation-schema="toTypedSchema(passwordSchema)"
              ref="passwordFormRef"
            >
              <form
                @submit.prevent="
                  validate().then(() =>
                    handlePasswordUpdate(values as z.infer<typeof passwordSchema>),
                  )
                "
              >
                <FormField v-slot="{ componentField }" name="currentPassword">
                  <FormItem>
                    <FormLabel>{{ t('profile.settings.old-password') }}</FormLabel>
                    <FormControl>
                      <Input type="password" v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <FormField v-slot="{ componentField }" name="newPassword">
                  <FormItem>
                    <FormLabel>{{ t('profile.settings.new-password') }}</FormLabel>
                    <FormControl>
                      <Input type="password" v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <FormField v-slot="{ componentField }" name="confirmPassword">
                  <FormItem>
                    <FormLabel>{{ t('profile.settings.confirm-password') }}</FormLabel>
                    <FormControl>
                      <Input type="password" v-bind="componentField" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <Button type="submit" class="save-button" :disabled="!meta.valid">
                  {{ t('profile.settings.save-password') }}
                </Button>
              </form>
            </Form>
          </div>
        </CardContent>
      </Card>

      <Card class="settings-card">
        <CardHeader>
          <CardTitle>{{ t('profile.settings.address.title') }}</CardTitle>
        </CardHeader>
        <CardContent>
          <Form
            v-slot="{ meta, values, validate }"
            as=""
            :validation-schema="toTypedSchema(addressSchema)"
            ref="addressFormRef"
          >
            <form
              @submit.prevent="
                validate().then(() => handleAddressUpdate(values as z.infer<typeof addressSchema>))
              "
            >
              <div class="address-container">
                <FormField v-slot="{ componentField }" name="streetName">
                  <FormItem>
                    <FormLabel>{{ t('profile.settings.address.street') }}</FormLabel>
                    <FormControl>
                      <div class="input-with-icon">
                        <MapPinIcon />
                        <Input v-bind="componentField" v-model="street" />
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <FormField v-slot="{ componentField }" name="streetNumber">
                  <FormItem>
                    <FormLabel>{{ t('profile.settings.address.streetNumber') }}</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" v-model="streetNumber" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <div class="input-split">
                  <FormField v-slot="{ componentField }" name="city">
                    <FormItem>
                      <FormLabel>{{ t('profile.settings.address.city') }}</FormLabel>
                      <FormControl>
                        <div class="input-with-icon">
                          <Building2Icon />
                          <Input v-bind="componentField" v-model="city" />
                        </div>
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="postalCode">
                    <FormItem>
                      <FormLabel>{{ t('profile.settings.address.postal') }}</FormLabel>
                      <FormControl>
                        <Input class="postal-input" v-bind="componentField" v-model="postalCode" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </div>

                <FormField v-slot="{ componentField }" name="country">
                  <FormItem>
                    <FormLabel>{{ t('profile.settings.address.country') }}</FormLabel>
                    <FormControl>
                      <Input v-bind="componentField" v-model="country" disabled />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <Button type="submit" class="save-button" :disabled="!meta.valid && !meta.dirty">
                  {{ t('profile.settings.address.save-address') }}
                </Button>

                <div class="map-container">
                  <MapboxMap
                    style="height: 400px"
                    :access-token="MAPBOX_API_TOKEN"
                    map-style="mapbox://styles/mapbox/streets-v12"
                    :center="[9.139, 60.687]"
                    :zoom="5.0"
                    @mb-created="(map: Map) => (mapRef = map)"
                  >
                    <MapboxMarker
                      v-if="currentLocation"
                      :lng-lat="[currentLocation.lon, currentLocation.lat]"
                    >
                      <div class="map-marker">
                        <HouseIcon />
                      </div>
                    </MapboxMarker>
                    <MapboxNavigationControl position="top-right" />
                  </MapboxMap>
                </div>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: calc(var(--spacing) * 4);
}

.profile-goback {
  display: block;
  margin-bottom: calc(var(--spacing) * 4);
}

.profile-goback .button {
  display: flex;
  gap: calc(var(--spacing) * 2);
  align-items: center;
}

.settings-header {
  margin-bottom: calc(var(--spacing) * 6);
}

.settings-header h1 {
  font-size: var(--text-4xl);
  font-weight: var(--font-weight-semibold);
  margin-bottom: calc(var(--spacing) * 2);
}

.settings-description {
  color: var(--muted-foreground);
  font-size: var(--text-sm);
}

.profile-container {
  display: flex;
  gap: calc(var(--spacing) * 5);
  align-items: center;
  margin-bottom: calc(var(--spacing) * 6);
}

.profile-avatar {
  width: calc(var(--spacing) * 30);
  height: calc(var(--spacing) * 30);
}

.profile-avatar .profile-fallback {
  font-size: var(--text-5xl);
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}

.profile-actions {
  display: flex;
  gap: calc(var(--spacing) * 2);
}

.settings-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: calc(var(--spacing) * 6);
}

.settings-card {
  height: fit-content;
}

.settings-container,
.address-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.setting-label {
  font-weight: var(--font-weight-medium);
  color: var(--foreground);
}

.input-with-icon {
  position: relative;
  width: 100%;
}

.input-with-icon svg {
  position: absolute;
  top: 50%;
  left: var(--spacing);
  transform: translateY(-50%);
  color: var(--muted-foreground);
  height: 60%;
}

.input-with-icon input {
  padding-left: calc(var(--spacing) * 8);
  width: 100%;
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

.map-container {
  margin-top: calc(var(--spacing) * 4);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transform: translateY(-50%);
}

.map-marker svg {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

:deep(.mapboxgl-ctrl-top-right) {
  top: 16px;
  right: 16px;
}

:deep(.mapboxgl-ctrl) {
  border-radius: var(--radius);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: var(--background);
  border: 1px solid var(--border);
}

:deep(.mapboxgl-ctrl button) {
  border-radius: var(--radius);
  background-color: var(--background);
  border: 1px solid var(--border);
}

:deep(.mapboxgl-ctrl button:hover) {
  background-color: var(--accent);
}

.form-item {
  margin-bottom: calc(var(--spacing) * 2);
}

.form-label {
  font-weight: var(--font-weight-medium);
  margin-bottom: calc(var(--spacing) * 1);
}

.form-message {
  color: var(--destructive);
  font-size: var(--text-sm);
  margin-top: calc(var(--spacing) * 1);
}

.save-button {
  margin-top: var(--spacing);
  margin-bottom: var(--spacing);
}

@media (min-width: 1024px) {
  .settings-grid {
    grid-template-columns: 1fr 1fr;
  }

  .input-split {
    flex-direction: row;
    gap: calc(var(--spacing) * 4);
  }
}

@media (max-width: 500px) {
  .profile-container {
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .setting-label {
    text-align: center;
  }
}
</style>

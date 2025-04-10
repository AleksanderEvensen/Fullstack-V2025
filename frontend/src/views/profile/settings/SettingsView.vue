<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useTypedI18n } from '@/i18n'
import { formatNameInitials, formatPictureUrl } from '@/lib/utils'
import { ArrowLeftIcon } from 'lucide-vue-next'
import { computed, ref, watch, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { Card, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import CardContent from '@/components/ui/card/CardContent.vue'
import CardHeader from '@/components/ui/card/CardHeader.vue'
import { useAuthStore } from '@/stores/auth'
import { useMutateUserProfilePicture, useRemoveUserProfilePicture, useUpdateUserPassword, useUpdateUserAddress } from '@/lib/api/queries/user'
import { toast } from 'vue-sonner'
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'

// Import the LocationInfoForm component
import LocationInfoForm from '@/components/LocationInfoForm.vue'

const { t } = useTypedI18n()
const userStore = useAuthStore()
const { mutate: mutateUserProfilePicture } = useMutateUserProfilePicture()
const { mutate: mutateRemoveUserProfilePicture } = useRemoveUserProfilePicture()
const { mutate: updatePassword } = useUpdateUserPassword()
const { mutate: updateAddress } = useUpdateUserAddress()

// Form schemas
const passwordSchema = z.object({
  currentPassword: z.string().optional(),
  newPassword: z.string()
    .min(8, t('profile.settings.password-min-length')),
  confirmPassword: z.string(),
}).refine((data) => data.newPassword === data.confirmPassword, {
  message: t('profile.settings.passwords-not-match'),
  path: ['confirmPassword'],
});

const addressSchema = z.object({
  streetName: z.string().min(1, t('profile.settings.address-required')),
  streetNumber: z.string(),
  city: z.string().min(1, t('profile.settings.address-required')),
  postalCode: z.string().min(1, t('profile.settings.address-required')),
  country: z.string().min(1, t('profile.settings.address-required')),
  latitude: z.number().optional(),
  longitude: z.number().optional(),
});

const passwordFormRef = ref<InstanceType<typeof Form> | null>(null)
const addressFormRef = ref<InstanceType<typeof Form> | null>(null)
const addressIsLoading = ref(false)

const userProfileImageUrl = computed(() => formatPictureUrl(userStore.user?.profileImageUrl ?? ''))
const profilePicture = ref<File | undefined>(undefined)
const user = computed(() => userStore.user)

// Initialize form values for address
const initialAddressValues = ref({
  streetName: user.value?.address?.streetName || '',
  city: user.value?.address?.city || '',
  postalCode: user.value?.address?.postalCode || '',
  country: user.value?.address?.country || 'Norway',
})

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
    }
  )
}

async function handleAddressUpdate(values: z.infer<typeof addressSchema>) {
  addressIsLoading.value = true
  
  try {
    await updateAddress(
      values,
      {
        onSuccess: () => {
          toast.success(t('profile.settings.address.address-updated'))
          userStore.fetchUser()
        },
        onError: () => {
          toast.error(t('profile.settings.address.address-update-failed'))
        },
      }
    )
  } finally {
    addressIsLoading.value = false
  }
}

// Initialize form values when user data changes
watch(() => userStore.user, (newUser) => {
  if (newUser && addressFormRef.value) {
    initialAddressValues.value = {
      streetName: newUser.address?.streetName || '',
      city: newUser.address?.city || '',
      postalCode: newUser.address?.postalCode || '',
      country: newUser.address?.country || 'Norway',
    }
    
    // Set form values
    addressFormRef.value.setValues(initialAddressValues.value)
  }
}, { immediate: true })

// Initialize form on mount
onMounted(() => {
  if (addressFormRef.value) {
    addressFormRef.value.setValues(initialAddressValues.value)
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
        <AvatarFallback class="profile-fallback">{{ formatNameInitials(user?.name ?? '') }}</AvatarFallback>
      </Avatar>
      <div class="profile-info">
        <Label class="setting-label">{{ t('profile.settings.profile-picture') }}</Label>
        <div class="profile-actions">
          <input @change="changeProfilePicture" style="display: none" id="profile-pic-input" accept=".jpg, .jpeg, .png"
            type="file" />
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

            <Form v-slot="{ meta, values, validate }" as="" :validation-schema="toTypedSchema(passwordSchema)"
              ref="passwordFormRef">
              <form
                @submit.prevent="validate().then(() => handlePasswordUpdate(values as z.infer<typeof passwordSchema>))">
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
          <Form v-slot="{ meta, values, validate }" as="" :validation-schema="toTypedSchema(addressSchema)"
            ref="addressFormRef" class="address-form">
            <form @submit.prevent="validate().then(() => handleAddressUpdate(values as z.infer<typeof addressSchema>))">
              <div class="address-container">
                <!-- Using the integrated LocationInfoForm component -->
                <LocationInfoForm :initial-values="initialAddressValues" />

                <!-- Save Address Button -->
                <div class="save-address-container">
                  <Button 
                    type="submit" 
                    class="save-address-button" 
                    :disabled="!meta.valid || addressIsLoading"
                  >
                    {{ addressIsLoading 
                      ? t('common.submitting') 
                      : t('profile.settings.address.save-address') 
                    }}
                  </Button>
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

.save-button,
.save-address-button {
  margin-top: calc(var(--spacing) * 2);
  margin-bottom: var(--spacing);
}

.save-address-container {
  display: flex;
  justify-content: flex-end;
  margin-top: calc(var(--spacing) * 4);
}

.save-address-button {
  min-width: 150px;
}

@media (min-width: 1024px) {
  .settings-grid {
    grid-template-columns: 1fr 1fr;
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
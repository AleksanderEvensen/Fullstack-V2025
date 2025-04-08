<script setup lang="ts">
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { formatNameInitials } from '@/lib/utils'
import { computed, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const userInfo = ref({
  name: 'Ola Nordmann',
  avatar: 'https://avatar.iran.liara.run/public/33.jpg',
  phone: '+47 123 45 678',
  email: 'ola.nordmann@norge.no',
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
  </div>
</template>

<style scoped>
.settings-header {
  margin-top: calc(var(--spacing) * 4);
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

.settings-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);

  input {
    max-width: 400px;
  }
}
</style>

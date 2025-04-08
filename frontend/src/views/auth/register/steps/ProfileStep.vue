<script setup lang="ts">
import { ref } from 'vue'
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Upload, User } from 'lucide-vue-next'
import Label from '@/components/ui/label/Label.vue'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n()

const previewImage = ref<string | null>(null)

const handleFileChange = (e: Event) => {
  const input = e.target as HTMLInputElement
  if (input.files?.[0]) {
    const reader = new FileReader()
    reader.onload = (e) => {
      previewImage.value = e.target?.result as string
    }
    reader.readAsDataURL(input.files[0])
  }
}
</script>

<template v-if="stepIndex === 1">
  <div class="profile-upload-container">
    <div class="avatar-container">
      <Avatar class="profile-avatar">
        <AvatarImage :src="previewImage || ''" />
        <AvatarFallback class="avatar-fallback">
          <User class="avatar-icon" />
        </AvatarFallback>
      </Avatar>
      <Label for="profile-picture" class="upload-button">
        <Upload class="upload-icon" />
      </Label>
      <input
        id="profile-picture"
        type="file"
        accept="image/*"
        class="hidden"
        @change="handleFileChange"
      />
    </div>
    <span class="upload-hint">{{ t('auth.uploadProfilePicture') }}</span>
  </div>

  <FormField v-slot="{ componentField }" name="fullName">
    <FormItem>
      <FormLabel class="form-label">{{ t('auth.fullName') }}</FormLabel>
      <FormControl>
        <div class="input-container">
          <User class="input-icon" />
          <Input
            type="text"
            v-bind="componentField"
            :placeholder="t('auth.fullNamePlaceholder')"
            class="input-with-icon"
          />
        </div>
      </FormControl>
      <FormMessage class="form-message" />
    </FormItem>
  </FormField>
</template>

<style scoped>
@import url("./shared.css");
.profile-upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.avatar-container {
  position: relative;
}

.profile-avatar {
  height: 6rem;
  width: 6rem;
  border: 4px solid hsl(var(--muted));
  background-color: hsl(var(--background));
}

.avatar-fallback {
  background-color: hsla(var(--primary), 0.1);
  color: hsl(var(--primary));
}

.avatar-icon {
  height: 3rem;
  width: 3rem;
}

.upload-button {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: var(--secondary);
  color: hsl(var(--secondary-foreground));
  border-radius: 9999px;
  padding: 0.5rem;
  cursor: pointer;
  box-shadow:
    0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
  transition: background-color 0.3s ease;
}

.upload-icon {
  height: 1rem;
  width: 1rem;
}

.upload-hint {
  font-size: 0.875rem;
  color: hsl(var(--muted-foreground));
}
</style>

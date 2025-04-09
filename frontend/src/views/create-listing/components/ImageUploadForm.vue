<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Button } from '@/components/ui/button'
import { X, Camera } from 'lucide-vue-next'
import { useTypedI18n } from '@/i18n'
import ImageDrop from '@/components/ui/image-drop/ImageDrop.vue'
import { useUploadImage } from '@/lib/api/queries/images'
import { toast } from 'vue-sonner'
const props = defineProps<{
  modelValue: string[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string[]): void
}>()

const { t } = useTypedI18n()
const { mutateAsync: uploadImage, isPending: uploadInProgress } = useUploadImage()
const uploadedImages = ref<{ filename: string, originalName: string }[]>([])
const processedFilesMap = ref<Map<string, boolean>>(new Map())

const handleImageError = (error: string) => {
  toast.error(error)
}

const removeImage = (filename: string) => {
  const index = uploadedImages.value.findIndex(img => img.filename === filename)
  if (index !== -1) {
    uploadedImages.value.splice(index, 1)
    emit('update:modelValue', uploadedImages.value.map(img => img.filename))
  }
}

const handleImagesUpdate = async (files: File[]) => {
  uploadInProgress.value = true

  const filesToProcess = files.filter(file => {
    const fileKey = `${file.name}-${file.size}-${file.lastModified}`
    return !processedFilesMap.value.has(fileKey)
  })

  for (const file of filesToProcess) {
    const fileKey = `${file.name}-${file.size}-${file.lastModified}`

    try {
      const generatedFilename = await uploadImage(file)

      if (generatedFilename) {
        uploadedImages.value.push({
          filename: generatedFilename,
          originalName: file.name
        })

        processedFilesMap.value.set(fileKey, true)
        emit('update:modelValue', uploadedImages.value.map(img => img.filename))
      }
    } catch (error) {
      console.error(`Failed to upload image ${file.name}:`, error)
    }
  }
}

const getImageUrl = (filename: string) => {
  return `${window.location.origin}/api/images/${filename}`
}

// Initialize with existing values if any
watch(() => props.modelValue, (newValue) => {
  if (newValue && newValue.length > 0) {
    uploadedImages.value = newValue.map(filename => ({
      filename,
      originalName: filename // We don't have the original name, so using filename
    }))
  }
}, { immediate: true })
</script>

<template>
  <div class="image-upload-section">
    <FormField name="images" v-slot="{ componentField }">
      <FormItem>
        <FormLabel>{{ t('createListing.form.productImages') }}</FormLabel>
        <FormControl>
          <ImageDrop @error="handleImageError" @update:modelValue="handleImagesUpdate" :max-files="5" :max-size="5" />
        </FormControl>
        <FormMessage />

        <input type="hidden" v-bind="componentField" :value="uploadedImages.map(img => img.filename)" />
      </FormItem>

      <!-- Upload Status -->
      <div v-if="uploadInProgress" class="upload-status">
        <div class="loader"></div>
        <span>{{ t('createListing.form.uploading') || 'Uploading...' }}</span>
      </div>

      <!-- Image previews -->
      <div v-if="uploadedImages.length > 0" class="images-container">
        <div class="image-preview-grid">
          <div v-for="image in uploadedImages" :key="image.filename" class="image-preview-item">
            <img :src="getImageUrl(image.filename)" :alt="image.originalName" />
            <div class="image-preview-actions">
              <Button variant="destructive" size="sm" @click.prevent="removeImage(image.filename)">
                <X />
              </Button>
            </div>
            <div class="image-preview-filename">{{ image.originalName }}</div>
          </div>
        </div>
      </div>

      <div v-else class="no-images-message">
        <p>{{ t('createListing.form.noImages') }}</p>
      </div>
    </FormField>
  </div>
</template>

<style scoped>
.image-upload-section {
  width: 100%;
}

.upload-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 1rem;
}

.loader {
  width: 1rem;
  height: 1rem;
  border: 2px solid var(--muted);
  border-top-color: var(--primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.images-container {
  margin-top: 1rem;
}

.image-preview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.image-preview-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: 0.5rem;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.image-preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-preview-actions {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
}

.image-preview-filename {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 0.5rem;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  font-size: 0.75rem;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.no-images-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 3rem;
  border: 2px dashed var(--border);
  border-radius: 0.5rem;
  background-color: var(--background);
  color: var(--muted-foreground);
  text-align: center;
}
</style>
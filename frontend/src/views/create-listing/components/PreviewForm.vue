<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Camera } from 'lucide-vue-next'
import { useTypedI18n } from '@/i18n'

const props = defineProps<{
  values: any
  categories: any[]
  uploadedImages: string[]
}>()

const { t } = useTypedI18n()

function formatPrice(price: number) {
  return price.toLocaleString('NO', {
    style: 'currency',
    currency: 'NOK',
  })
}

const computedConditionI18n = function (condition: string) {
  switch (condition) {
    case 'NEW':
      return t('createListing.form.conditionOptions.new')
    case 'LIKE_NEW':
      return t('createListing.form.conditionOptions.likeNew')
    case 'VERY_GOOD':
      return t('createListing.form.conditionOptions.veryGood')
    case 'GOOD':
      return t('createListing.form.conditionOptions.good')
    case 'ACCEPTABLE':
      return t('createListing.form.conditionOptions.acceptable')
    default:
      return t('createListing.form.conditionOptions.new')
  }
}

const getImageUrl = (filename: string) => {
  return `${window.location.origin}/api/images/${filename}`
}
</script>

<template>
  <div class="preview-container">
    <Card>
      <CardHeader>
        <CardTitle>{{ t('createListing.preview.basicInfo') }}</CardTitle>
        <CardDescription>{{ t('createListing.preview.basicInfoDescription') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <div class="preview-grid">
          <div class="preview-item">
            <span class="preview-label">{{ t('createListing.form.title') }}</span>
            <span class="preview-value">{{ values.title }}</span>
          </div>
          <div class="preview-item">
            <span class="preview-label">{{ t('createListing.form.category') }}</span>
            <span class="preview-value">{{categories?.find((c) => c.id === values.categoryId)?.name}}</span>
          </div>
          <div class="preview-item">
            <span class="preview-label">{{ t('createListing.form.condition') }}</span>
            <span class="preview-value">{{ computedConditionI18n(values.condition) }}</span>
          </div>
          <div class="preview-item">
            <span class="preview-label">{{ t('createListing.form.price') }}</span>
            <span class="preview-value">{{ formatPrice(values.price) }}</span>
          </div>
          <div class="preview-item" v-if="values.originalPrice">
            <span class="preview-label">{{ t('createListing.form.originalPrice') }}</span>
            <span class="preview-value">{{ formatPrice(values.originalPrice) }}</span>
          </div>
          <div class="preview-item full-width">
            <span class="preview-label">{{ t('createListing.form.description') }}</span>
            <span class="preview-value">{{ values.description }}</span>
          </div>
        </div>
      </CardContent>
    </Card>

    <Card>
      <CardHeader>
        <CardTitle>{{ t('createListing.preview.productDetails') }}</CardTitle>
        <CardDescription>{{ t('createListing.preview.productDetailsDescription') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <div class="preview-grid">
          <div class="preview-item" v-if="values.modelYear">
            <span class="preview-label">{{ t('createListing.form.modelYear') }}</span>
            <span class="preview-value">{{ values.modelYear }}</span>
          </div>
          <div class="preview-item" v-if="values.manufacturer">
            <span class="preview-label">{{ t('createListing.form.manufacturer') }}</span>
            <span class="preview-value">{{ values.manufacturer }}</span>
          </div>
          <div class="preview-item" v-if="values.model">
            <span class="preview-label">{{ t('createListing.form.model') }}</span>
            <span class="preview-value">{{ values.model }}</span>
          </div>
          <div class="preview-item" v-if="values.serialNumber">
            <span class="preview-label">{{ t('createListing.form.serialNumber') }}</span>
            <span class="preview-value">{{ values.serialNumber }}</span>
          </div>
          <div class="preview-item" v-if="values.purchaseDate">
            <span class="preview-label">{{ t('createListing.form.purchaseDate') }}</span>
            <span class="preview-value">{{ values.purchaseDate }}</span>
          </div>
          <div class="preview-item" v-if="values.usageDuration">
            <span class="preview-label">{{ t('createListing.form.usageDuration') }}</span>
            <span class="preview-value">{{ values.usageDuration }}</span>
          </div>
          <div class="preview-item full-width" v-if="values.reasonForSelling">
            <span class="preview-label">{{ t('createListing.form.reasonForSelling') }}</span>
            <span class="preview-value">{{ values.reasonForSelling }}</span>
          </div>
        </div>
      </CardContent>
    </Card>

    <Card>
      <CardHeader>
        <CardTitle>{{ t('createListing.preview.imagesTitle') }}</CardTitle>
        <CardDescription>{{ t('createListing.preview.imagesDescription') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <div class="preview-images" v-if="uploadedImages.length">
          <div class="image-grid">
            <div v-for="(filename, index) in uploadedImages" :key="filename" class="image-preview">
              <img :src="getImageUrl(filename)" :alt="`${t('createListing.preview.imageAlt')} ${index + 1}`" />
            </div>
          </div>
        </div>
        <div v-else class="no-images">
          <Camera class="no-images-icon" />
          <p>{{ t('createListing.form.noImages') }}</p>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.preview-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.preview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: calc(var(--spacing) * 3);
}

.preview-item {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 1);
}

.preview-item.full-width {
  grid-column: 1 / -1;
}

.preview-label {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

.preview-value {
  font-size: var(--font-size-base);
  color: var(--foreground);
  line-height: 1.5;
}

.preview-images {
  width: 100%;
  margin-top: 1rem;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1.5rem;
  padding: 1rem;
}

.image-preview {
  position: relative;
  aspect-ratio: 1;
  border-radius: 0.5rem;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  background-color: var(--background);
  border: 1px solid var(--border);
}

.image-preview:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.image-preview:hover img {
  transform: scale(1.05);
}

.no-images {
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

.no-images-icon {
  width: 2.5rem;
  height: 2.5rem;
  color: var(--muted-foreground);
  opacity: 0.7;
}

@media (max-width: 640px) {
  .preview-grid {
    grid-template-columns: 1fr;
  }

  .image-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 1rem;
    padding: 0.5rem;
  }
}
</style>
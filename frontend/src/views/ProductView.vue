<script setup lang="ts">
import { computed, ref } from 'vue'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { getListing } from '@/lib/api/queries/listings'
import { useRoute } from 'vue-router'
import { formatAddress } from '@/lib/utils'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n()
const id = useRoute().params.id as unknown as number
const { data: product } = getListing(id)
const currentImageIndex = ref(0)


const formatPrice = (price: number) => {
  return new Intl.NumberFormat('NO', {
    style: 'currency',
    currency: 'NOK',
  }).format(price)
}

const calculateDiscount = (original: number, current: number) => {
  return Math.round(((original - current) / original) * 100)
}

const getTranslatedCondition = (condition: string) => {
  switch (condition) {
    case 'New':
      return t('product.conditionLabels.new')
    case 'Like New':
      return t('product.conditionLabels.likeNew')
    case 'Very Good':
      return t('product.conditionLabels.veryGood')
    case 'Good':
      return t('product.conditionLabels.good')
    case 'Acceptable':
      return t('product.conditionLabels.acceptable')
    default:
      return condition
  }
}

const shouldDisplayOriginalPrice = computed(() => {
  return product.value?.originalPrice && product.value.originalPrice > product.value.price
})
</script>

<template>
  <div class="container" v-if="product">
    <div class="product-grid">
      <div class="product-images">
        <div class="main-image">
          <img :src="product.images[currentImageIndex]" :alt="product.title" />
        </div>
        <div class="image-thumbnails">
          <button v-for="(image, index) in product.images" :key="index" @click="currentImageIndex = index"
            class="thumbnail-button" :class="{ active: currentImageIndex === index }">
            <img :src="image" :alt="`${t('product.imageAlt')} ${index + 1}`" />
          </button>
        </div>
      </div>

      <!-- Product Info -->
      <div class="product-info">
        <Card>
          <CardHeader>
            <CardTitle>{{ product.title }}</CardTitle>
            <div class="condition-badge">
              <Badge variant="outline">{{ getTranslatedCondition(product.condition) }}</Badge>
            </div>
          </CardHeader>
          <CardContent>
            <!-- Price -->
            <div class="price-section">
              <div class="price-display">
                <span class="current-price">{{ formatPrice(product.price) }}</span>
                <span class="original-price" v-if="shouldDisplayOriginalPrice">{{
                  formatPrice(product.originalPrice!)
                }}</span>
                <span class="discount" v-if="shouldDisplayOriginalPrice && product.originalPrice">
                  {{ formatPrice(product.originalPrice - product.price) }}
                  {{ t('product.pricing.save') }}
                  {{ calculateDiscount(product.originalPrice, product.price) }}%
                </span>
              </div>
            </div>

            <!-- Actions -->
            <div class="action-buttons">
              <Button class="buy-button" size="lg">{{ t('product.buyNow') }}</Button>
              <Button variant="outline" class="message-button" size="lg">{{
                t('product.messageButton')
              }}</Button>
            </div>
          </CardContent>
        </Card>

        <!-- Seller Info -->
        <Card>
          <CardHeader>
            <CardTitle>{{ t('product.sellerInfo.title') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <div class="seller-info">
              <Avatar class="seller-avatar">
                <AvatarImage :src="product.seller.profileImageUrl ?? ''" :alt="product.seller.firstName" />
                <AvatarFallback>{{ product.seller.firstName[0] }}</AvatarFallback>
              </Avatar>
              <div class="seller-details">
                <h3>{{ product.seller.firstName }} {{ product.seller.lastName }}</h3>
                <div class="seller-info-text" v-if="product.seller.address">
                  {{ formatAddress(product.seller.address) }}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>{{ t('product.aboutThisItem') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <p>{{ product.description }}</p>
          </CardContent>
        </Card>
      </div>
    </div>

    <!-- Product Details Below -->
    <div class="product-details">
      <div class="details-grid">
        <!-- Basic Info -->
        <Card>
          <CardHeader>
            <CardTitle>{{ t('product.details.basicInfo') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <div class="details-list">
              <div class="detail-item" v-if="product.modelYear">
                <span class="detail-label">{{ t('product.labels.modelYear') }}</span>
                <span class="detail-value">{{ product.modelYear }}</span>
              </div>
              <div class="detail-item" v-if="product.manufacturer">
                <span class="detail-label">{{ t('product.labels.manufacturer') }}</span>
                <span class="detail-value">{{ product.manufacturer }}</span>
              </div>
              <div class="detail-item" v-if="product.model">
                <span class="detail-label">{{ t('product.labels.model') }}</span>
                <span class="detail-value">{{ product.model }}</span>
              </div>
              <div class="detail-item" v-if="product.serialNumber">
                <span class="detail-label">{{ t('product.labels.serialNumber') }}</span>
                <span class="detail-value">{{ product.serialNumber }}</span>
              </div>
            </div>
          </CardContent>
        </Card>

        <!-- Usage Info -->
        <Card>
          <CardHeader>
            <CardTitle>{{ t('product.details.usageInfo') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <div class="details-list">
              <div class="detail-item" v-if="product.purchaseDate">
                <span class="detail-label">{{ t('product.labels.purchaseDate') }}</span>
                <span class="detail-value">{{ product.purchaseDate }}</span>
              </div>
              <div class="detail-item" v-if="product.usageDuration">
                <span class="detail-label">{{ t('product.labels.usageDuration') }}</span>
                <span class="detail-value">{{ product.usageDuration }}</span>
              </div>
            </div>
          </CardContent>
        </Card>

        <!-- Defects -->
        <Card v-if="product.defects.length">
          <CardHeader>
            <CardTitle>{{ t('product.details.knownDefects') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <ul class="details-list">
              <li v-for="(defect, index) in product.defects" :key="index" class="detail-list-item">
                {{ defect }}
              </li>
            </ul>
          </CardContent>
        </Card>

        <!-- Modifications -->
        <Card v-if="product.modifications?.length">
          <CardHeader>
            <CardTitle>{{ t('product.details.modifications') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <ul class="details-list">
              <li v-for="(mod, index) in product.modifications" :key="index" class="detail-list-item">
                {{ mod }}
              </li>
            </ul>
          </CardContent>
        </Card>

        <!-- Reason for Selling -->
        <Card v-if="product.reasonForSelling">
          <CardHeader>
            <CardTitle>{{ t('product.details.reasonForSelling') }}</CardTitle>
          </CardHeader>
          <CardContent>
            <p class="detail-text">{{ product.reasonForSelling }}</p>
          </CardContent>
        </Card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: calc(var(--spacing) * 4);
}

.product-grid {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: calc(var(--spacing) * 4);
  margin-bottom: calc(var(--spacing) * 4);
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

/* Product Images */
.product-images {
  width: 100%;
}

.product-images :deep(.card-content) {
  padding: 0;
}

.main-image {
  aspect-ratio: 1;
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  overflow: hidden;
  background-color: var(--accent);
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.image-thumbnails {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: calc(var(--spacing) * 4);
  padding: calc(var(--spacing) * 4);
}

.thumbnail-button {
  aspect-ratio: 1;
  border-radius: var(--radius-lg);
  overflow: hidden;
  background-color: var(--accent);
  border: 2px solid transparent;
  cursor: pointer;
}

.thumbnail-button.active {
  border-color: var(--primary);
}

.thumbnail-button img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* Product Info */
.product-info {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 6);
}

/* Seller Info */
.seller-info {
  display: flex;
  align-items: flex-start;
  gap: calc(var(--spacing) * 4);
  padding: calc(var(--spacing) * 4);
  background-color: var(--accent);
  border-radius: var(--radius-lg);
}

.seller-avatar {
  height: calc(var(--spacing) * 12);
  width: calc(var(--spacing) * 12);
}

.seller-details {
  display: flex;
  flex-direction: column;
  gap: var(--spacing);
}

.seller-details h3 {
  font-weight: var(--font-weight-semibold);
}

.seller-rating {
  display: flex;
  align-items: center;
  gap: var(--spacing);
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

.rating-stars {
  display: flex;
  align-items: center;
}

.star-icon {
  height: calc(var(--spacing) * 4);
  width: calc(var(--spacing) * 4);
  fill: var(--warning);
  color: var(--warning);
  margin-right: var(--spacing);
}

.seller-info-text {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

/* Price Section */
.price-section {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}

.price-display {
  display: flex;
  align-items: baseline;
  gap: calc(var(--spacing) * 2);
}

.current-price {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-bold);
}

.original-price {
  font-size: var(--font-size-lg);
  color: var(--muted-foreground);
  text-decoration: line-through;
}

.discount {
  font-size: var(--font-size-sm);
  color: var(--success);
}

/* Delivery Info */
.delivery-info {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}

.delivery-text {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

/* Action Buttons */
.action-buttons {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.buy-button,
.message-button {
  width: 100%;
}

/* Product Description */
.product-description {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}

.product-description h2 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
}

.product-description p {
  color: var(--muted-foreground);
}

/* Listing Details */
.listing-details {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.listing-details h2 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: calc(var(--spacing) * 4);
}

@media (min-width: 640px) {
  .details-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

.details-section {
  background-color: var(--background);
  border-radius: var(--radius);
  padding: calc(var(--spacing) * 3);
}

.details-section h3 {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  margin-bottom: calc(var(--spacing) * 2);
  color: var(--foreground);
}

.details-list {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 1);
}

.detail-label {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

.detail-value {
  font-size: var(--font-size-sm);
  color: var(--foreground);
}

.detail-list-item {
  font-size: var(--font-size-sm);
  color: var(--foreground);
  padding-left: calc(var(--spacing) * 2);
  position: relative;
}

.detail-list-item::before {
  content: '•';
  position: absolute;
  left: 0;
  color: var(--muted-foreground);
}

.detail-text {
  font-size: var(--font-size-sm);
  color: var(--foreground);
  line-height: 1.5;
}

/* Make certain sections span full width */
.details-grid> :nth-child(n + 3) {
  grid-column: 1 / -1;
}

/* Product Properties */
.product-properties {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.product-properties h2 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
}
</style>

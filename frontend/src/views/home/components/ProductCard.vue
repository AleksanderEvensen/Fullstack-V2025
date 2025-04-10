<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { HeartIcon } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { cn, formatAddress, formatPictureUrl } from '@/lib/utils'
import type { components } from '@/lib/api/schema'
import { useTypedI18n } from '@/i18n'
import { useBookmarkListing, useUnbookmarkListing } from '@/lib/api/queries/listings'
import { computed } from 'vue'

const { t } = useTypedI18n()

type Product = components['schemas']['ListingDto']

const props = defineProps<{
  product: Product
}>()

const { mutate: bookmarkListing, isPending: isBookmarking } = useBookmarkListing()
const { mutate: unbookmarkListing, isPending: isUnbookmarking } = useUnbookmarkListing()

const isBookmarked = computed(() => props.product.isBookmarked)

const handleBookmark = () => {
  if (isBookmarked.value) {
    unbookmarkListing(props.product.id)
  } else {
    bookmarkListing(props.product.id)
  }
}
</script>

<template>
  <div class="product-card">
    <RouterLink :to="`/marketplace/product/${product.id}`" class="product-image">
      <img :src="formatPictureUrl(product.images[0])" :alt="product.title" />
    </RouterLink>
    <div class="product-info">
      <RouterLink :to="`/marketplace/product/${product.id}`" class="product-title">
        {{ product.title }}
      </RouterLink>
      <div class="product-price">{{ product.price }} {{ t('home.currency') }}</div>
      <div class="product-meta">
        <div class="seller-info">
          <Avatar class="seller-avatar">
            <AvatarImage
              :src="
                product.seller.profileImageUrl
                  ? formatPictureUrl(product.seller.profileImageUrl)
                  : ''
              "
              :alt="product.seller.name"
            />
            <AvatarFallback>{{ product.seller.name[0] }}</AvatarFallback>
          </Avatar>
          <span class="seller-name">{{ product.seller.name }}</span>
        </div>
        <div class="product-location" v-if="product.seller.address">
          {{ formatAddress(product.seller.address) }}
        </div>
      </div>
      <div class="product-actions">
        <Button
          variant="ghost"
          size="icon"
          class="action-button"
          @click="handleBookmark"
          :disabled="isBookmarking || isUnbookmarking"
        >
          <HeartIcon :class="cn('icon', product.isBookmarked ? 'icon-filled' : '')" />
        </Button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.product-image {
  display: block;
  aspect-ratio: 1;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 12px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.product-title {
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-primary);
  text-decoration: none;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 4px;
  line-height: 1.3;
  height: 2.6em;
}

.product-title:hover {
  text-decoration: underline;
}

.product-price {
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.product-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 8px;
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.seller-avatar {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
}

.seller-name {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-location {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: var(--text-secondary);
}

.product-actions {
  display: flex;
  justify-content: flex-end;
  padding-top: 8px;
  border-top: 1px solid var(--border);
  margin-top: auto;
}

.action-button {
  padding: var(--spacing);
  flex: 0;
  justify-content: center;
}

.icon {
  width: 18px;
  height: 18px;
}

.icon-filled {
  color: red;
  fill: red;
}
</style>

<script lang="ts" setup>
import {Avatar, AvatarImage, AvatarFallback} from '@/components/ui/avatar'
import {Heart} from 'lucide-vue-next'
import { Button } from "@/components/ui/button";
import type { ItemCardData } from '../mock_data'

defineProps<{ item: ItemCardData }>()

function formatCurrency(value: number) {
  return Intl.NumberFormat('no-NB', {
    currency: 'NOK',
    style: 'currency',
  }).format(value)
}
</script>

<template>
  <div class="search-result-item">
    <div class="result-image">
      <img src="https://dummyimage.com/400x320/000/ffffff" :alt="item.title" />
      <Button variant="secondary" size="icon" class="favorite-button">
        <Heart />
      </Button>
      <!-- <button class="favorite-button">
        <Heart />
      </button> -->
    </div>
    <div v-if="item.type === 'mobility'" class="result-content">
      <h3 class="result-title">{{ item.title }}</h3>
      <div class="result-details">
        <div class="detail-row">
          <span class="year">{{ item.year }}</span>
          <span class="separator">•</span>
          <span class="fuel">{{ item.fuelType === 'electric' ? 'Elektrisitet' : 'Bensin' }}</span>
        </div>

        <div class="result-price">{{ formatCurrency(item.price) }}</div>

        <div class="seller-info">
          <Avatar class="seller-avatar">
            <AvatarImage :src="item.seller.avatar" :alt="item.seller.name" />
            <AvatarFallback>{{ item.seller.name.charAt(0) }}</AvatarFallback>
          </Avatar>
          <span class="seller-name">{{ item.seller.name }}</span>
        </div>
      </div>
    </div>
    <div v-if="item.type === 'default'" class="result-content">
      <h3 class="result-title">{{ item.title }}</h3>
      <div class="result-details">
        <!-- <div class="detail-row">
                  <span class="separator">•</span>
                </div> -->

        <div class="result-price">{{ formatCurrency(item.price) }}</div>

        <div class="seller-info">
          <Avatar class="seller-avatar">
            <AvatarImage :src="item.seller.avatar" :alt="item.seller.name" />
            <AvatarFallback>{{ item.seller.name.charAt(0) }}</AvatarFallback>
          </Avatar>
          <span class="seller-name">{{ item.seller.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
.search-result-item {
  display: flex;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
}

.result-image {
  width: 300px;
  height: 200px;
  position: relative;
}

.result-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.favorite-button {
  position: absolute;
  top: 10px;
  right: 10px;
}

.result-content {
  flex: 1;
  padding: 1rem;
  display: flex;
  flex-direction: column;
}

.result-title {
  font-size: 1.1rem;
  font-weight: 500;
  margin: 0 0 0.5rem 0;
}

.result-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
}

.separator {
  color: var(--muted-foreground);
}

.result-price {
  font-size: 1.2rem;
  font-weight: 500;
}

.dealer-info {
  display: flex;
  flex-direction: column;
  font-size: 0.85rem;
  color: var(--muted-foreground);
  margin-top: auto;
}

.seller-info {
    display: flex;
    align-items: center;
    gap: 8px;
}

.seller-avatar {
    width: 24px;
    height: 24px;
}

.seller-name {
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

@media (max-width: 900px) {
  .search-result-item {
    flex-direction: column;
  }
  
  .result-image {
    width: 100%;
  }
}
</style>
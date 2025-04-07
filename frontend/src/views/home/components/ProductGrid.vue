<script setup lang="ts">
import type { components } from '@/lib/api/schema';
import ProductCard from './ProductCard.vue'

type Product = components['schemas']['ListingResponse']

defineProps<{
    products: Product[]
}>()
</script>

<template>
    <div class="product-grid">
        <ProductCard v-for="product in products" :key="product.id" :product="product" />
    </div>
</template>

<style scoped>
.product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 24px;
    padding: 24px;
}

@media (max-width: 640px) {
    .product-grid {
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
        gap: 16px;
        padding: 16px;
    }
}
</style>
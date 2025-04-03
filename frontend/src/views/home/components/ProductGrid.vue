<script setup lang="ts">
import ProductCard from './ProductCard.vue'

interface Product {
    id: number
    title: string
    price: number
    location: string
    image: string
    seller: {
        name: string
        avatar: string
    }
    condition: string
    postedAt: string
}

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
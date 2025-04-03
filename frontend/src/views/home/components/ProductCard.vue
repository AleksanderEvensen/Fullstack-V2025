<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { HeartIcon, MessageCircleIcon } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

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
    product: Product
}>()
</script>

<template>
    <div class="product-card">
        <RouterLink :to="`/marketplace/product/${product.id}`" class="product-image">
            <img :src="product.image" :alt="product.title" />
        </RouterLink>
        <div class="product-info">
            <RouterLink :to="`/marketplace/product/${product.id}`" class="product-title">
                {{ product.title }}
            </RouterLink>
            <div class="product-price">{{ product.price }} {{ t('home.currency') }}</div>
            <div class="product-meta">
                <div class="seller-info">
                    <Avatar class="seller-avatar">
                        <AvatarImage :src="product.seller.avatar" :alt="product.seller.name" />
                        <AvatarFallback>{{ product.seller.name[0] }}</AvatarFallback>
                    </Avatar>
                    <span class="seller-name">{{ product.seller.name }}</span>
                </div>
                <div class="product-location">{{ product.location }}</div>
            </div>
            <div class="product-actions">
                <Button variant="ghost" size="icon" class="action-button">
                    <HeartIcon class="icon" />
                </Button>
                <Button variant="ghost" size="icon" class="action-button">
                    <MessageCircleIcon class="icon" />
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
    transition: transform 0.2s, box-shadow 0.2s;
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
}

.product-price {
    font-size: 1.125rem;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 8px;
}

.product-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
    font-size: 0.875rem;
    color: var(--text-secondary);
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

.product-location {
    color: var(--text-secondary);
}

.product-actions {
    display: flex;
    gap: 8px;
    padding-top: 8px;
    border-top: 1px solid var(--border);
}

.action-button {
    flex: 1;
    justify-content: center;
}

.icon {
    width: 18px;
    height: 18px;
}
</style>
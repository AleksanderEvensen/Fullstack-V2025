<script setup lang="ts">
import { ref } from 'vue';
import { StarIcon } from 'lucide-vue-next';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';

interface Seller {
    name: string;
    avatar: string;
    memberSince: string;
    rating: number;
    totalRatings: number;
    positiveFeedback: string;
}

interface ProductProperty {
    label: string;
    value: string;
}

interface ListingDetails {
    modelYear?: string;
    manufacturer?: string;
    model?: string;
    serialNumber?: string;
    purchaseDate?: string;
    usageDuration?: string;
    defects?: string[];
    modifications?: string[];
    reasonForSelling?: string;
}

interface Product {
    id: string;
    title: string;
    price: number;
    originalPrice: number;
    condition: 'New' | 'Like New' | 'Very Good' | 'Good' | 'Acceptable';
    description: string;
    category: string;
    subcategory: string;
    images: string[];
    seller: Seller;
    location: string;
    deliveryDate: string;
    properties: ProductProperty[];
    listingDetails: ListingDetails;
}

// Mock data - replace with actual API call
const product = ref<Product>({
    id: '1',
    title: 'Sony WH-1000XM5 Wireless Noise Cancelling Headphones',
    price: 348.00,
    originalPrice: 399.99,
    condition: 'Like New',
    description: 'Industry-leading noise cancellation. Exceptional sound quality. Nearly new condition, includes original packaging and accessories.',
    category: 'Electronics',
    subcategory: 'Headphones',
    images: ['/headphones-1.jpg', '/headphones-2.jpg', '/headphones-3.jpg'],
    seller: {
        name: 'Sony Official Store',
        avatar: 'https://github.com/shadcn.png',
        memberSince: 'Jan 2015',
        rating: 5,
        totalRatings: 2387,
        positiveFeedback: '99.8%'
    },
    location: 'San Francisco, 94105',
    deliveryDate: 'Tuesday, April 1',
    properties: [
        { label: 'Brand', value: 'Sony' },
        { label: 'Model', value: 'WH-1000XM5' },
        { label: 'Color', value: 'Silver' },
        { label: 'Connectivity', value: 'Bluetooth 5.2' },
        { label: 'Battery Life', value: 'Up to 30 hours' },
        { label: 'Noise Cancelling', value: 'Yes' },
        { label: 'Warranty', value: '1 year remaining' },
        { label: 'Package Contents', value: 'Headphones, Carrying case, USB-C charging cable, 3.5mm audio cable' }
    ],
    listingDetails: {
        modelYear: '2023',
        manufacturer: 'Sony Corporation',
        model: 'WH-1000XM5',
        serialNumber: 'SN123456789',
        purchaseDate: '2023-06-15',
        usageDuration: '8 months',
        defects: [
            'Minor scratch on left ear cup',
            'Small dent on headband'
        ],
        modifications: [
            'Custom ear pads installed',
            'Firmware updated to latest version'
        ],
        reasonForSelling: 'Upgrading to newer model'
    }
});

const currentImageIndex = ref(0);

const formatPrice = (price: number) => {
    return new Intl.NumberFormat('NO', {
        style: 'currency',
        currency: 'NOK'
    }).format(price);
};

const calculateDiscount = (original: number, current: number) => {
    return Math.round(((original - current) / original) * 100);
};
</script>

<template>
    <div class="container">
        <div class="product-grid">
            <!-- Product Images -->
            <div class="product-images">
                <div class="main-image">
                    <img :src="product.images[currentImageIndex]" :alt="product.title">
                </div>
                <div class="image-thumbnails">
                    <button v-for="(image, index) in product.images" :key="index" @click="currentImageIndex = index"
                        class="thumbnail-button" :class="{ 'active': currentImageIndex === index }">
                        <img :src="image" :alt="`Product image ${index + 1}`">
                    </button>
                </div>
            </div>


            <!-- Product Info -->
            <div class="product-info">
                <Card>
                    <CardHeader>
                        <CardTitle>{{ product.title }}</CardTitle>
                        <div class="condition-badge">
                            <Badge variant="outline">{{ product.condition }}</Badge>
                        </div>
                    </CardHeader>
                    <CardContent>
                        <div class="seller-info">
                            <Avatar class="seller-avatar">
                                <AvatarImage :src="product.seller.avatar" :alt="product.seller.name" />
                                <AvatarFallback>{{ product.seller.name[0] }}</AvatarFallback>
                            </Avatar>
                            <div class="seller-details">
                                <h3>{{ product.seller.name }}</h3>
                                <div class="seller-rating">
                                    <div class="rating-stars">
                                        <StarIcon class="star-icon" />
                                        <span>{{ product.seller.rating }}</span>
                                    </div>
                                    <span>({{ product.seller.totalRatings }} ratings)</span>
                                </div>
                                <div class="seller-info-text">
                                    Member since: {{ product.seller.memberSince }}
                                </div>
                                <div class="seller-info-text">
                                    {{ product.seller.positiveFeedback }} Positive Feedback
                                </div>
                            </div>
                        </div>

                        <div class="price-section">
                            <div class="price-display">
                                <span class="current-price">{{ formatPrice(product.price) }}</span>
                                <span class="original-price">{{ formatPrice(product.originalPrice) }}</span>
                                <span class="discount">
                                    Save {{ calculateDiscount(product.originalPrice, product.price) }}%
                                </span>
                            </div>
                        </div>

                        <div class="delivery-info">
                            <div class="delivery-text">
                                FREE delivery: {{ product.deliveryDate }}
                            </div>
                            <div class="delivery-text">
                                Delivering to: {{ product.location }}
                            </div>
                        </div>

                        <div class="action-buttons">
                            <Button class="buy-button" size="lg">Buy Now</Button>
                            <Button variant="outline" class="message-button" size="lg">Message Seller</Button>
                        </div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle>About this item</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <p>{{ product.description }}</p>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle>Listing Details</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div class="details-grid">
                            <Card>
                                <CardHeader>
                                    <CardTitle>Basic Information</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div class="details-list">
                                        <div class="detail-item" v-if="product.listingDetails.modelYear">
                                            <span class="detail-label">Model Year</span>
                                            <span class="detail-value">{{ product.listingDetails.modelYear }}</span>
                                        </div>
                                        <div class="detail-item" v-if="product.listingDetails.manufacturer">
                                            <span class="detail-label">Manufacturer</span>
                                            <span class="detail-value">{{ product.listingDetails.manufacturer
                                            }}</span>
                                        </div>
                                        <div class="detail-item" v-if="product.listingDetails.model">
                                            <span class="detail-label">Model</span>
                                            <span class="detail-value">{{ product.listingDetails.model }}</span>
                                        </div>
                                        <div class="detail-item" v-if="product.listingDetails.serialNumber">
                                            <span class="detail-label">Serial Number</span>
                                            <span class="detail-value">{{ product.listingDetails.serialNumber
                                            }}</span>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>

                            <!-- Usage Info -->
                            <Card>
                                <CardHeader>
                                    <CardTitle>Usage Information</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div class="details-list">
                                        <div class="detail-item" v-if="product.listingDetails.purchaseDate">
                                            <span class="detail-label">Purchase Date</span>
                                            <span class="detail-value">{{ product.listingDetails.purchaseDate
                                            }}</span>
                                        </div>
                                        <div class="detail-item" v-if="product.listingDetails.usageDuration">
                                            <span class="detail-label">Usage Duration</span>
                                            <span class="detail-value">{{ product.listingDetails.usageDuration
                                            }}</span>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>

                            <!-- Defects -->
                            <Card v-if="product.listingDetails.defects?.length">
                                <CardHeader>
                                    <CardTitle>Known Defects</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <ul class="details-list">
                                        <li v-for="(defect, index) in product.listingDetails.defects" :key="index"
                                            class="detail-list-item">
                                            {{ defect }}
                                        </li>
                                    </ul>
                                </CardContent>
                            </Card>

                            <!-- Modifications -->
                            <Card v-if="product.listingDetails.modifications?.length">
                                <CardHeader>
                                    <CardTitle>Modifications</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <ul class="details-list">
                                        <li v-for="(mod, index) in product.listingDetails.modifications" :key="index"
                                            class="detail-list-item">
                                            {{ mod }}
                                        </li>
                                    </ul>
                                </CardContent>
                            </Card>

                            <!-- Reason for Selling -->
                            <Card v-if="product.listingDetails.reasonForSelling">
                                <CardHeader>
                                    <CardTitle>Reason for Selling</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <p class="detail-text">{{ product.listingDetails.reasonForSelling }}</p>
                                </CardContent>
                            </Card>
                        </div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle>Specifications</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div class="properties-grid">
                            <div v-for="prop in product.properties" :key="prop.label" class="property-item">
                                <span class="property-label">{{ prop.label }}</span>
                                <span class="property-value">{{ prop.value }}</span>
                            </div>
                        </div>
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
    grid-template-columns: 1fr;
    gap: calc(var(--spacing) * 8);
}

@media (min-width: 768px) {
    .product-grid {
        grid-template-columns: repeat(2, 1fr);
    }
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
    content: "•";
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
.details-grid> :nth-child(n+3) {
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

.properties-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: calc(var(--spacing) * 2);
    padding: calc(var(--spacing) * 2);
    border-radius: var(--radius-lg);
}

@media (min-width: 640px) {
    .properties-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

.property-item {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 1);
    padding: calc(var(--spacing) * 2);
    border-radius: var(--radius);
    background-color: var(--accent);
}

.property-label {
    font-size: var(--font-size-sm);
    font-weight: var(--font-weight-medium);
    color: var(--muted-foreground);
}

.property-value {
    font-size: var(--font-size-sm);
    color: var(--foreground);
}
</style>
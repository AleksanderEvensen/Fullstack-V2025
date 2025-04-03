<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import ProductGrid from '@/views/home/components/ProductGrid.vue'
import { HeartIcon, PackageIcon } from 'lucide-vue-next'

const { t } = useI18n()

const user = ref({
    name: 'John Doe',
    avatar: 'https://github.com/shadcn.png',
    memberSince: '2024-01-01',
    location: 'Oslo, Norway',
    rating: 4.8,
    totalRatings: 127
})

const myListings = ref([
    {
        id: 1,
        title: 'Tesla Model 3 Long Range',
        price: 570000,
        location: 'Oslo',
        image: 'https://picsum.photos/id/1/400/400',
        seller: {
            name: user.value.name,
            avatar: user.value.avatar
        },
        condition: 'New',
        postedAt: '2024-03-15'
    },
    {
        id: 2,
        title: 'iPhone 15 Pro Max',
        price: 15999,
        location: 'Oslo',
        image: 'https://picsum.photos/id/2/400/400',
        seller: {
            name: user.value.name,
            avatar: user.value.avatar
        },
        condition: 'Like New',
        postedAt: '2024-03-14'
    }
])

const favoriteListings = ref([
    {
        id: 3,
        title: 'MacBook Pro M3',
        price: 29999,
        location: 'Trondheim',
        image: 'https://picsum.photos/id/3/400/400',
        seller: {
            name: 'Apple Store',
            avatar: 'https://github.com/shadcn.png'
        },
        condition: 'New',
        postedAt: '2024-03-13'
    },
    {
        id: 4,
        title: 'Sony WH-1000XM5',
        price: 3999,
        location: 'Bergen',
        image: 'https://picsum.photos/id/4/400/400',
        seller: {
            name: 'Electronics Shop',
            avatar: 'https://github.com/shadcn.png'
        },
        condition: 'Like New',
        postedAt: '2024-03-12'
    }
])
</script>

<template>
    <div class="profile-page">
        <!-- User Profile Header -->
        <div class="profile-header">
            <div class="profile-info">
                <Avatar class="profile-avatar">
                    <AvatarImage :src="user.avatar" :alt="user.name" />
                    <AvatarFallback>{{ user.name[0] }}</AvatarFallback>
                </Avatar>
                <div class="user-details">
                    <h1 class="user-name">{{ user.name }}</h1>
                    <div class="user-meta">
                        <span class="location">{{ user.location }}</span>
                        <span class="member-since">{{ t('profile.memberSince') }}: {{ user.memberSince }}</span>
                    </div>
                    <div class="user-rating">
                        <span class="rating">{{ user.rating }}</span>
                        <span class="total-ratings">({{ user.totalRatings }} {{ t('profile.ratings') }})</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Listings Tabs -->
        <Tabs default-value="my-listings" class="listings-tabs">
            <TabsList class="tabs-list">
                <TabsTrigger value="my-listings" class="tab-trigger">
                    <PackageIcon class="tab-icon" />
                    {{ t('profile.myListings') }}
                </TabsTrigger>
                <TabsTrigger value="favorites" class="tab-trigger">
                    <HeartIcon class="tab-icon" />
                    {{ t('profile.favorites') }}
                </TabsTrigger>
            </TabsList>

            <TabsContent value="my-listings" class="tab-content">
                <div class="listings-header">
                    <h2>{{ t('profile.myListings') }}</h2>
                    <span class="listing-count">{{ myListings.length }} {{ t('profile.items') }}</span>
                </div>
                <ProductGrid :products="myListings" />
            </TabsContent>

            <TabsContent value="favorites" class="tab-content">
                <div class="listings-header">
                    <h2>{{ t('profile.favorites') }}</h2>
                    <span class="listing-count">{{ favoriteListings.length }} {{ t('profile.items') }}</span>
                </div>
                <ProductGrid :products="favoriteListings" />
            </TabsContent>
        </Tabs>
    </div>
</template>

<style scoped>
.profile-page {
    max-width: 1300px;
    width: 100%;
    margin: 0 auto;
    padding: 2rem;
}

.profile-header {
    background: var(--card);
    border-radius: var(--radius);
    padding: 2rem;
    margin-bottom: 2rem;
    box-shadow: var(--shadow);
}

.profile-info {
    display: flex;
    align-items: center;
    gap: 2rem;
}

.profile-avatar {
    width: 120px;
    height: 120px;
}

.user-details {
    flex: 1;
}

.user-name {
    font-size: 2rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
}

.user-meta {
    display: flex;
    gap: 1rem;
    color: var(--muted-foreground);
    margin-bottom: 0.5rem;
}

.user-rating {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.rating {
    font-weight: bold;
    color: var(--primary);
}

.total-ratings {
    color: var(--muted-foreground);
}

.listings-tabs {
    margin-top: 2rem;
}

.tabs-list {
    margin-bottom: 2rem;
}

.tab-trigger {
    display: flex;
    align-items: center;
    flex-direction: row;
    gap: 0.5rem;
    justify-content: center;
}

.tab-icon {
    width: 20px;
    height: 20px;

}

.listings-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
}

.listing-count {
    color: var(--muted-foreground);
}

@media (max-width: 768px) {
    .profile-page {
        padding: 1rem;
    }

    .profile-info {
        flex-direction: column;
        text-align: center;
    }

    .user-meta {
        flex-direction: column;
        gap: 0.5rem;
    }
}
</style>
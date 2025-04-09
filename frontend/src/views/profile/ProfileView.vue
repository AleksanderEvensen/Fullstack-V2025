<script lang="ts" setup>
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { formatNameInitials } from '@/lib/utils'
import { CogIcon } from 'lucide-vue-next'
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import UserListing from './components/UserListing.vue'
import { useTypedI18n } from '@/i18n'

const userInfo = ref({
  name: 'Ola Nordmann',
  avatar: 'https://avatar.iran.liara.run/public/33.jpg',
  email: 'ola.nordmann@norge.no',
})

const userListings = ref([
  {
    id: 1,
    title: 'Tesla Model 3 Long Range',
    price: 5700000,
    location: 'Oslo',
    image: 'https://picsum.photos/id/1/400/400',
    seller: {
      name: 'Tesla Motors',
      avatar: 'https://github.com/shadcn.png',
    },
    condition: 'New',
    postedAt: '2024-03-15',
  },
  {
    id: 2,
    title: 'COWI - Senior Developer Position',
    price: 0,
    location: 'Oslo',
    image: 'https://picsum.photos/id/2/400/400',
    seller: {
      name: 'COWI',
      avatar: 'https://github.com/shadcn.png',
    },
    condition: 'Job Listing',
    postedAt: '2024-03-14',
  },
  {
    id: 3,
    title: 'Nike Air Max 270',
    price: 700,
    location: 'Trondheim',
    image: 'https://picsum.photos/id/3/400/400',
    seller: {
      name: 'Sneaker Store',
      avatar: 'https://github.com/shadcn.png',
    },
    condition: 'Like New',
    postedAt: '2024-03-13',
  },
  {
    id: 4,
    title: 'Nike Air Max 270',
    price: 700,
    location: 'Trondheim',
    image: 'https://picsum.photos/id/3/400/400',
    seller: {
      name: 'Sneaker Store',
      avatar: 'https://github.com/shadcn.png',
    },
    condition: 'Like New',
    postedAt: '2024-03-13',
  },
  {
    id: 3,
    title: 'Nike Air Max 270',
    price: 700,
    location: 'Trondheim',
    image: 'https://picsum.photos/id/3/400/400',
    seller: {
      name: 'Sneaker Store',
      avatar: 'https://github.com/shadcn.png',
    },
    condition: 'Like New',
    postedAt: '2024-03-13',
  },
  {
    id: 3,
    title: 'Nike Air Max 270',
    price: 700,
    location: 'Trondheim',
    image: 'https://picsum.photos/id/3/400/400',
    seller: {
      name: 'Sneaker Store',
      avatar: 'https://github.com/shadcn.png',
    },
    condition: 'Like New',
    postedAt: '2024-03-13',
  },
])

const activeTab = ref('all')

const setActiveTab = (tab: string) => {
  activeTab.value = tab
}

const { t } = useTypedI18n()
</script>

<template>
  <div class="container">
    <div class="profile-container">
      <Avatar class="profile-avatar">
        <AvatarImage :src="userInfo.avatar" />
        <AvatarFallback class="profile-fallback">{{
          formatNameInitials(userInfo.name)
        }}</AvatarFallback>
      </Avatar>
      <div class="profile-info">
        <h2 class="text-2xl info-name">{{ userInfo.name }}</h2>
        <span class="text-sm info-email">{{ userInfo.email }}</span>
        <div class="profile-actions">
          <RouterLink to="/profile/settings">
            <Button variant="outline" size="sm" class="text-xs">
              <CogIcon /> {{ t('common.settings') }}
            </Button>
          </RouterLink>
        </div>
      </div>
    </div>

    <hr style="margin: 1rem 0" />
    <div class="listings">
      <h1 class="listings-header">Mine annonser</h1>

      <!-- Filter tabs -->
      <div class="filter-tabs">
        <Button
          :variant="activeTab === 'all' ? 'default' : 'outline'"
          @click="setActiveTab('all')"
        >
          Alle (1)
        </Button>
        <Button
          :variant="activeTab === 'active' ? 'default' : 'outline'"
          @click="setActiveTab('active')"
        >
          Ferdig (1)
        </Button>
      </div>

      <!-- Listings -->
      <div class="listings-list">
        <UserListing
          v-for="listing in userListings"
          :key="listing.id"
          :image="listing.image"
          :title="listing.title"
          :likes="10"
          :published="new Date()"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  margin-top: calc(var(--spacing) * 10);
  display: flex;
  gap: calc(var(--spacing) * 5);
  align-items: center;
}

.info-name {
  font-weight: var(--font-weight-semibold);
}

.info-email {
  color: var(--muted-foreground);
}

.profile-avatar {
  width: calc(var(--spacing) * 30);
  height: calc(var(--spacing) * 30);

  .profile-fallback {
    font-size: var(--text-5xl);
  }
}
.profile-actions {
  margin-top: var(--spacing);
  svg {
    width: calc(var(--spacing) * 6);
    height: calc(var(--spacing) * 6);
  }

  .button {
    display: flex;
    gap: calc(var(--spacing) * 2);
    padding: calc(var(--spacing)) calc(var(--spacing) * 3);
  }
}

.listings-header {
  font-size: calc(var(--text-2xl) * 1.3);
  font-weight: var(--font-weight-semibold);
  margin-bottom: calc(var(--spacing) * 5);
}

.filter-tabs {
  display: flex;
  gap: calc(var(--spacing) * 2);
  margin-bottom: calc(var(--spacing) * 5);
}

.listings-list {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);

  container-type: inline-size;
  container-name: user-listings;
}


</style>

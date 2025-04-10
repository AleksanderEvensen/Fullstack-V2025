<script lang="ts" setup>
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { formatNameInitials, formatPictureUrl } from '@/lib/utils'
import { CogIcon } from 'lucide-vue-next'
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import { useTypedI18n } from '@/i18n'
import { useAuthStore } from '@/stores/auth'
import SoldListingsTab from './components/SoldListingsTab.vue'
import ActiveListingsTab from './components/ActiveListingsTab.vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import BookmarkedListingsTab from './components/BookmarkedListingsTab.vue'

const { user } = useAuthStore()
const activeTab = ref('active')

const setActiveTab = (tab: string) => {
  activeTab.value = tab
}

const { t } = useTypedI18n()
</script>

<template>
  <div class="container">
    <Card class="profile-container">
      <CardContent>
        <div class="profile-content">
          <Avatar class="profile-avatar">
            <AvatarImage :src="formatPictureUrl(user?.profileImageUrl ?? '')" />
            <AvatarFallback class="profile-fallback">{{
              formatNameInitials(user?.name ?? '')
            }}</AvatarFallback>
          </Avatar>
          <div class="profile-info">
            <h2 class="info-name">{{ user?.name }}</h2>
            <span class="info-email">{{ user?.email }}</span>
            <div class="profile-actions">
              <RouterLink to="/profile/settings">
                <Button variant="outline" size="sm">
                  <CogIcon /> {{ t('common.settings') }}
                </Button>
              </RouterLink>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>

    <Card class="listings">
      <CardHeader>
        <CardTitle>{{ t('profile.listings') }}</CardTitle>
      </CardHeader>
      <CardContent>
        <!-- Filter tabs -->
        <div class="filter-tabs">
          <Button :variant="activeTab === 'active' ? 'default' : 'outline'" @click="setActiveTab('active')">
            {{ t('profile.activeListings') }}
          </Button>
          <Button :variant="activeTab === 'sold' ? 'default' : 'outline'" @click="setActiveTab('sold')">
            {{ t('profile.soldListings') }}
          </Button>
          <Button :variant="activeTab === 'bookmarked' ? 'default' : 'outline'" @click="setActiveTab('bookmarked')">
            {{ t('profile.bookmarkedListings') }}
          </Button>
        </div>

        <!-- Tab content -->
        <ActiveListingsTab v-if="activeTab === 'active'" />
        <SoldListingsTab v-else-if="activeTab === 'sold'" />
        <BookmarkedListingsTab v-else />
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: calc(var(--spacing) * 4);
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 6);
}

.profile-container {
  margin-top: calc(var(--spacing) * 6);
}

.profile-content {
  display: flex;
  gap: calc(var(--spacing) * 5);
  align-items: center;
  margin-top: calc(var(--spacing) * 2);
}

.info-name {
  font-size: var(--text-2xl);
  font-weight: var(--font-weight-semibold);
  color: var(--foreground);
}

.info-email {
  color: var(--muted-foreground);
  font-size: var(--text-sm);
}

.profile-avatar {
  width: calc(var(--spacing) * 20);
  height: calc(var(--spacing) * 20);
  border: 2px solid var(--border);
  box-shadow: var(--shadow-sm);
}

.profile-avatar .profile-fallback {
  font-size: var(--text-4xl);
}

.profile-actions {
  margin-top: calc(var(--spacing) * 2);
}

.profile-actions svg {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

.profile-actions .button {
  display: flex;
  gap: calc(var(--spacing) * 2);
  align-items: center;
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 4);
}

.listings {
  margin-top: calc(var(--spacing) * 6);
}

.filter-tabs {
  display: flex;
  gap: calc(var(--spacing) * 2);
  margin-bottom: calc(var(--spacing) * 4);
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .container {
    padding: calc(var(--spacing) * 2);
    gap: calc(var(--spacing) * 4);
  }

  .profile-container {
    margin-top: calc(var(--spacing) * 4);
  }

  .profile-content {
    flex-direction: column;
    text-align: center;
  }

  .profile-avatar {
    width: calc(var(--spacing) * 16);
    height: calc(var(--spacing) * 16);
  }

  .profile-actions {
    justify-content: center;
  }

  .listings {
    margin-top: calc(var(--spacing) * 4);
  }

  .filter-tabs {
    justify-content: center;
  }

  .profile-info {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}



@media (max-width: 480px) {
  .container {
    padding: var(--spacing);
    gap: calc(var(--spacing) * 3);
  }

  .profile-avatar {
    width: calc(var(--spacing) * 14);
    height: calc(var(--spacing) * 14);
  }

  .info-name {
    font-size: var(--text-xl);
  }


}
</style>

<script lang="ts" setup>
import { computed } from 'vue'
import UserListing from './UserListing.vue'
import { formatPictureUrl } from '@/lib/utils'
import { useBookmarkedListings } from '@/lib/api/queries/listings'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n()
const { data: pagedListings } = useBookmarkedListings({
    page: 0,
    size: 10,
})

const userListings = computed(() => pagedListings.value?.content ?? [])
</script>

<template>
    <div class="listings-list">
        <UserListing v-for="listing in userListings" :key="listing.id" :listing="listing" />
        <div v-if="userListings.length === 0" class="empty-state">
            <p>{{ t('profile.listings.empty.bookmarked') }}</p>
        </div>
    </div>
</template>

<style scoped>
.listings-list {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 4);
}

.empty-state {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: calc(var(--spacing) * 8);
    background-color: var(--accent);
    border-radius: var(--radius-lg);
    color: var(--muted-foreground);
    text-align: center;
}
</style>
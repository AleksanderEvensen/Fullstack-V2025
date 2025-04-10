<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { useTypedI18n } from '@/i18n'
import { formatDate } from '@vueuse/core'
import { CheckIcon, MoreHorizontalIcon, TrashIcon } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { LISTING_QUERY_KEY, useDeleteListing, useUpdateListing } from '@/lib/api/queries/listings'
import { toast } from 'vue-sonner'
import { useQueryClient } from '@tanstack/vue-query'
import type { components } from '@/lib/api/schema'
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { formatPictureUrl } from '@/lib/utils'

type Listing = components['schemas']['ListingDto']

const { mutate: deleteListing, isPending: isDeleting } = useDeleteListing()
const { mutate: toggleSold, isPending: isTogglingSold } = useUpdateListing()
const queryClient = useQueryClient()
const router = useRouter()
const { t } = useTypedI18n()

const props = defineProps<{
  listing: Listing
}>()

const viewListing = () => {
  router.push({
    name: 'product',
    params: {
      id: props.listing.id
    }
  })
}

const handleDelete = () => {
  if (!props.listing.id) return
  deleteListing(props.listing.id, {
    onSuccess: () => {
      toast.success(t('product.deleteSuccess'))
      router.push('/')
    }
  })
}

const handleToggleSold = () => {
  if (!props.listing.id) return
  toggleSold({
    id: props.listing.id,
    status: props.listing.status === 'ACTIVE' ? 'SOLD' : 'ACTIVE',
    title: props.listing.title,
    categoryId: props.listing.categoryId,
    condition: props.listing.condition,
    price: props.listing.price,
    originalPrice: props.listing.originalPrice,
    description: props.listing.description,
    longitude: 0,
    latitude: 0,
  }, {
    onSuccess: () => {
      toast.success(t('product.toggleSoldSuccess'))
      queryClient.invalidateQueries({ queryKey: [LISTING_QUERY_KEY] })
    }
  })
}

const { user } = useAuthStore()
const canEditListing = computed(() => {
  return user?.id === props.listing.seller.id
})

</script>

<template>
  <Card class="user-listing">
    <img :src="formatPictureUrl(listing.images[0])" :alt="listing.title" class="user-listing-preview rounded-md" />

    <div class="listing-content">
      <div class="listing-info">
        <h4>{{ listing.title }}</h4>
        <Button @click="viewListing" variant="outline" size="sm">{{ t('profile.view-listing') }}</Button>
      </div>
      <div class="listing-actions">
        <span class="listing-date">{{ formatDate(new Date(listing.createdAt), 'DD. MMM YYYY') }}</span>
        <DropdownMenu v-if="canEditListing">
          <DropdownMenuTrigger>
            <MoreHorizontalIcon :size="16" />
          </DropdownMenuTrigger>
          <DropdownMenuContent>

            <DropdownMenuItem class="dropdown-menu-item" @click="handleToggleSold" :disabled="isTogglingSold">
              <CheckIcon />
              {{ listing.status === 'ACTIVE' ? t('product.toggleSold') : t('product.toggleActive') }}
            </DropdownMenuItem>
            <DropdownMenuItem class="dropdown-menu-item danger" @click="handleDelete" :disabled="isDeleting">
              <TrashIcon />
              {{ t('product.delete') }}
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  </Card>
</template>

<style scoped>
.user-listing {
  display: flex;
  flex-direction: row;
}

.user-listing-preview {
  width: calc(var(--spacing) * 30);
  height: calc(var(--spacing) * 30);
  object-fit: cover;
}

@container user-listings (max-width: 475px) {
  .user-listing {
    flex-direction: column;
  }

  .user-listing-preview {
    width: 100%;
  }
}

.dropdown-menu-item {
  display: flex;
  align-items: center;
  gap: calc(var(--spacing) * 2);
}

.danger {
  color: red;
}

.listing-content {
  display: flex;
  width: 100%;
  padding: calc(var(--spacing) * 4);
  flex-direction: row;
  justify-content: space-between;
}

.listing-info {
  display: flex;
  flex-direction: column;
  justify-items: start;

  button {
    margin-top: auto;
    width: fit-content;
  }
}

.listing-actions {
  display: flex;
  flex-direction: column;
  align-items: end;
  justify-content: space-around;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--spacing);
  color: var(--muted-foreground);
}
</style>

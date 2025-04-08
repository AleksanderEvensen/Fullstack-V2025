<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { formatDate } from '@vueuse/core'
import { HeartIcon, MoreHorizontalIcon } from 'lucide-vue-next'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

defineProps<{
  image: string
  title: string
  likes: number
  published: Date
}>()
</script>

<template>
  <Card class="user-listing">
    <img :src="image" :alt="title" class="user-listing-preview rounded-md" />

    <div class="listing-content">
      <div class="listing-info">
        <span class="stat-item"><HeartIcon :size="16" /> {{ likes }}</span>
        <h4>{{ title }}</h4>
        <Button variant="outline" size="sm">{{ t('profile.view-listign') }}</Button>
      </div>
      <div class="listing-actions">
        <span class="listing-date">{{ formatDate(published, 'DD. MMM YYYY') }}</span>
        <DropdownMenu>
          <DropdownMenuTrigger>
            <MoreHorizontalIcon :size="16" />
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuItem>Test</DropdownMenuItem>
            <DropdownMenuItem>Test2</DropdownMenuItem>
            <DropdownMenuItem>Test3</DropdownMenuItem>
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

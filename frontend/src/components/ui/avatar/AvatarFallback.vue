<script setup lang="ts">
import { AvatarFallback, type AvatarFallbackProps } from 'reka-ui'
import { cn } from '@/lib/utils'

const props = defineProps<AvatarFallbackProps & { class?: string }>()
</script>

<template>
  <AvatarFallback v-bind="props" :class="cn('avatar-fallback', props.class)">
    <slot />
  </AvatarFallback>
</template>

<style scoped>
.avatar-fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  background-color: var(--muted);
  color: var(--muted-foreground);
  font-weight: var(--font-weight-medium);
}
</style>

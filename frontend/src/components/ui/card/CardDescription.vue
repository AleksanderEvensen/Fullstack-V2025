<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <p :class="cn('text-sm _card-description-component', props.class)">
    <slot />
  </p>
</template>

<style scoped>
._card-description-component {
  color: var(--muted-foreground);
}
</style>

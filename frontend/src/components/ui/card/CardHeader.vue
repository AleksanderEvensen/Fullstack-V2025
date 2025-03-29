<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <div :class="cn('_card-header-component', props.class)">
    <slot />
  </div>
</template>

<style scoped>
._card-header-component {
  display: flex;
  flex-direction: column;
  row-gap: calc(var(--spacing) * 1.5);
  padding: calc(var(--spacing) * 6);
}
</style>
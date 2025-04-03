<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <h3 :class="cn('text-2xl _card-title-component', props.class)">
    <slot />
  </h3>
</template>

<style scoped>
._card-title-component {
  color: var(--card-title);
  font-weight: var(--font-weight-semibold);
  line-height: 1;
}
</style>

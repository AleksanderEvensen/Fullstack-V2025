<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <div :class="cn('_card-content-component', props.class)">
    <slot />
  </div>
</template>

<style scoped>
  ._card-content-component {
    display: flex;
    align-items: center;
    padding: calc(var(--spacing) * 6);
    padding-top: 0;
  }
</style>
<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'

const props = defineProps<{ class?: HTMLAttributes['class'] }>()
</script>

<template>
  <div :class="cn('sheet-header', $attrs.class ?? '')">
    <slot />
  </div>
</template>

<style lang="css" scoped>
.sheet-header {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
  text-align: center;
}

@media (min-width: 640px) {
  .sheet-header {
    text-align: left;
  }
}
</style>

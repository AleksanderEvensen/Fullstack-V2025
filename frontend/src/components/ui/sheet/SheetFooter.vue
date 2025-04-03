<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'

defineProps<{ class?: HTMLAttributes['class'] }>()
</script>

<template>
  <div :class="cn('sheet-footer', $attrs.class ?? '')">
    <slot />
  </div>
</template>

<style lang="css" scoped>
.sheet-footer {
  display: flex;
  flex-direction: column-reverse;
  gap: calc(var(--spacing) * 2);
}

@media (min-width: 640px) {
  .sheet-footer {
    flex-direction: row;
    justify-content: flex-end;
  }
}
</style>

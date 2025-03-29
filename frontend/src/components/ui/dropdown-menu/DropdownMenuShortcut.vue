<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <span :class="cn('dropdown-menu-shortcut', props.class)">
    <slot />
  </span>
</template>

<style lang="css" scoped>
.dropdown-menu-shortcut {
  display: inline-flex;
  margin-left: auto;
  padding-left: calc(var(--spacing) * 4);
  font-size: 12px;
  color: var(--muted-foreground);
}
</style>

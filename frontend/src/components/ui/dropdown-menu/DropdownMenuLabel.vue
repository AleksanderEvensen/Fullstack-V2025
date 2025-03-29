<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuLabel,
  type DropdownMenuLabelProps,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuLabelProps & {
  class?: HTMLAttributes['class']
  inset?: boolean
}>()
</script>

<template>
  <DropdownMenuLabel v-bind="props"
    :class="cn('dropdown-menu-label', { 'dropdown-menu-label-inset': props.inset }, props.class)">
    <slot />
  </DropdownMenuLabel>
</template>

<style lang="css" scoped>
.dropdown-menu-label {
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 2);
  font-size: 14px;
  font-weight: var(--font-weight-medium);
  color: var(--foreground);
}

.dropdown-menu-label-inset {
  padding-left: calc(var(--spacing) * 8);
}
</style>

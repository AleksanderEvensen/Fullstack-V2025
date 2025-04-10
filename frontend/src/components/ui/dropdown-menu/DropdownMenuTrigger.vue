<script setup lang="ts">
import { cn } from '@/lib/utils'
import { DropdownMenuTrigger, type DropdownMenuTriggerProps } from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuTriggerProps & { class?: HTMLAttributes['class'] }>()
</script>

<template>
  <DropdownMenuTrigger  v-bind="props" :class="cn(!props.asChild && 'dropdown-menu-trigger', props.class)">
    <slot />
  </DropdownMenuTrigger>
</template>

<style lang="css" scoped>
.dropdown-menu-trigger {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: calc(var(--radius) - 2px);
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 4);
  font-size: 14px;
  font-weight: var(--font-weight-medium);
  background-color: var(--background);
  color: var(--foreground);
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  border: 1px solid var(--border);
}

.dropdown-menu-trigger:focus-visible {
  outline: none;
  box-shadow:
    0 0 0 2px var(--ring),
    0 0 0 4px var(--ring-offset, white);
}

.dropdown-menu-trigger:hover:not(:disabled) {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-trigger:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.dropdown-menu-trigger[data-state='open'] {
  background-color: var(--accent);
  color: var(--accent-foreground);
}
</style>

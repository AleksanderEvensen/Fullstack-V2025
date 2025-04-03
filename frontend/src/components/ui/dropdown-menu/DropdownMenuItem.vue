<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuItem,
  type DropdownMenuItemEmits,
  type DropdownMenuItemProps,
  useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<
  DropdownMenuItemProps & {
    class?: HTMLAttributes['class']
    inset?: boolean
  }
>()
const emits = defineEmits<DropdownMenuItemEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
  <DropdownMenuItem
    v-bind="forwarded"
    :class="cn('dropdown-menu-item', { 'dropdown-menu-item-inset': props.inset }, props.class)"
  >
    <slot />
  </DropdownMenuItem>
</template>

<style lang="css" scoped>
.dropdown-menu-item {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  padding: calc(var(--spacing) * 1.5) calc(var(--spacing) * 2);
  font-size: 14px;
  color: var(--foreground);
  background: none;
  border: none;
  border-radius: calc(var(--radius) - 4px);
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  text-align: left;
  user-select: none;
}

.dropdown-menu-item:hover:not(:disabled) {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-item:focus {
  outline: none;
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-item:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.dropdown-menu-item-inset {
  padding-left: calc(var(--spacing) * 8);
}
</style>

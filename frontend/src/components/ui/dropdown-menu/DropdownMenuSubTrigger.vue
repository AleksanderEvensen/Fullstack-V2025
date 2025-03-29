<script setup lang="ts">
import { DropdownMenuSubTrigger } from 'reka-ui'
import { cn } from "@/lib/utils"

interface Props {
  class?: string
  inset?: boolean
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  class: '',
  inset: false,
  disabled: false
})
</script>

<template>
  <DropdownMenuSubTrigger :disabled="disabled"
    :class="cn('dropdown-menu-sub-trigger', { 'dropdown-menu-sub-trigger-inset': inset }, props.class)">
    <slot />
    <span class="dropdown-menu-sub-trigger-icon">
      <slot name="icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
          class="dropdown-menu-chevron">
          <path d="m9 18 6-6-6-6" />
        </svg>
      </slot>
    </span>
  </DropdownMenuSubTrigger>
</template>

<style lang="css" scoped>
.dropdown-menu-sub-trigger {
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

.dropdown-menu-sub-trigger:hover:not(:disabled) {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-sub-trigger:focus {
  outline: none;
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-sub-trigger:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.dropdown-menu-sub-trigger[data-state="open"] {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-sub-trigger-inset {
  padding-left: calc(var(--spacing) * 8);
}

.dropdown-menu-sub-trigger-icon {
  margin-left: auto;
  height: calc(var(--spacing) * 4);
  width: calc(var(--spacing) * 4);
  display: flex;
  align-items: center;
  justify-content: center;
}

.dropdown-menu-chevron {
  height: calc(var(--spacing) * 4);
  width: calc(var(--spacing) * 4);
}
</style>

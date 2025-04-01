<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { type BadgeVariants, badgeVariants } from '.'

const props = defineProps<{
  variant?: BadgeVariants['variant']
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <div :class="cn(badgeVariants({ variant }), props.class)">
    <slot />
  </div>
</template>

<style scoped>
.badge {
  display: inline-flex;
  align-items: center;
  border-radius: var(--radius);
  padding: calc(var(--spacing) * 0.5) calc(var(--spacing) * 2.5);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  transition-property: color, background-color, border-color;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
}

.badge:focus {
  outline: none;
  box-shadow: 0 0 0 2px var(--ring), 0 0 0 4px var(--ring-offset, white);
}

/* Variants */
.badge-default {
  background-color: var(--primary);
  color: var(--primary-foreground);
  border: 1px solid transparent;
}

.badge-default:hover {
  background-color: color-mix(in srgb, var(--primary) 90%, white);
}

.badge-secondary {
  background-color: var(--secondary);
  color: var(--secondary-foreground);
  border: 1px solid transparent;
}

.badge-secondary:hover {
  background-color: color-mix(in srgb, var(--secondary) 90%, black);
}

.badge-destructive {
  background-color: var(--destructive);
  color: var(--destructive-foreground);
  border: 1px solid transparent;
}

.badge-destructive:hover {
  background-color: color-mix(in srgb, var(--destructive) 90%, white);
}

.badge-outline {
  background-color: transparent;
  color: var(--foreground);
  border: 1px solid var(--input);
}

.badge-outline:hover {
  background-color: var(--accent);
  color: var(--accent-foreground);
}
</style>

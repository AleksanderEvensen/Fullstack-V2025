<script setup lang="ts">
import { cn } from '@/lib/utils'
import { ChevronDown } from 'lucide-vue-next'
import {
  NavigationMenuTrigger,
  type NavigationMenuTriggerProps,
  useForwardProps,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<NavigationMenuTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <NavigationMenuTrigger v-bind="forwardedProps" :class="cn('nav-menu-trigger', props.class)">
    <slot />
    <ChevronDown class="nav-menu-trigger-icon" aria-hidden="true" />
  </NavigationMenuTrigger>
</template>

<style>
.nav-menu-trigger {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-md);
  padding: calc(var(--spacing) * 1.5) calc(var(--spacing) * 3);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  background-color: transparent;
  color: var(--foreground);
  transition: color 0.2s, background-color 0.2s;
}

.nav-menu-trigger:hover {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.nav-menu-trigger:focus {
  outline: none;
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.nav-menu-trigger[data-state="open"] {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.nav-menu-trigger-icon {
  position: relative;
  top: 1px;
  margin-left: calc(var(--spacing) * 1);
  height: calc(var(--spacing) * 3);
  width: calc(var(--spacing) * 3);
  transition: transform 0.3s;
}

.nav-menu-trigger[data-state="open"] .nav-menu-trigger-icon {
  transform: rotate(180deg);
}
</style>

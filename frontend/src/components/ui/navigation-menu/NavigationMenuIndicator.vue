<script setup lang="ts">
import { cn } from '@/lib/utils'
import { NavigationMenuIndicator, type NavigationMenuIndicatorProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<NavigationMenuIndicatorProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <NavigationMenuIndicator v-bind="forwardedProps" :class="cn('nav-menu-indicator', props.class)">
    <div class="nav-menu-indicator-arrow" />
  </NavigationMenuIndicator>
</template>

<style>
.nav-menu-indicator {
  position: relative;
  top: 100%;
  z-index: 1;
  display: flex;
  height: calc(var(--spacing) * 1.5);
  align-items: flex-end;
  justify-content: center;
  overflow: hidden;
}

.nav-menu-indicator[data-state="visible"] {
  animation: indicatorFadeIn 0.2s ease;
}

.nav-menu-indicator[data-state="hidden"] {
  animation: indicatorFadeOut 0.2s ease;
}

.nav-menu-indicator-arrow {
  position: relative;
  top: 60%;
  height: calc(var(--spacing) * 2);
  width: calc(var(--spacing) * 2);
  transform: rotate(45deg);
  border-top-left-radius: var(--radius-sm);
  background-color: var(--border);
  box-shadow: var(--shadow-md);
}

@keyframes indicatorFadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes indicatorFadeOut {
  from {
    opacity: 1;
  }

  to {
    opacity: 0;
  }
}
</style>

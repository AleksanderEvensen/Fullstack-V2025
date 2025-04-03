<script setup lang="ts">
import { cn } from '@/lib/utils'
import { NavigationMenuViewport, type NavigationMenuViewportProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<NavigationMenuViewportProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <div class="nav-menu-viewport-wrapper">
    <NavigationMenuViewport v-bind="forwardedProps" :class="cn('nav-menu-viewport', props.class)" />
  </div>
</template>

<style>
.nav-menu-viewport-wrapper {
  position: absolute;
  left: 0;
  top: 100%;
  display: flex;
  justify-content: center;
}

.nav-menu-viewport {
  position: relative;
  margin-top: calc(var(--spacing) * 1.5);
  height: var(--reka-navigation-menu-viewport-height);
  width: 100%;
  overflow: hidden;
  border-radius: var(--radius-md);
  border: 1px solid var(--border);
  background-color: var(--popover);
  color: var(--popover-foreground);
  box-shadow: var(--shadow-md);
  transform-origin: top center;
}

.nav-menu-viewport[data-state='open'] {
  animation: viewportZoomIn 0.2s ease;
}

.nav-menu-viewport[data-state='closed'] {
  animation: viewportZoomOut 0.2s ease;
}

@media (min-width: 768px) {
  .nav-menu-viewport {
    width: var(--reka-navigation-menu-viewport-width);
  }
}

@keyframes viewportZoomIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }

  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes viewportZoomOut {
  from {
    opacity: 1;
    transform: scale(1);
  }

  to {
    opacity: 0;
    transform: scale(0.95);
  }
}
</style>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import { NavigationMenuList, type NavigationMenuListProps, useForwardProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<NavigationMenuListProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <NavigationMenuList v-bind="forwardedProps" :class="cn('nav-menu-list', props.class)">
    <slot />
  </NavigationMenuList>
</template>

<style>
.nav-menu-list {
  display: flex;
  flex: 1;
  list-style: none;
  align-items: center;
  justify-content: center;
  gap: calc(var(--spacing) * 1);
}

/* Group hover states for child elements */
.nav-menu-list[data-orientation="horizontal"] {
  flex-direction: row;
}

.nav-menu-list[data-orientation="vertical"] {
  flex-direction: column;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .nav-menu-list {
    flex-direction: column;
    gap: calc(var(--spacing) * 2);
  }
}
</style>

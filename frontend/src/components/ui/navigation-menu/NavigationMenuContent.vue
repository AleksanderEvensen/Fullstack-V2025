<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  NavigationMenuContent,
  type NavigationMenuContentEmits,
  type NavigationMenuContentProps,
  useForwardPropsEmits,
} from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<NavigationMenuContentProps & { class?: HTMLAttributes['class'] }>()

const emits = defineEmits<NavigationMenuContentEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <NavigationMenuContent v-bind="forwarded" :class="cn('nav-menu-content', props.class)">
    <slot />
  </NavigationMenuContent>
</template>

<style>
.nav-menu-content {
  position: absolute;
  left: 0;
  top: 0;
  width: auto;
}

.nav-menu-content[data-motion^="from-"],
.nav-menu-content[data-state="open"],
.nav-menu-content[data-state="visible"] {
  --enter-opacity: initial;
  --enter-translate-x: initial;
  animation: nav-view-change 0.2s ease;
}

.nav-menu-content[data-motion^="to-"],
.nav-menu-content[data-state="closed"],
.nav-menu-content[data-state="hidden"] {
  --enter-opacity: initial;
  --enter-translate-x: initial;
  animation: nav-view-change 0.2s ease;
}

/* Fade in */
.nav-menu-content[data-motion^="from-"],
.nav-menu-content[data-state="visible"] {
  --enter-opacity: 0;
}


/* Slide in from left and right */
.nav-menu-content[data-motion="from-end"] {
  --enter-translate-x: calc(var(--spacing) * 13)
}
.nav-menu-content[data-motion="from-start"] {
  --enter-translate-x: calc(var(--spacing) * -13)
}

@keyframes nav-view-change {
  0% {
    opacity: var(--enter-opacity, 1);
    transform: translateX(var(--enter-translate-x, 0))
  }
}

</style>

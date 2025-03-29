<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  NavigationMenuRoot,
  type NavigationMenuRootEmits,
  type NavigationMenuRootProps,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'
import NavigationMenuViewport from './NavigationMenuViewport.vue'

const props = defineProps<NavigationMenuRootProps & { class?: HTMLAttributes['class'] }>()

const emits = defineEmits<NavigationMenuRootEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <NavigationMenuRoot v-bind="forwarded" :class="cn('nav-menu-root', props.class)">
    <slot />
    <NavigationMenuViewport />
  </NavigationMenuRoot>
</template>

<style>
.nav-menu-root {
  position: relative;
  z-index: 10;
  display: flex;
  max-width: max-content;
  flex: 1;
  align-items: center;
  justify-content: center;
}
</style>

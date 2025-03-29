<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuRoot,
  type DropdownMenuRootEmits,
  type DropdownMenuRootProps,
  useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuRootProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<DropdownMenuRootEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
  <DropdownMenuRoot v-bind="forwarded" :class="cn('dropdown-menu', props.class)">
    <slot />
  </DropdownMenuRoot>
</template>

<style lang="css" scoped>
.dropdown-menu {
  position: relative;
  display: inline-block;
}
</style>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuSub,
  type DropdownMenuSubEmits,
  type DropdownMenuSubProps,
  useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuSubProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<DropdownMenuSubEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
  <DropdownMenuSub v-bind="forwarded" :class="cn('dropdown-menu-sub', props.class)">
    <slot />
  </DropdownMenuSub>
</template>

<style lang="css" scoped>
.dropdown-menu-sub {
  position: relative;
}
</style>

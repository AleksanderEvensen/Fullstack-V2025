<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuRadioGroup,
  type DropdownMenuRadioGroupEmits,
  type DropdownMenuRadioGroupProps,
  useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuRadioGroupProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<DropdownMenuRadioGroupEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
  <DropdownMenuRadioGroup v-bind="forwarded" :class="cn('dropdown-menu-radio-group', props.class)">
    <slot />
  </DropdownMenuRadioGroup>
</template>

<style lang="css" scoped>
.dropdown-menu-radio-group {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 0.5);
}
</style>

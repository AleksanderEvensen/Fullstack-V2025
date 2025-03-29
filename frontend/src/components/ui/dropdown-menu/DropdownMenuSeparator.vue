<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuSeparator,
  type DropdownMenuSeparatorProps,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuSeparatorProps & { class?: HTMLAttributes['class'] }>()

</script>

<template>
  <DropdownMenuSeparator v-bind="props" :class="cn('dropdown-menu-separator', props.class)" />
</template>

<style lang="css" scoped>
.dropdown-menu-separator {
  height: 1px;
  margin: calc(var(--spacing) * 1) calc(var(--spacing) * -1);
  background-color: var(--border);
}
</style>

<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { SelectLabel, type SelectLabelProps } from 'reka-ui'

const props = defineProps<SelectLabelProps & { class?: HTMLAttributes['class'] }>()
</script>

<template>
  <SelectLabel :class="cn('text-sm _select-label-component', props.class)">
    <slot />
  </SelectLabel>
</template>

<style scoped>
  ._select-label-component {
    padding: 
      calc(var(--spacing) * 1.5) /* top */
      calc(var(--spacing) * 2) /* right */
      calc(var(--spacing) * 1.5) /* bottom */
      calc(var(--spacing) * 8); /* left */
    font-weight: var(--font-weight-semibold);
  }
</style>

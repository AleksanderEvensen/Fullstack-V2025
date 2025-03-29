<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { SelectLabel, type SelectLabelProps } from 'reka-ui'

const props = defineProps<SelectLabelProps & { class?: HTMLAttributes['class'] }>()
</script>

<template>
  <SelectLabel :class="cn('select-label', props.class)">
    <slot />
  </SelectLabel>
</template>

<style scoped>
.select-label {
  padding: calc(var(--spacing) * 1.5) calc(var(--spacing) * 2);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
}
</style>

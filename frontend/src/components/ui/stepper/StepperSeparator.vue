<script lang="ts" setup>
import type { StepperSeparatorProps } from 'reka-ui'
import { cn } from '@/lib/utils'
import { StepperSeparator, useForwardProps } from 'reka-ui'

import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<StepperSeparatorProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardProps(delegatedProps)
</script>

<template>
  <StepperSeparator v-bind="forwarded" :class="cn('stepper-separator', props.class)" />
</template>

<style>
.stepper-separator {
  background-color: var(--muted);
}

.stepper-separator[data-disabled] {
  background-color: var(--muted);
  opacity: 0.5;
}

.stepper-separator[data-state='completed'] {
  background-color: var(--accent-foreground);
}
</style>

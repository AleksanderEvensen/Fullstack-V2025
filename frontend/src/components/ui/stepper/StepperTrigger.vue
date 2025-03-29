<script lang="ts" setup>
import type { StepperTriggerProps } from 'reka-ui'
import { cn } from '@/lib/utils'
import { StepperTrigger, useForwardProps } from 'reka-ui'

import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<StepperTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardProps(delegatedProps)
</script>

<template>
  <StepperTrigger v-bind="forwarded" :class="cn('stepper-trigger', props.class)">
    <slot />
  </StepperTrigger>
</template>

<style>
.stepper-trigger {
  padding: calc(var(--spacing) * 1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: calc(var(--spacing) * 1);
  border-radius: var(--radius);
}
</style>

<script lang="ts" setup>
import type { StepperTitleProps } from 'reka-ui'
import { cn } from '@/lib/utils'
import { StepperTitle, useForwardProps } from 'reka-ui'

import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<StepperTitleProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardProps(delegatedProps)
</script>

<template>
  <StepperTitle v-bind="forwarded" :class="cn('stepper-title', props.class)">
    <slot />
  </StepperTitle>
</template>

<style>
.stepper-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  white-space: nowrap;
}
</style>

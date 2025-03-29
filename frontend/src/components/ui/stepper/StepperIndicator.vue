<script lang="ts" setup>
import type { StepperIndicatorProps } from 'reka-ui'
import { cn } from '@/lib/utils'
import { StepperIndicator, useForwardProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<StepperIndicatorProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})

const forwarded = useForwardProps(delegatedProps)
</script>

<template>
  <StepperIndicator v-bind="forwarded" :class="cn('stepper-indicator', props.class)">
    <slot />
  </StepperIndicator>
</template>

<style>
.stepper-indicator {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: calc(var(--spacing) * 8);
  height: calc(var(--spacing) * 8);
  border-radius: 9999px;
  color: var(--muted-foreground);
  opacity: 0.5;
}

.stepper-indicator[data-state="active"] {
  background-color: var(--primary);
  color: var(--primary-foreground);
  opacity: 1;
}

.stepper-indicator[data-state="completed"] {
  background-color: var(--accent);
  color: var(--accent-foreground);
  opacity: 1;
}

.stepper-item[data-disabled] .stepper-indicator {
  opacity: 0.5;
}
</style>

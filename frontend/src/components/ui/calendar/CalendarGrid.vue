<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { CalendarGrid, type CalendarGridProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarGridProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarGrid :class="cn('_calendar-grid-component', props.class)" v-bind="forwardedProps">
    <slot />
  </CalendarGrid>
</template>

<style scoped lang="scss">
._calendar-grid-component {
  width: 100%;
  border-collapse: collapse;

  & > :not([hidden]) ~ :not([hidden]) {
    margin-top: calc(var(--spacing) * 1);
    margin-bottom: calc(var(--spacing) * 1);
  }
}
</style>

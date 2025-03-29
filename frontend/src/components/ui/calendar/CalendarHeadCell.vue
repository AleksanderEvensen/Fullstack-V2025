<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { CalendarHeadCell, type CalendarHeadCellProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarHeadCellProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarHeadCell :class="cn('rounded-md _calendar-head-cell-component', props.class)" v-bind="forwardedProps">
    <slot />
  </CalendarHeadCell>
</template>

<style scoped>
  ._calendar-head-cell-component {
    width: calc(var(--spacing) * 9);
    font-size: 0.8rem;
    font-weight: var(--font-weight-normal);
    color: var(--muted-foreground)
  }
</style>
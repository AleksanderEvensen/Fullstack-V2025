<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { CalendarCell, type CalendarCellProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarCellProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarCell
    :class="cn('text-sm _calendar-cell-component', props.class)"
    v-bind="forwardedProps"
  >
    <slot />
  </CalendarCell>
</template>

<style scoped lang="scss">
  /*'relative h-9 w-9 p-0 text-center text-sm focus-within:relative focus-within:z-20 [&:has([data-selected])]:rounded-md [&:has([data-selected])]:bg-accent [&:has([data-selected][data-outside-view])]:bg-accent/50*/

  ._calendar-cell-component {
    display: flex;
    height: calc(var(--spacing) * 9);
    width: calc(var(--spacing) * 9);
    text-align: center;

    &:focus-within {
      position: relative;
      z-index: 20;
    }

    &:has([data-selected]) {
      border-radius: calc(var(--radius) - 2px);
      background-color: var(--accent);
    }
    &:has([data-selected][data-outside-view]) {
      background-color: color-mix(in srgb, var(--accent) 50%, transparent);
    }
  }

</style>
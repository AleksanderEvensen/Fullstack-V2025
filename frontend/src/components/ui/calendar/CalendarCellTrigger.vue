<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { CalendarCellTrigger, type CalendarCellTriggerProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarCellTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarCellTrigger
    :class="cn('_calendar-cell-trigger-component', props.class)"
    v-bind="forwardedProps"
  >
    <slot />
  </CalendarCellTrigger>
</template>

<style scoped lang="scss">

._calendar-cell-trigger-component {

  // From Button.vue (ghost variant)
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-weight-medium);
  transition-property: color, background-color, border-color;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
  cursor: pointer;
  &:hover {
    background-color: var(--accent);
    color: var(--accent-foreground);
  }

  height: calc(var(--spacing) * 9); 
  width: calc(var(--spacing) * 9);   
  padding: 0;
  
  &[data-today]:not([data-selected]) {
    background-color: var(--accent);
    color: var(--accent-foreground);
  }

  &[data-selected] {
    background-color: var(--primary);
    color: var(--primary-foreground);
    opacity: 1;

    &:hover, &:focus {
      background-color: var(--primary);
      color: var(--primary-foreground);
    }
  }

  &[data-disabled] {
    color: var(--muted-foreground);
    opacity: 0.5;
  }

  &[data-unavailable] {
    color: var(--destructive-foreground);
    text-decoration: line-through;
  }

  &[data-outside-view] {
    color: var(--muted-foreground);
    opacity: 0.5;

    &[data-selected] {
      background-color: color-mix(in srgb, var(--accent) 50%, transparent);
      color: var(--muted-foreground);
      opacity: 0.3;
    }
  }
}
</style>
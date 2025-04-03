<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { ChevronRight } from 'lucide-vue-next'
import { CalendarNext, type CalendarNextProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarNextProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarNext :class="cn('_calendar-next-component', props.class)" v-bind="forwardedProps">
    <slot>
      <ChevronRight class="_calendar-next-icon" />
    </slot>
  </CalendarNext>
</template>

<style lang="scss" scoped>
._calendar-next-component {
  // From Button.vue (outline)
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-weight-medium);
  transition-property: color, background-color, border-color;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
  cursor: pointer;
  border: 1px solid var(--input);

  width: calc(var(--spacing) * 7);
  height: calc(var(--spacing) * 7);
  background-color: transparent;
  padding: 0;
  opacity: 0.5;

  &:hover {
    background-color: var(--accent);
    color: var(--accent-foreground);
    opacity: 1;
  }
}

._calendar-next-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}
</style>

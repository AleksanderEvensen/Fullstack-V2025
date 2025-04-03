<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { ChevronLeft } from 'lucide-vue-next'
import { CalendarPrev, type CalendarPrevProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarPrevProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarPrev :class="cn('_calendar-prev-component', props.class)" v-bind="forwardedProps">
    <slot>
      <ChevronLeft class="_calendar-prev-icon" />
    </slot>
  </CalendarPrev>
</template>

<style lang="scss" scoped>
._calendar-prev-component {
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

._calendar-prev-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}
</style>

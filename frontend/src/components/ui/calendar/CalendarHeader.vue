<script lang="ts" setup>
import { cn } from '@/lib/utils'
import { CalendarHeader, type CalendarHeaderProps, useForwardProps } from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

const props = defineProps<CalendarHeaderProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <CalendarHeader :class="cn('_calendar-header-component', props.class)" v-bind="forwardedProps">
    <slot />
  </CalendarHeader>
</template>

<style scoped>
._calendar-header-component {
  position: relative;
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
  padding-top: var(--spacing);
}
</style>

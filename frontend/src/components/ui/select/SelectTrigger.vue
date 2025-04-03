<script setup lang="ts">
import { cn } from '@/lib/utils'
import { ChevronDown } from 'lucide-vue-next'
import { SelectIcon, SelectTrigger, type SelectTriggerProps, useForwardProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<SelectTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <SelectTrigger
    v-bind="forwardedProps"
    :class="cn(
      'rounded-md text-sm _select-trigger-component',
      props.class,
    )"
  >
    <slot />
    <SelectIcon as-child>
      <ChevronDown class="_select-trigger-icon" />
    </SelectIcon>
  </SelectTrigger>
</template>

<style scoped lang="scss">
._select-trigger-component {
  display: flex;
  height: calc(var(--spacing) * 10);
  width: 100%;
  align-items: center;
  justify-content: space-between;
  border-style: solid;
  border-width: 1px;
  border-color: var(--input);
  background-color: var(--background);
  
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 3);

  text-align: start;

  &[data-placeholder] {
    color: var(--muted-foreground);
  }

  &:focus {
    outline: none;
    // focus:ring-2 focus:ring-ring focus:ring-offset-2
  }
  &:disabled {
    cursor: not-allowed;
    opacity: 0.5;
  }
  & > span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

._select-trigger-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
  opacity: 0.5;
  flex-shrink: 0;
}
</style>

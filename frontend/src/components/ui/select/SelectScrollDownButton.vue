<script setup lang="ts">
import { cn } from '@/lib/utils'
import { ChevronDown } from 'lucide-vue-next'
import { SelectScrollDownButton, type SelectScrollDownButtonProps, useForwardProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<SelectScrollDownButtonProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <SelectScrollDownButton v-bind="forwardedProps" :class="cn('_select-scroll-button-component', props.class)">
    <slot>
      <ChevronDown class="_select-scroll-icon-component" />
    </slot>
  </SelectScrollDownButton>
</template>

<style scoped>
._select-scroll-button-component {
  display: flex;
  cursor: default;
  align-items: center;
  justify-content: center;
  padding-top: var(--spacing);
  padding-bottom: var(--spacing);
}

._select-scroll-icon-component {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}
</style>

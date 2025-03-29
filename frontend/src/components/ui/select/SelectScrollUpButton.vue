<script setup lang="ts">
import { cn } from '@/lib/utils'
import { ChevronUp } from 'lucide-vue-next'
import { SelectScrollUpButton, type SelectScrollUpButtonProps, useForwardProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<SelectScrollUpButtonProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <SelectScrollUpButton v-bind="forwardedProps" :class="cn('select-scroll-button', props.class)">
    <slot>
      <ChevronUp class="select-scroll-icon" />
    </slot>
  </SelectScrollUpButton>
</template>

<style scoped>
.select-scroll-button {
  display: flex;
  cursor: default;
  align-items: center;
  justify-content: center;
  padding: calc(var(--spacing) * 1) 0;
}

.select-scroll-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}
</style>

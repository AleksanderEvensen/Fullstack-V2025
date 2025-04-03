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
      'rounded-md text-sm data-[placeholder]:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 [&>span]:truncate text-start',
      props.class,
    )"
  >
    <slot />
    <SelectIcon as-child>
      <ChevronDown class="w-4 h-4 opacity-50 shrink-0" />
    </SelectIcon>
  </SelectTrigger>
</template>

<style scoped>
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
}


</style>

<style scoped>
.select-trigger {
  display: flex;
  height: calc(var(--spacing) * 9);
  width: 100%;
  align-items: center;
  justify-content: space-between;
  white-space: nowrap;
  border-radius: var(--radius);
  border: 1px solid var(--input);
  background: transparent;
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 3);
  font-size: var(--font-size-sm);
  box-shadow: var(--shadow-sm);
  text-align: start;
}

.select-trigger:focus {
  outline: none;
}

.select-trigger:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.select-trigger > span {
  overflow: hidden;
  text-overflow: ellipsis;
}

.select-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
  opacity: 0.5;
  flex-shrink: 0;
}
</style>

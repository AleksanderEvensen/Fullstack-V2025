<script setup lang="ts">
import { cn } from '@/lib/utils'
import { Check } from 'lucide-vue-next'
import {
  SelectItem,
  SelectItemIndicator,
  type SelectItemProps,
  SelectItemText,
  useForwardProps,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<SelectItemProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <SelectItem v-bind="forwardedProps" :class="cn('select-item', props.class)">
    <span class="select-item-indicator">
      <SelectItemIndicator>
        <Check class="select-item-check" />
      </SelectItemIndicator>
    </span>

    <SelectItemText>
      <slot />
    </SelectItemText>
  </SelectItem>
</template>

<style scoped>
.select-item {
  position: relative;
  display: flex;
  width: 100%;
  cursor: default;
  user-select: none;
  align-items: center;
  border-radius: var(--radius-sm);
  padding: calc(var(--spacing) * 1.5) calc(var(--spacing) * 2);
  padding-right: calc(var(--spacing) * 8);
  font-size: var(--font-size-sm);
  outline: none;
}

.select-item:focus {
  background: var(--accent);
  color: var(--accent-foreground);
}

.select-item[data-disabled] {
  pointer-events: none;
  opacity: 0.5;
}

.select-item-indicator {
  position: absolute;
  right: calc(var(--spacing) * 2);
  display: flex;
  height: calc(var(--spacing) * 3.5);
  width: calc(var(--spacing) * 3.5);
  align-items: center;
  justify-content: center;
}

.select-item-check {
  height: calc(var(--spacing) * 4);
  width: calc(var(--spacing) * 4);
}
</style>

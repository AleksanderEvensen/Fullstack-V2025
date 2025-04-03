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
  <SelectItem
    v-bind="forwardedProps"
    :class="
      cn(
        'rounded-sm text-sm _select-item-component',
        props.class,
      )
    "
  >
    <span class="_select-item-indicator-component">
      <SelectItemIndicator>
        <Check class="_select-item-indicator-check-component" />
      </SelectItemIndicator>
    </span>

    <SelectItemText>
      <slot />
    </SelectItemText>
  </SelectItem>
</template>

<style scoped lang="scss">
  ._select-item-component {
    position: relative;
    display: flex;
    width: 100%;
    cursor: default;
    user-select: none;
    align-items: center;

    padding-top: calc(var(--spacing) * 1.5);
    padding-bottom: calc(var(--spacing) * 1.5);
    padding-left: calc(var(--spacing) * 8);
    padding-right: calc(var(--spacing) * 2);

    outline: none;

    &:focus {
      background-color: var(--accent);
      color: var(--accent-foreground);
    }
    &[data-disabled] {
      pointer-events: none;
      opacity: 0.5;
    }
  }

  ._select-item-indicator-component {
    position: absolute;
    left: calc(var(--spacing) * 2);
    display: flex;
    height: calc(var(--spacing) * 3.5);
    width: calc(var(--spacing) * 3.5);
    align-items: center;
    justify-content: center;
  }

  ._select-item-indicator-check-component {
    height: calc(var(--spacing) * 4);
    width: calc(var(--spacing) * 4);
  }
</style>

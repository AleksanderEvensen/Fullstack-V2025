<script setup lang="ts">
import { cn } from '@/lib/utils'
import { Circle } from 'lucide-vue-next'
import {
  DropdownMenuItemIndicator,
  DropdownMenuRadioItem,
  type DropdownMenuRadioItemEmits,
  type DropdownMenuRadioItemProps,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuRadioItemProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<DropdownMenuRadioItemEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <DropdownMenuRadioItem v-bind="forwarded" :class="cn('dropdown-menu-radio-item', props.class)">
    <span class="dropdown-menu-radio-indicator">
      <DropdownMenuItemIndicator>
        <Circle class="dropdown-menu-circle-icon" />
      </DropdownMenuItemIndicator>
    </span>
    <slot />
  </DropdownMenuRadioItem>
</template>

<style lang="css" scoped>
.dropdown-menu-radio-item {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  padding: calc(var(--spacing) * 1.5) calc(var(--spacing) * 8) calc(var(--spacing) * 1.5) calc(var(--spacing) * 2);
  font-size: 14px;
  color: var(--foreground);
  background: none;
  border: none;
  border-radius: calc(var(--radius) - 4px);
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  text-align: left;
  user-select: none;
}

.dropdown-menu-radio-item:hover:not(:disabled) {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-radio-item:focus {
  outline: none;
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-radio-item:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.dropdown-menu-radio-indicator {
  position: absolute;
  left: calc(var(--spacing) * 2);
  display: flex;
  align-items: center;
  justify-content: center;
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

.dropdown-menu-circle-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
  fill: currentColor;
}
</style>

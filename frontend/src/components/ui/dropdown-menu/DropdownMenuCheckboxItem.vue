<script setup lang="ts">
import { cn } from '@/lib/utils'
import { Check } from 'lucide-vue-next'
import {
  DropdownMenuCheckboxItem,
  type DropdownMenuCheckboxItemEmits,
  type DropdownMenuCheckboxItemProps,
  DropdownMenuItemIndicator,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuCheckboxItemProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<DropdownMenuCheckboxItemEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <DropdownMenuCheckboxItem v-bind="forwarded" :class="cn('dropdown-menu-checkbox-item', props.class)">
    <span class="dropdown-menu-checkbox-indicator">
      <DropdownMenuItemIndicator>
        <Check class="dropdown-menu-check-icon" />
      </DropdownMenuItemIndicator>
    </span>
    <slot />
  </DropdownMenuCheckboxItem>
</template>

<style lang="css" scoped>
.dropdown-menu-checkbox-item {
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

.dropdown-menu-checkbox-item:hover:not(:disabled) {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-checkbox-item:focus {
  outline: none;
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.dropdown-menu-checkbox-item:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.dropdown-menu-checkbox-indicator {
  position: absolute;
  left: calc(var(--spacing) * 2);
  display: flex;
  align-items: center;
  justify-content: center;
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

.dropdown-menu-check-icon {
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}
</style>

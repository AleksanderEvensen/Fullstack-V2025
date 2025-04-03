<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  SelectContent,
  type SelectContentEmits,
  type SelectContentProps,
  SelectPortal,
  SelectViewport,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'
import { SelectScrollDownButton, SelectScrollUpButton } from '.'

defineOptions({
  inheritAttrs: false,
})

const props = withDefaults(
  defineProps<SelectContentProps & { class?: HTMLAttributes['class'] }>(),
  {
    position: 'popper',
  },
)
const emits = defineEmits<SelectContentEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <SelectPortal>
    <SelectContent v-bind="{ ...forwarded, ...$attrs }" :class="cn('rounded-md select-content', props.class)">
      <SelectScrollUpButton />
      <SelectViewport class="select-viewport">
        <slot />
      </SelectViewport>
      <SelectScrollDownButton />
    </SelectContent>
  </SelectPortal>
</template>

<style>
.select-content {
  position: relative;
  z-index: 150;
  max-height: calc(var(--spacing) * 96);
  min-width: calc(var(--spacing) * 32);
  overflow: hidden;
  border: 1px solid var(--border);
  background: var(--popover);
  color: var(--popover-foreground);
  /* box-shadow: var(--shadow-md); */
}

.select-content[data-state='open'] {
  animation: selectIn 0.2s ease-out;
}

.select-content[data-state='closed'] {
  animation: selectOut 0.2s ease-in;
}

.select-content[data-side='bottom'] {
  transform: translateY(calc(var(--spacing) * 1));
}

.select-content[data-side='top'] {
  transform: translateY(calc(var(--spacing) * -1));
}

.select-content[data-side='left'] {
  transform: translateX(calc(var(--spacing) * -1));
}

.select-content[data-side='right'] {
  transform: translateX(calc(var(--spacing) * 1));
}

.select-viewport {
  height: var(--reka-select-trigger-height);
  width: 100%;
  min-width: var(--reka-select-trigger-width);
  padding: calc(var(--spacing) * 1);
}

@keyframes selectIn {
  from {
    opacity: 0;
    transform: translateY(calc(var(--spacing) * -2));
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes selectOut {
  from {
    opacity: 1;
    transform: translateY(0);
  }

  to {
    opacity: 0;
    transform: translateY(calc(var(--spacing) * -2));
  }
}
</style>

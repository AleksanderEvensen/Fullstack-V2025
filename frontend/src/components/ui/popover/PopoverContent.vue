<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  PopoverContent,
  type PopoverContentEmits,
  type PopoverContentProps,
  PopoverPortal,
  useForwardPropsEmits,
} from 'reka-ui'
import { type HTMLAttributes, computed } from 'vue'

defineOptions({
  inheritAttrs: false,
})

const props = withDefaults(
  defineProps<PopoverContentProps & { class?: HTMLAttributes['class'] }>(),
  {
    align: 'center',
    sideOffset: 4,
  },
)
const emits = defineEmits<PopoverContentEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <PopoverPortal>
    <PopoverContent
      v-bind="{ ...forwarded, ...$attrs }"
      :class="cn('_popover-content-component', props.class)"
    >
      <slot />
    </PopoverContent>
  </PopoverPortal>
</template>

<style>
._popover-content-component {
  z-index: 50;
  width: 18rem;
  border-radius: 0.375rem;
  border: 1px solid var(--border);
  background-color: var(--popover);
  color: var(--popover-foreground);
  padding: 1rem;
  box-shadow:
    0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
  outline: none;
}

/* Animation states */
._popover-content-component[data-state='open'] {
  animation:
    fadeIn 0.2s ease-out,
    zoomIn 0.2s ease-out;
}

._popover-content-component[data-state='closed'] {
  animation:
    fadeOut 0.2s ease-in,
    zoomOut 0.2s ease-in;
}

/* Positioning animations */
._popover-content-component[data-side='bottom'] {
  transform-origin: top;
  animation: slideInFromTop 0.2s ease-out;
}

._popover-content-component[data-side='top'] {
  transform-origin: bottom;
  animation: slideInFromBottom 0.2s ease-out;
}

._popover-content-component[data-side='left'] {
  transform-origin: right;
  animation: slideInFromRight 0.2s ease-out;
}

._popover-content-component[data-side='right'] {
  transform-origin: left;
  animation: slideInFromLeft 0.2s ease-out;
}

/* Animations */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

@keyframes zoomIn {
  from {
    transform: scale(0.95);
  }
  to {
    transform: scale(1);
  }
}

@keyframes zoomOut {
  from {
    transform: scale(1);
  }
  to {
    transform: scale(0.95);
  }
}

@keyframes slideInFromTop {
  from {
    transform: translateY(-0.5rem);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideInFromBottom {
  from {
    transform: translateY(0.5rem);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideInFromLeft {
  from {
    transform: translateX(-0.5rem);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes slideInFromRight {
  from {
    transform: translateX(0.5rem);
  }
  to {
    transform: translateX(0);
  }
}
</style>

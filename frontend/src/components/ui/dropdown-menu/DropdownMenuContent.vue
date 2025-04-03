<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  DropdownMenuContent,
  type DropdownMenuContentEmits,
  type DropdownMenuContentProps,
  useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<DropdownMenuContentProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<DropdownMenuContentEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
  <DropdownMenuContent v-bind="forwarded" :class="cn('dropdown-menu-content', props.class)">
    <slot />
  </DropdownMenuContent>
</template>

<style lang="css">
.dropdown-menu-content {
  z-index: 100;
  min-width: 8rem;
  padding: calc(var(--spacing) * 1);
  margin: calc(var(--spacing) * 1) 0;
  background-color: var(--popover);
  border: 1px solid var(--border);
  border-radius: calc(var(--radius) - 2px);
  color: var(--popover-foreground);
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* Animation states */
.dropdown-menu-content[data-state='open'] {
  animation: contentShow 0.2s ease-out;
}

.dropdown-menu-content[data-state='closed'] {
  animation: contentHide 0.2s ease-in;
}

/* Side-based animations */
.dropdown-menu-content[data-side='bottom'] {
  animation-name: slideFromTop;
}

.dropdown-menu-content[data-side='top'] {
  animation-name: slideFromBottom;
}

.dropdown-menu-content[data-side='left'] {
  animation-name: slideFromRight;
}

.dropdown-menu-content[data-side='right'] {
  animation-name: slideFromLeft;
}

@keyframes contentShow {
  from {
    opacity: 0;
    transform: scale(0.95);
  }

  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes contentHide {
  from {
    opacity: 1;
    transform: scale(1);
  }

  to {
    opacity: 0;
    transform: scale(0.95);
  }
}

@keyframes slideFromTop {
  from {
    opacity: 0;
    transform: translateY(-0.5rem);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideFromBottom {
  from {
    opacity: 0;
    transform: translateY(0.5rem);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideFromLeft {
  from {
    opacity: 0;
    transform: translateX(-0.5rem);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideFromRight {
  from {
    opacity: 0;
    transform: translateX(0.5rem);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>

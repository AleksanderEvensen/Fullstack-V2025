<script setup lang="ts">
import { DropdownMenuSubContent } from 'reka-ui'
import { cn } from "@/lib/utils"

interface Props {
  class?: string
  sideOffset?: number
  alignOffset?: number
  loop?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  class: '',
  sideOffset: 2,
  alignOffset: 0,
  loop: false
})
</script>

<template>
  <DropdownMenuSubContent :sideOffset="sideOffset" :alignOffset="alignOffset" :loop="loop"
    :class="cn('dropdown-menu-sub-content', props.class)">
    <slot />
  </DropdownMenuSubContent>
</template>

<style lang="css">
.dropdown-menu-sub-content {
  position: relative;
  z-index: 50;
  min-width: 8rem;
  padding: calc(var(--spacing) * 1);
  background-color: var(--popover);
  border: 1px solid var(--border);
  border-radius: calc(var(--radius) - 2px);
  color: var(--popover-foreground);
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* Animation states */
.dropdown-menu-sub-content[data-state="open"] {
  animation: subContentShow 0.2s ease-out;
}

.dropdown-menu-sub-content[data-state="closed"] {
  animation: subContentHide 0.2s ease-in;
}

/* Side-based animations */
.dropdown-menu-sub-content[data-side="right"] {
  animation-name: slideFromLeft;
}

.dropdown-menu-sub-content[data-side="left"] {
  animation-name: slideFromRight;
}

@keyframes subContentShow {
  from {
    opacity: 0;
    transform: scale(0.95);
  }

  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes subContentHide {
  from {
    opacity: 1;
    transform: scale(1);
  }

  to {
    opacity: 0;
    transform: scale(0.95);
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

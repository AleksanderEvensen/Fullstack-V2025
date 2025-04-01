<script setup lang="ts">
import { cn } from '@/lib/utils'
import { X } from 'lucide-vue-next'
import {
  DialogClose,
  DialogContent,
  type DialogContentEmits,
  type DialogContentProps,
  DialogOverlay,
  DialogPortal,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'
import { type SheetVariants, sheetVariants } from '.'

interface SheetContentProps extends DialogContentProps {
  class?: HTMLAttributes['class']
  side?: SheetVariants['side']
}

defineOptions({
  inheritAttrs: false,
})

const props = defineProps<SheetContentProps>()
const emits = defineEmits<DialogContentEmits>()

const delegatedProps = computed(() => {
  const { class: _, side, ...delegated } = props
  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <DialogPortal>
    <DialogOverlay class="sheet-overlay" />
    <DialogContent :class="cn(sheetVariants({ side }), $attrs.class ?? '')" v-bind="{ ...forwarded, ...$attrs }">
      <slot />

      <DialogClose class="sheet-close">
        <X class="sheet-close-icon" />
      </DialogClose>
    </DialogContent>
  </DialogPortal>
</template>

<style lang="css">
.sheet-overlay {
  position: fixed;
  inset: 0;
  z-index: 100;
  background-color: var(--overlay);
  backdrop-filter: blur(4px);
  transition: opacity 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-overlay[data-state="open"] {
  animation: fade-in 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-overlay[data-state="closed"] {
  animation: fade-out 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-content {
  position: fixed;
  z-index: 101;
  gap: calc(var(--spacing) * 4);
  background-color: var(--background);
  padding: calc(var(--spacing) * 6);
  box-shadow: var(--shadow-lg);
  transition: all 150ms cubic-bezier(0.4, 0, 0.2, 1);
  width: 100%;
}

.sheet-content-top {
  inset-inline: 0;
  top: 0;
  border-bottom: 1px solid var(--border);
  height: auto;
}

.sheet-content-bottom {
  inset-inline: 0;
  bottom: 0;
  border-top: 1px solid var(--border);
  height: auto;
}

.sheet-content-left {
  inset-block: calc(var(--spacing) * 0);
  left: 0;
  height: 100%;
  width: 75%;
  border-right: 1px solid var(--border);
}

.sheet-content-right {
  inset-block: calc(var(--spacing) * 0);
  right: 0;
  height: 100%;
  width: 75%;
  border-left: 1px solid var(--border);
}

@media (min-width: 640px) {

  .sheet-content-left,
  .sheet-content-right {
    max-width: 24rem;
  }
}

.sheet-close {
  position: absolute;
  right: calc(var(--spacing) * 4);
  top: calc(var(--spacing) * 4);
  border-radius: var(--radius);
  opacity: 0.7;
  transition: opacity 150ms cubic-bezier(0.4, 0, 0.2, 1);
  padding: calc(var(--spacing) * 2);
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.sheet-close:hover {
  opacity: 1;
}

.sheet-close:focus {
  outline: none;
  box-shadow: var(--ring-offset) var(--ring);
  box-shadow-offset: 2px;
}

.sheet-close-icon {
  width: var(--icon-size);
  height: var(--icon-size);
  cursor: pointer;
}

@keyframes fade-in {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes fade-out {
  from {
    opacity: 1;
  }

  to {
    opacity: 0;
  }
}

.sheet-content[data-state="open"] {
  animation: sheet-in 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-content[data-state="closed"] {
  animation: sheet-out 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes sheet-in {
  from {
    opacity: 0;
    transform: translateX(100%);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes sheet-out {
  from {
    opacity: 1;
    transform: translateX(0);
  }

  to {
    opacity: 0;
    transform: translateX(100%);
  }
}

.sheet-content-top[data-state="open"] {
  animation: sheet-in-top 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-content-top[data-state="closed"] {
  animation: sheet-out-top 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes sheet-in-top {
  from {
    opacity: 0;
    transform: translateY(-100%);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes sheet-out-top {
  from {
    opacity: 1;
    transform: translateY(0);
  }

  to {
    opacity: 0;
    transform: translateY(-100%);
  }
}

.sheet-content-bottom[data-state="open"] {
  animation: sheet-in-bottom 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-content-bottom[data-state="closed"] {
  animation: sheet-out-bottom 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes sheet-in-bottom {
  from {
    opacity: 0;
    transform: translateY(100%);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes sheet-out-bottom {
  from {
    opacity: 1;
    transform: translateY(0);
  }

  to {
    opacity: 0;
    transform: translateY(100%);
  }
}

.sheet-content-left[data-state="open"] {
  animation: sheet-in-left 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

.sheet-content-left[data-state="closed"] {
  animation: sheet-out-left 150ms cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes sheet-in-left {
  from {
    opacity: 0;
    transform: translateX(-100%);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes sheet-out-left {
  from {
    opacity: 1;
    transform: translateX(0);
  }

  to {
    opacity: 0;
    transform: translateX(-100%);
  }
}
</style>

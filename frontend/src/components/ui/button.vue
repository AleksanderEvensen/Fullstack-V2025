<script setup lang="ts">
import { cn } from "@/lib/utils";
import { cva } from 'class-variance-authority'

const buttonVariants = cva(
  'button rounded-md text-sm',
  {
    variants: {
      variant: {
        default: 'button-default',
        destructive: 'button-destructive',
        outline: 'button-outline',
        secondary: 'button-secondary',
        ghost: 'button-ghost',
        link: 'button-link',
      },
      size: {
        default: 'button-size-default',
        sm: 'rounded-md button-size-sm',
        lg: 'rounded-md button-size-lg',
        icon: 'button-size-icon',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  },
)

interface Props {
  variant?: NonNullable<Parameters<typeof buttonVariants>[0]>['variant']
  size?: NonNullable<Parameters<typeof buttonVariants>[0]>['size']
  as?: string
}

// eslint-disable-next-line vue/define-macros-order
withDefaults(defineProps<Props>(), {
  as: 'button',
})
</script>

<template>
  <component
    :is="as"
    :class="cn(buttonVariants({ variant, size }), $attrs.class ?? '')"
  >
    <slot />
  </component>
</template>


<style lang="css" scoped>
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-weight-medium);
  transition-property: color, background-color, border-color;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
  cursor: pointer;
}

.button:focus-visible {
  outline: none;
  box-shadow: 0 0 0 2px var(--ring), 0 0 0 4px var(--ring-offset, white);
}

.button:disabled {
  pointer-events: none;
  opacity: 0.5;
}

/* Variants */
.button-default {
  background-color: var(--primary);
  color: var(--primary-foreground);
}

.button-default:hover {
  background-color: color-mix(in srgb, var(--primary) 90%, white);
}

.button-destructive {
  background-color: var(--destructive);
  color: var(--destructive-foreground);
}
.button-destructive:hover {
  background-color: color-mix(in srgb, var(--destructive) 90%, white);
}

.button-outline {
  border: 1px solid var(--input);
  background-color: var(--background);
}
.button-outline:hover {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.button-secondary {
  background-color: var(--secondary);
  color: var(--secondary-foreground);
}
.button-secondary:hover {
  background-color: color-mix(in srgb, var(--secondary) 90%, black);
}

.button-ghost:hover {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.button-link {
  color: var(--primary);
  text-decoration: none;
  text-underline-offset: 4px;
}
.button-link:hover {
  text-decoration: underline;
}

/* Size styles */
.button-size-default {
  height: calc(var(--spacing) * 10); /* h-10 */
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 4); /* px-4 py-2 */
}

.button-size-sm {
  height: calc(var(--spacing) * 9); /* h-9 */
  padding: 0 calc(var(--spacing) * 3); /* px-3 */
}

.button-size-lg {
  height: calc(var(--spacing) * 11); /* h-11 */
  padding: 0 calc(var(--spacing) * 8); /* px-8 */
}

.button-size-icon {
  height: calc(var(--spacing) * 10); /* h-10 */
  width: calc(var(--spacing) * 10); /* w-10 */
}
</style>

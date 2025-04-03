<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { AvatarRoot } from 'reka-ui'

const props = withDefaults(
  defineProps<{
    class?: HTMLAttributes['class']
    size?: 'sm' | 'base' | 'lg'
    shape?: 'circle' | 'square'
  }>(),
  {
    size: 'sm',
    shape: 'circle',
  },
)
</script>

<template>
  <AvatarRoot :class="cn('avatar', `avatar-${size}`, `avatar-${shape}`, props.class)">
    <slot />
  </AvatarRoot>
</template>

<style scoped>
.avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-weight-normal);
  color: var(--foreground);
  user-select: none;
  flex-shrink: 0;
  background-color: var(--secondary);
  overflow: hidden;
}

.avatar-sm {
  height: calc(var(--spacing) * 10);
  width: calc(var(--spacing) * 10);
  font-size: var(--font-size-xs);
}

.avatar-base {
  height: calc(var(--spacing) * 16);
  width: calc(var(--spacing) * 16);
  font-size: var(--font-size-2xl);
}

.avatar-lg {
  height: calc(var(--spacing) * 32);
  width: calc(var(--spacing) * 32);
  font-size: var(--font-size-5xl);
}

.avatar-circle {
  border-radius: 50%;
}

.avatar-square {
  border-radius: var(--radius);
}
</style>

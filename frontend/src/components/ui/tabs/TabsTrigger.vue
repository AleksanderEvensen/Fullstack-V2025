<script setup lang="ts">
import { cn } from '@/lib/utils'
import { TabsTrigger, type TabsTriggerProps, useForwardProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<TabsTriggerProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})

const forwardedProps = useForwardProps(delegatedProps)
</script>

<template>
  <TabsTrigger v-bind="forwardedProps" :class="cn('tabs-trigger', props.class)">
    <span class="tabs-trigger-text">
      <slot />
    </span>
  </TabsTrigger>
</template>

<style>
.tabs-trigger {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  white-space: nowrap;
  border-radius: calc(var(--radius) - 2px);
  padding: calc(var(--spacing) * 1) calc(var(--spacing) * 3);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--muted-foreground);
  transition: all 0.2s ease;
}

.tabs-trigger:focus-visible {
  outline: none;
  ring: 2px solid var(--ring);
  ring-offset: 2px;
  ring-offset-color: var(--background);
}

.tabs-trigger:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.tabs-trigger[data-state="active"] {
  background-color: var(--background);
  color: var(--foreground);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.tabs-trigger-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import { TabsList, type TabsListProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<TabsListProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})
</script>

<template>
  <TabsList v-bind="delegatedProps" :class="cn('tabs-list', props.class)">
    <slot />
  </TabsList>
</template>

<style>
.tabs-list {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius);
  background-color: var(--muted);
  padding: calc(var(--spacing) * 1);
  color: var(--muted-foreground);
}
</style>

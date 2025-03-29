<script setup lang="ts">
import { cn } from '@/lib/utils'
import { TabsContent, type TabsContentProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<TabsContentProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})
</script>

<template>
  <TabsContent :class="cn('tabs-content', props.class)" v-bind="delegatedProps">
    <slot />
  </TabsContent>
</template>

<style>
.tabs-content {
  margin-top: calc(var(--spacing) * 2);
}

.tabs-content:focus-visible {
  outline: none;
  ring: 2px solid var(--ring);
  ring-offset: 2px;
  ring-offset-color: var(--background);
}
</style>

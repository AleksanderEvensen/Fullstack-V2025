<script setup lang="ts">
import { cn } from '@/lib/utils'
import { SelectSeparator, type SelectSeparatorProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<SelectSeparatorProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})
</script>

<template>
  <SelectSeparator v-bind="delegatedProps" :class="cn('_select-separator-component', props.class)" />
</template>

<style scoped>
._select-separator-component {
  height: 1px;
  background-color: var(--muted);
  margin: var(--spacing) calc(var(--spacing) * -1);
}
</style>

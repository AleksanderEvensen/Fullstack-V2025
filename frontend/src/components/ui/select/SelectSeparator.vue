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
  <SelectSeparator v-bind="delegatedProps" :class="cn('select-separator', props.class)" />
</template>

<style scoped>
.select-separator {
  margin: calc(var(--spacing) * 1) calc(var(--spacing) * -1);
  height: 1px;
  background-color: var(--muted);
}
</style>

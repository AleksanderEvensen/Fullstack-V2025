<script setup lang="ts">
import { cn } from '@/lib/utils'
import { Label, type LabelProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<LabelProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})
</script>

<template>
  <Label v-bind="delegatedProps" :class="cn('label', props.class)">
    <slot />
  </Label>
</template>

<style scoped>
.label {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  line-height: 1;
  color: var(--foreground);
  cursor: default;
}

.label:has(+ .peer:disabled),
.label:has(+ input:disabled),
.label:has(+ textarea:disabled),
.label:has(+ select:disabled) {
  cursor: not-allowed;
  opacity: 0.7;
}
</style>

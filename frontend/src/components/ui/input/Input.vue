<script setup lang="ts">
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { useVModel } from '@vueuse/core'

const props = defineProps<{
  defaultValue?: string | number
  modelValue?: string | number
  class?: HTMLAttributes['class']
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', payload: string | number): void
}>()

const modelValue = useVModel(props, 'modelValue', emits, {
  passive: true,
  defaultValue: props.defaultValue,
})
</script>

<template>
  <input v-model="modelValue" :class="cn('input', props.class)" />
</template>

<style scoped>
.input {
  display: flex;
  width: 100%;
  height: calc(var(--spacing) * 9);
  padding: calc(var(--spacing) * 1) calc(var(--spacing) * 3);
  font-size: var(--font-size-sm);
  background-color: transparent;
  border: 1px solid var(--input);
  border-radius: var(--radius);
  box-shadow: var(--shadow-sm);
  transition: all var(--duration) var(--ease);
}

.input::placeholder {
  color: var(--muted-foreground);
}

.input:focus-visible {
  outline: none;
  box-shadow: 0 0 0 1px var(--ring);
}

.input:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.input[type="file"] {
  border: 2px solid var(--border);
  background: transparent;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

.input[type="file"]::file-selector-button {
  border: 0;
  background: transparent;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}
</style>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import { useVModel } from '@vueuse/core'

const props = defineProps<{
  modelValue?: string | number
  class?: string
}>()

const emits = defineEmits<(e: 'update:modelValue', payload: string | number) => void>()

const modelValue = useVModel(props, 'modelValue', emits, {
  defaultValue: '',
})
</script>

<template>
  <textarea v-model="modelValue" :class="cn('textarea', props.class)" />
</template>

<style scoped>
.textarea {
  display: flex;
  min-height: 60px;
  width: 100%;
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 3);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  background-color: var(--background);
  color: var(--foreground);
  font-size: var(--font-size-sm);
  line-height: 1.5;
  transition: all 0.2s ease;
}

.textarea::placeholder {
  color: var(--muted-foreground);
}

.textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 1px var(--primary);
}

.textarea:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.textarea:read-only {
  background-color: var(--muted);
}
</style>

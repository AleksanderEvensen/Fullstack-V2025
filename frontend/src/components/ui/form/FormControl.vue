<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'
import { useFormField } from './useFormField'

const props = defineProps<{ class?: HTMLAttributes['class'] }>()

const { error, formItemId, formDescriptionId, formMessageId } = useFormField()
</script>

<template>
  <div :class="cn('form-control', props.class)" :data-error="!!error">
    <slot :id="formItemId" :aria-describedby="!error ? formDescriptionId : `${formDescriptionId} ${formMessageId}`"
      :aria-invalid="!!error" />
  </div>
</template>

<style>
.form-control {
  display: flex;
  width: 100%;
}

.form-control[data-error="true"] :deep(input),
.form-control[data-error="true"] :deep(textarea),
.form-control[data-error="true"] :deep(select) {
  border-color: var(--destructive);
}

.form-control[data-error="true"] :deep(input:focus),
.form-control[data-error="true"] :deep(textarea:focus),
.form-control[data-error="true"] :deep(select:focus) {
  ring-color: var(--destructive);
}
</style>

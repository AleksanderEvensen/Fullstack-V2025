<script lang="ts" setup>
import type { HTMLAttributes } from 'vue'
import { cn } from '@/lib/utils'
import { useFormField } from './useFormField'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()

const { formDescriptionId } = useFormField()
</script>

<template>
  <p :id="formDescriptionId" :class="cn('form-description', props.class)">
    <slot />
  </p>
</template>

<style>
.form-description {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
  margin-top: calc(var(--spacing) * 1);
}
</style>

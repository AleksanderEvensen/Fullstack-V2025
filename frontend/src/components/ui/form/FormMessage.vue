<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'
import { useFormField } from './useFormField'

const props = defineProps<{ class?: HTMLAttributes['class'] }>()

const { error, formMessageId } = useFormField()
</script>

<template>
  <p v-if="error" :id="formMessageId" :class="cn('form-message', props.class)">
    {{ error }}
  </p>
</template>

<style>
.form-message {
  font-size: var(--font-size-sm);
  color: var(--destructive);
  margin-top: calc(var(--spacing) * 1);
}
</style>

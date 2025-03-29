<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'
import { provide } from 'vue'
import { FORM_ITEM_INJECTION_KEY } from './injectionKeys'

const props = defineProps<{ class?: HTMLAttributes['class'] }>()

provide(FORM_ITEM_INJECTION_KEY, crypto.randomUUID())
</script>

<template>
  <div :class="cn('form-item', props.class)">
    <slot />
  </div>
</template>

<style>
.form-item {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}
</style>

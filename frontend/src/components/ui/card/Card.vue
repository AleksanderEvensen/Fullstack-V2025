<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { HTMLAttributes } from 'vue'

const props = defineProps<{
  class?: HTMLAttributes['class']
}>()
</script>

<template>
  <div
    :class="cn('rounded _card-component', props.class)"
  >
    <slot />
  </div>
</template>

<style scoped>

._card-component {
  border-width: 1px;
  background-color: var(--card);
  color: var(--card-foreground);
  box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
}

</style>
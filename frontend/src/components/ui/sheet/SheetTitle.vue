<script setup lang="ts">
import { cn } from '@/lib/utils'
import { DialogTitle, type DialogTitleProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<DialogTitleProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})
</script>

<template>
  <DialogTitle :class="cn('sheet-title', $attrs.class ?? '')" v-bind="delegatedProps">
    <slot />
  </DialogTitle>
</template>

<style lang="css" scoped>
.sheet-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--foreground);
  line-height: var(--line-height-normal);
}
</style>

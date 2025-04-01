<script setup lang="ts">
import { cn } from '@/lib/utils'
import { DialogDescription, type DialogDescriptionProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<DialogDescriptionProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props
  return delegated
})
</script>

<template>
  <DialogDescription :class="cn('sheet-description', $attrs.class ?? '')" v-bind="delegatedProps">
    <slot />
  </DialogDescription>
</template>

<style lang="css" scoped>
.sheet-description {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
  line-height: var(--line-height-normal);
}
</style>

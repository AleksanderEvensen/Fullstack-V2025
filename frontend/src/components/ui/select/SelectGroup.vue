<script setup lang="ts">
import { cn } from '@/lib/utils'
import { SelectGroup, type SelectGroupProps } from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<SelectGroupProps & { class?: HTMLAttributes['class'] }>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})
</script>

<template>
  <SelectGroup :class="cn('_select-group-component', props.class)" v-bind="delegatedProps">
    <slot />
  </SelectGroup>
</template>

<style scoped>
._select-group-component {
  padding: var(--spacing-1);
  width: 100%;
}
</style>
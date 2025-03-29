<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  SwitchRoot,
  type SwitchRootEmits,
  type SwitchRootProps,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

defineOptions({
  inheritAttrs: false,
})

const props = defineProps<SwitchRootProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<SwitchRootEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <SwitchRoot v-bind="{ ...forwarded, ...$attrs }" :class="cn('switch-root', props.class)">
    <span class="switch-thumb" />
  </SwitchRoot>
</template>

<style>
.switch-root {
  display: inline-flex;
  width: calc(var(--spacing) * 11);
  height: calc(var(--spacing) * 6);
  cursor: pointer;
  align-items: center;
  border-radius: var(--radius);
  border: 2px solid var(--input);
  background-color: var(--input);
  padding: calc(var(--spacing) * 0.5);
  transition: background-color 0.2s, border-color 0.2s;
}

.switch-root:focus-visible {
  outline: none;
  box-shadow: 0 0 0 2px var(--background), 0 0 0 4px var(--ring);
}

.switch-root[data-state="checked"] {
  background-color: var(--primary);
  border-color: var(--primary);
}

.switch-root[data-disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}

.switch-thumb {
  display: block;
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
  border-radius: var(--radius);
  background-color: var(--background);
  transition: transform 0.2s;
  will-change: transform;
}

.switch-root[data-state="checked"] .switch-thumb {
  transform: translateX(calc(var(--spacing) * 5));
}
</style>

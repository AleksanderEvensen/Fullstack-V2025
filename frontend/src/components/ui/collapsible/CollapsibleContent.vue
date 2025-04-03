<script setup lang="ts">
import { CollapsibleContent, type CollapsibleContentProps } from 'reka-ui'

const props = defineProps<CollapsibleContentProps>()
</script>

<template>
  <CollapsibleContent v-bind="props" class="_collapsible-content-component">
    <slot />
  </CollapsibleContent>
</template>

<style lang="scss" scoped>
  ._collapsible-content-component {
    overflow: hidden;
    transition: 0.2s;
    
    &[data-state='closed'] {
      animation: collapsible-up 0.2s ease-out;
    }

    &[data-state='open'] {
      animation: collapsible-down 0.2s ease-out;
    }
  }
  
  @keyframes collapsible-up {
    0% {
      height: var(--reka-collapsible-content-height);
    }
    to {
      height: 0;
    }
  }
  @keyframes collapsible-down {
    0% {
      height: 0;
    }
    to {
      height: var(--reka-collapsible-content-height);
    }
  }
</style>
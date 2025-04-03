<script setup lang="ts">
import { CheckboxIndicator, CheckboxRoot } from 'reka-ui'
import { CheckIcon } from 'lucide-vue-next'
</script>

<template>
  <CheckboxRoot class="checkbox rounded-xs">
    <CheckboxIndicator class="checkbox-indicator">
      <CheckIcon class="checkbox-icon" />
    </CheckboxIndicator>
  </CheckboxRoot>
</template>

<style lang="css" scoped>
.checkbox {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
  border: 1px solid var(--input);
  background-color: var(--background);
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  overflow: hidden;
}

.checkbox:focus-visible {
  outline: none;
  box-shadow:
    0 0 0 2px var(--ring),
    0 0 0 4px var(--ring-offset, white);
}

.checkbox:disabled {
  pointer-events: none;
  opacity: 0.5;
}

.checkbox-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: white;
  transition: background-color 1s ease-in-out;
}

.checkbox-indicator[data-state='checked'] {
  background-color: black;
}

.checkbox-icon {
  width: calc(var(--spacing) * 3.5);
  height: calc(var(--spacing) * 3.5);
  transform: scale(0);
  transition: transform 1s ease-in-out;
}

.checkbox-indicator[data-state='checked'] .checkbox-icon {
  transform: scale(1);
}
</style>

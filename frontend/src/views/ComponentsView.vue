<script setup lang="ts">
import {
  ButtonTest,
  FormTest,
  AvatarTest,
  DropdownTest,
  TabsTest,
  StepperTest,
  SelectTest,
  SwitchTest,
  NavigationMenuTest,
} from './test-components'
</script>

<template>
  <div class="components-container">
    <h1>Component Examples</h1>
    <NavigationMenuTest />
    <ButtonTest />
    <FormTest />
    <AvatarTest />
    <DropdownTest />
    <TabsTest />
    <StepperTest />
    <SelectTest />
    <SwitchTest />
  </div>
</template>

<style scoped>
.components-container {
  padding: calc(var(--spacing) * 6);
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 8);
}

.components-container h1 {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-bold);
  color: var(--foreground);
  margin-bottom: calc(var(--spacing) * 4);
}
</style>

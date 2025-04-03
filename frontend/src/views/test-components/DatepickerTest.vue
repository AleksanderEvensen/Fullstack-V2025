<script setup lang="ts">
import { Datepicker } from '@/components/ui/datepicker'
</script>

<template>
  <section class="component-section">
    <h2>Datepicker</h2>
    <div class="component-grid">
      <Datepicker />
    </div>
  </section>
</template>

<style scoped>
.component-section {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 6);
}

.component-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: calc(var(--spacing) * 6);
}
</style>

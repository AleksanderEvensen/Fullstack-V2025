<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
</script>

<template>
  <section class="component-section">
    <h2>Buttons</h2>
    <div class="component-grid">
      <div class="component-example">
        <h3>Variants</h3>
        <div class="example-content">
          <Button variant="default">Default</Button>
          <Button variant="secondary">Secondary</Button>
          <Button variant="destructive">Destructive</Button>
          <Button variant="outline">Outline</Button>
          <Button variant="ghost">Ghost</Button>
          <Button variant="link">Link</Button>
        </div>
      </div>

      <div class="component-example">
        <h3>Sizes</h3>
        <div class="example-content">
          <Button size="lg">Large</Button>
          <Button size="default">Default</Button>
          <Button size="sm">Small</Button>
          <Button size="icon">Icon</Button>
        </div>
      </div>
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

.component-example {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
  padding: calc(var(--spacing) * 4);
  background-color: var(--background);
  border: 1px solid var(--border);
  border-radius: var(--radius);
}

.example-content {
  display: flex;
  flex-wrap: wrap;
  gap: calc(var(--spacing) * 2);
  align-items: flex-start;
}
</style>

<script setup lang="ts">
import { Collapsible, CollapsibleTrigger, CollapsibleContent } from '@/components/ui/collapsible'
</script>

<template>
  <section class="component-section">
    <h2>Collapsible</h2>
    <div class="component-grid">
      <Collapsible>
        <CollapsibleTrigger>Can I use this in my project?</CollapsibleTrigger>
        <CollapsibleContent>
          Yes. Free to use for personal and commercial projects. No attribution required.
        </CollapsibleContent>
      </Collapsible>
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

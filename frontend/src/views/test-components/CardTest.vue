<script setup lang="ts">
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
</script>

<template>
  <section class="component-section">
    <h2>Card</h2>
    <div class="component-grid">
      <Card>
        <CardHeader>
          <CardTitle>Card Title</CardTitle>
          <CardDescription>Card Description</CardDescription>
        </CardHeader>
        <CardContent> Card Content </CardContent>
        <CardFooter> Card Footer </CardFooter>
      </Card>
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

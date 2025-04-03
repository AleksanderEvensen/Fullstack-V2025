<template>
  <div class="app-container">
    <Navbar />
    <div class="center-view">
      <RouterView />
    </div>
    <Toaster />
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/Navbar.vue'
import { RouterView } from 'vue-router'
import { Toaster } from 'vue-sonner'
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100dvh;
}

.center-view {
  display: flex;
  flex-direction: column;
  width: 100%;
  align-items: center;  
}
</style>

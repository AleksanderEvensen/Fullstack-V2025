<template>
  <div class="icon-example">
    <h3>Font Awesome Icon Examples</h3>
    <div class="icon-grid">
      <div class="icon-item">
        <FontAwesomeIcon icon="fas fa-circle" />
        <span>Circle</span>
      </div>
      <div class="icon-item">
        <FontAwesomeIcon icon="fas fa-square" />
        <span>Square</span>
      </div>
      <div class="icon-item">
        <FontAwesomeIcon icon="fas fa-star" color="#FFD700" size="2em" />
        <span>Star (Custom Color & Size)</span>
      </div>
      <div class="icon-item">
        <FontAwesomeIcon icon="fas fa-heart" color="red" />
        <span>Heart</span>
      </div>
      <div class="icon-item">
        <FontAwesomeIcon icon="fas fa-check" color="green" />
        <span>Check</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import FontAwesomeIcon from './icons/FontAwesomeIcon.vue'
</script>

<style scoped>
.icon-example {
  padding: 1rem;
  max-width: 800px;
  margin: 0 auto;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem;
  border: 1px solid #eee;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.icon-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>

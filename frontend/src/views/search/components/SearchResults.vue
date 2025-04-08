<script setup lang="ts">
import { ref } from 'vue';
import { Button } from '@/components/ui/button';
import { FunnelIcon, MapPin } from 'lucide-vue-next';
import ItemCard from './ItemCard.vue';

// Define component props with TypeScript
defineProps<{
    results: any[];
    isLoading: boolean;
    totalItems: number;
}>();

// Define emits for parent component communication
const emit = defineEmits<{
    toggleFilters: [];
}>();

// Local state
const showFiltersPanel = ref(true);

// Handle toggle filters panel
const toggleFiltersPanel = () => {
    emit('toggleFilters');
};
</script>

<template>
    <div class="search-results">
        <div class="search-controls">
            <div class="search-info">
                <span class="result-count">{{ totalItems }} results</span>
            </div>

            <div class="search-actions">
                <Button variant="outline" @click="toggleFiltersPanel">
                    <FunnelIcon :size="20" />
                    <span class="button-text">{{ showFiltersPanel ? 'Hide Filters' : 'Show Filters' }}</span>
                </Button>

                <Button variant="outline" class="map-button">
                    <MapPin :size="20" />
                    <span class="button-text">View on Map</span>
                </Button>
            </div>
        </div>

        <div v-if="isLoading" class="loading-state">
            <p>Loading results...</p>
        </div>

        <div v-else-if="results.length === 0" class="empty-state">
            <p>No listings match your search criteria. Try adjusting your filters.</p>
        </div>

        <div v-else class="results-list">
            <ItemCard v-for="(item, index) in results" :key="index" :item="item" />
        </div>
    </div>
</template>

<style scoped>
.search-results {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    width: 100%;
}

.search-controls {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
}

.search-info {
    font-size: 0.875rem;
}

.result-count {
    font-weight: 500;
}

.search-actions {
    display: flex;
    gap: 0.5rem;
}

.map-button {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.button-text {
    display: none;
}

.loading-state,
.empty-state {
    padding: 2rem;
    text-align: center;
    background-color: var(--background);
    border: 1px solid var(--border);
    border-radius: var(--radius);
}

.results-list {
    display: grid;
    gap: 1rem;
    grid-template-columns: 1fr;
}

/* Media queries for responsive design */
@media (min-width: 640px) {
    .button-text {
        display: inline;
    }
}

@media (min-width: 768px) {
    .results-list {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (min-width: 1024px) {
    .results-list {
        grid-template-columns: repeat(3, 1fr);
    }
}
</style>
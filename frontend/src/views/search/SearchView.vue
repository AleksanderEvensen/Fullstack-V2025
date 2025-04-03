<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { ChevronDown, MapPin, X, FunnelIcon } from 'lucide-vue-next'
import { useUrlSearchParams, watchDebounced } from '@vueuse/core'
import { computed, ref, watch } from 'vue'
import { MockSearchEntries } from './mock_data'
import ItemCard from './components/ItemCard.vue'

const queryParams = useUrlSearchParams('history', {
  removeFalsyValues: true,
  removeNullishValues: true,
})

const searchInput = ref('')
watchDebounced(
  searchInput,
  (value) => {
    queryParams.q = value
  },
  { debounce: 500 },
)

watch(queryParams, (query) => {
  console.log('Search query updated:', query)
})

const filtersPanelOpen = ref(true)

const data = computed(() => MockSearchEntries)
</script>

<template>
  <div class="container">
    <header>
      <h1 class="text-2xl">Find what your heart desires</h1>
      <span class="text-sm">{{ data.length }} treff</span>
    </header>
    <p>Søk:</p>
    <div class="search-header">
      <Input v-model="searchInput" placeholder="Søkeord..." />
    </div>

    <div class="search-container">
      <!-- Left sidebar with filters -->
      <div v-if="filtersPanelOpen" class="filters-sidebar">
        <div class="filter-section">
          <div class="filter-header">
            <h3>Merke</h3>
            <ChevronDown class="filter-expand-icon" />
          </div>
          <div class="filter-options">
            <div class="filter-option">
              <input type="checkbox" id="4speed" class="filter-checkbox" />
              <label for="4speed">4Speed (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="4wmoto" class="filter-checkbox" />
              <label for="4wmoto">4W-Moto (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="aixam" class="filter-checkbox" />
              <label for="aixam">Aixam (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="ajp" class="filter-checkbox" />
              <label for="ajp">AJP (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="americanironhorse" class="filter-checkbox" />
              <label for="americanironhorse">American Iron Horse (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="aprilia" class="filter-checkbox" />
              <label for="aprilia">Aprilia (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="arcticcat" class="filter-checkbox" />
              <label for="arcticcat">Arctic-cat (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="ariel" class="filter-checkbox" />
              <label for="ariel">Ariel (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="beta" class="filter-checkbox" />
              <label for="beta">Beta (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="honda" class="filter-checkbox" />
              <label for="honda">Honda (72)</label>
            </div>
            <button class="show-more-button">Vis alle</button>
          </div>
        </div>

        <div class="filter-section">
          <div class="filter-header">
            <h3>Betaling og eierskifte</h3>
            <ChevronDown class="filter-expand-icon" />
          </div>
          <div class="filter-options">
            <div class="filter-option">
              <input type="checkbox" id="smidigmc" class="filter-checkbox" />
              <label for="smidigmc">Smidig MC-handel (0)</label>
            </div>
          </div>
        </div>

        <div class="filter-section">
          <div class="filter-header">
            <h3>Type</h3>
            <ChevronDown class="filter-expand-icon" />
          </div>
          <div class="filter-options">
            <div class="filter-option">
              <input type="checkbox" id="adventure" class="filter-checkbox" />
              <label for="adventure">Adventure (6)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="atv" class="filter-checkbox" />
              <label for="atv">ATV (0)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="chopper" class="filter-checkbox" />
              <label for="chopper">Chopper (1)</label>
            </div>
            <div class="filter-option">
              <input type="checkbox" id="classicnaked" class="filter-checkbox" />
              <label for="classicnaked">Classic/Nakne (32)</label>
            </div>
          </div>
        </div>
      </div>

      <!-- Right side with search results -->
      <div class="search-results">
        <div class="search-filters">
          <Button variant="outline" @click="filtersPanelOpen = !filtersPanelOpen">
            <FunnelIcon :size="20" />
          </Button>
          <Button variant="outline" class="map-view">
            <MapPin :size="20" />
            Vis på kart
          </Button>

          <Select default-value="published">
            <SelectTrigger class="sort-by">
              <SelectValue placeholder="Sortering" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="published">Publisert</SelectItem>
                <SelectItem value="price-low">Pris lav-høy</SelectItem>
                <SelectItem value="price-high">Pris høy-lav</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>

        <div class="search-results-list">
          <ItemCard v-for="(item, index) in data" :item="item" :key="index" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
header {
  margin-top: 2rem;
  margin-bottom: 1.5rem;
  font-weight: var(--font-weight-semibold);

  span {
    font-weight: var(--font-weight-thin);
    color: var(--muted-foreground);
  }
}

.search-header {
  display: flex;
  flex-direction: row;
  gap: calc(var(--spacing) * 2);

  align-items: center;

  margin-bottom: calc(var(--spacing) * 4);
}

.search-container {
  display: flex;
  gap: 2rem;
  width: 100%;
}

.search-filters {
  display: flex;
  gap: calc(var(--spacing) * 2);
}

.map-view {
  display: flex;
  gap: var(--spacing);
}

.sort-by {
  align-self: flex-end;
  width: fit-content;
}

/*

.search-title h1 {
  font-size: 1.5rem;
  font-weight: 500;
  margin: 0;
}

.search-results-count {
  font-size: 0.9rem;
  color: var(--muted-foreground);
}

.search-container {
  display: flex;
  gap: 2rem;
}
  */

/* Filters sidebar */
.filters-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.search-box {
  margin-bottom: 1.5rem;
}

.search-box p {
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.search-input-container {
  position: relative;
}

.search-input {
  width: 100%;
}

.clear-search-button {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  color: var(--muted-foreground);
}

.filter-section {
  border-top: 1px solid var(--border);
  padding: 1rem 0;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  cursor: pointer;
}

.filter-header h3 {
  font-size: 1rem;
  font-weight: 500;
  margin: 0;
}

.filter-expand-icon {
  color: var(--muted-foreground);
}

.filter-options {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.filter-checkbox {
  width: 18px;
  height: 18px;
}

.filter-option label {
  font-size: 0.9rem;
}

.show-more-button {
  background: none;
  border: none;
  color: #0068e1;
  font-size: 0.9rem;
  padding: 0.5rem 0;
  cursor: pointer;
  text-align: left;
}

/* Search results */
.search-results {
  flex: 1;
}

.search-controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.view-options {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.view-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: none;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 0.5rem;
  cursor: pointer;
  font-size: 0.9rem;
}

.view-option.active {
  background-color: var(--muted);
}

.sort-select {
  padding: 0.5rem;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  font-size: 0.9rem;
}

.info-banner {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background-color: #f0f6ff;
  color: #0068e1;
  padding: 0.75rem;
  border-radius: var(--radius);
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.search-results-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* Responsive */
@media (max-width: 900px) {
  .search-container {
    flex-direction: column;
  }

  .filters-sidebar {
    width: 100%;
  }
}
</style>

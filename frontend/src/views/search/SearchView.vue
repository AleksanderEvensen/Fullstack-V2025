<script setup lang="ts">
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { MapPin, FunnelIcon, ChevronRight } from 'lucide-vue-next'
import { useUrlSearchParams, watchDebounced } from '@vueuse/core'
import { computed, ref, watch } from 'vue'
import { MockSearchEntries } from './mock_data'
import ItemCard from './components/ItemCard.vue'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible'
import { Label } from '@/components/ui/label'
import { Checkbox } from '@/components/ui/checkbox'

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
        <Collapsible :default-open="true">
          <CollapsibleTrigger as-child>
            <Button variant="ghost" class="category-collapse">
              <span>Merke</span>
              <ChevronRight class="category-collapse-icon" />
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent class="filter-container">
            <Label class="filter-check">
              <Checkbox />
              4Speed (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              4W-Moto (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Aixam (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              AJP (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              American Iron Horse (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Aprilia (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Arctic-cat (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Ariel (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Beta (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Honda (72)
            </Label>
          </CollapsibleContent>
        </Collapsible>

        <Collapsible :default-open="true">
          <CollapsibleTrigger as-child>
            <Button variant="ghost" class="category-collapse">
              <span>Type</span>
              <ChevronRight class="category-collapse-icon" />
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent class="filter-container">
            <Label class="filter-check">
              <Checkbox />
              Adventure (6)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              ATV (0)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Chopper (1)
            </Label>
            <Label class="filter-check">
              <Checkbox />
              Classic/Nakne (32)
            </Label>
          </CollapsibleContent>
        </Collapsible>
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

<style scoped lang="css">
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
  margin-left: auto;
  width: fit-content;
}

.category-collapse {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  & > .category-collapse-icon {
    transition: 0.1s ease;
  }

  &[data-state='open'] > .category-collapse-icon {
    rotate: 90deg;
  }
}
.filter-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
  padding: calc(var(--spacing) * 5);
  font-size: var(--font-size-sm);
  & > label {
    font-weight: 400 !important;
  }
}

.search-results {
  flex: 1;
  container-type: inline-size;
  container-name: search-results;
}

.filters-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.search-results-list {
  display: grid;
  gap: var(--spacing);
  grid-template-rows: auto;
  grid-template-columns: repeat(1, minmax(0, 1fr));
}


@container search-results (min-width: 700px) {
  .search-results-list {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
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

/*.filters-sidebar {
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
}*/

/* Search results */
/*.search-results {
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
}*/

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

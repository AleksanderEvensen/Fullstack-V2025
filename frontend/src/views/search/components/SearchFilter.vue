<script setup lang="ts">
import { ref } from 'vue';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';
import { ChevronRight } from 'lucide-vue-next';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import type { ListingSearchParams } from '@/models/listing';

// Define props and emits
const emit = defineEmits<{
    filter: [params: Partial<ListingSearchParams>];
}>();

// Local state for filter values
const searchQuery = ref('');
const minPrice = ref<string>('');
const maxPrice = ref<string>('');
const selectedCondition = ref<string | null>(null);
const selectedCategory = ref<number | null>(null);
const manufacturer = ref('');
const modelYear = ref('');

// Predefined filter options
const conditionOptions = [
    { label: 'New', value: 'NEW' },
    { label: 'Like New', value: 'LIKE_NEW' },
    { label: 'Very Good', value: 'VERY_GOOD' },
    { label: 'Good', value: 'GOOD' },
    { label: 'Acceptable', value: 'ACCEPTABLE' },
];

const manufacturerOptions = [
    'Honda', 'Toyota', 'BMW', 'Tesla', 'Audi', 'Yamaha',
    'Samsung', 'Apple', 'Sony', 'Canon', 'Dell'
];

// Apply filters
const applyFilters = () => {
    emit('filter', {
        q: searchQuery.value || undefined,
        condition: selectedCondition.value || undefined,
        minPrice: minPrice.value ? parseFloat(minPrice.value) : undefined,
        maxPrice: maxPrice.value ? parseFloat(maxPrice.value) : undefined,
        categoryId: selectedCategory.value || undefined,
        manufacturer: manufacturer.value || undefined,
        modelYear: modelYear.value || undefined,
    });
};

// Reset filters
const resetFilters = () => {
    searchQuery.value = '';
    minPrice.value = '';
    maxPrice.value = '';
    selectedCondition.value = null;
    selectedCategory.value = null;
    manufacturer.value = '';
    modelYear.value = '';

    applyFilters();
};
</script>

<template>
    <div class="filters-sidebar">
        <div class="filters-header">
            <h3>Filters</h3>
            <Button variant="ghost" size="sm" @click="resetFilters">Clear all</Button>
        </div>

        <div class="filter-section">
            <label class="filter-label">Search</label>
            <Input v-model="searchQuery" placeholder="Search in title and description..." @keyup.enter="applyFilters" />
        </div>

        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>Price Range</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <div class="price-range">
                    <Input v-model="minPrice" type="number" placeholder="Min price" min="0" />
                    <span class="to-separator">to</span>
                    <Input v-model="maxPrice" type="number" placeholder="Max price" min="0" />
                    <Button variant="outline" size="sm" class="apply-price" @click="applyFilters">Apply</Button>
                </div>
            </CollapsibleContent>
        </Collapsible>

        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>Condition</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <Label class="filter-check" v-for="option in conditionOptions" :key="option.value">
                    <Checkbox :value="option.value" :checked="selectedCondition === option.value"
                        @click="selectedCondition = selectedCondition === option.value ? null : option.value; applyFilters()" />
                    {{ option.label }}
                </Label>
            </CollapsibleContent>
        </Collapsible>

        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>Manufacturer</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <div class="manufacturer-filter">
                    <Input v-model="manufacturer" placeholder="Search manufacturer..." @keyup.enter="applyFilters" />

                    <div class="manufacturer-options">
                        <Label class="filter-check" v-for="option in manufacturerOptions" :key="option">
                            <Checkbox :value="option" :checked="manufacturer === option"
                                @click="manufacturer = manufacturer === option ? '' : option; applyFilters()" />
                            {{ option }}
                        </Label>
                    </div>
                </div>
            </CollapsibleContent>
        </Collapsible>

        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>Year</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <Input v-model="modelYear" placeholder="Model year (e.g. 2023)" @keyup.enter="applyFilters" />
                <Button variant="outline" size="sm" @click="applyFilters" class="year-apply">Apply</Button>
            </CollapsibleContent>
        </Collapsible>

        <div class="filter-actions">
            <Button @click="applyFilters">Apply Filters</Button>
        </div>
    </div>
</template>

<style scoped>
.filters-sidebar {
    width: 300px;
    flex-shrink: 0;
    border-right: 1px solid var(--border);
    padding: 1rem;
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.filters-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
}

.filters-header h3 {
    font-size: 1.25rem;
    font-weight: 600;
    margin: 0;
}

.filter-section {
    margin-bottom: 1rem;
}

.filter-label {
    display: block;
    font-size: 0.875rem;
    font-weight: 500;
    margin-bottom: 0.5rem;
}

.category-collapse {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding-left: 0;
    padding-right: 0;
    font-weight: 500;
}

.category-collapse-icon {
    transition: 0.1s ease;
}

.category-collapse[data-state='open']>.category-collapse-icon {
    transform: rotate(90deg);
}

.filter-container {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
    padding: 0.75rem 0;
    font-size: var(--font-size-sm);
}

.filter-check {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-weight: 400;
    cursor: pointer;
}

.price-range {
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    gap: 0.5rem;
    align-items: center;
}

.to-separator {
    text-align: center;
    color: var(--muted-foreground);
}

.apply-price {
    grid-column: span 3;
    margin-top: 0.5rem;
}

.year-apply {
    margin-top: 0.5rem;
}

.manufacturer-filter {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
}

.manufacturer-options {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    max-height: 200px;
    overflow-y: auto;
    padding-right: 0.5rem;
}

.filter-actions {
    margin-top: auto;
    padding-top: 1rem;
}

@media (max-width: 768px) {
    .filters-sidebar {
        width: 100%;
        border-right: none;
        border-bottom: 1px solid var(--border);
    }
}
</style>
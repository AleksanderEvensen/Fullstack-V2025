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
import { ref, watch } from 'vue'

// Define the search form props and events
defineProps<{
    categories: Array<{ id: number; name: string }>;
}>();

const emit = defineEmits<{
    search: [searchParams: {
        title?: string;
        description?: string;
        categoryId?: number;
        condition?: string;
        minPrice?: number;
        maxPrice?: number;
        modelYear?: string;
        manufacturer?: string;
        model?: string;
        sortBy?: string;
        sortDirection?: string;
    }];
}>();

// Search form state
const searchTitle = ref('');
const searchDescription = ref('');
const selectedCategory = ref<number | null>(null);
const selectedCondition = ref<string | null>(null);
const minPrice = ref<string>('');
const maxPrice = ref<string>('');
const modelYear = ref('');
const manufacturer = ref('');
const model = ref('');
const sortBy = ref('createdAt');
const sortDirection = ref('DESC');

const conditionOptions = [
    { value: 'NEW', label: 'New' },
    { value: 'LIKE_NEW', label: 'Like New' },
    { value: 'VERY_GOOD', label: 'Very Good' },
    { value: 'GOOD', label: 'Good' },
    { value: 'ACCEPTABLE', label: 'Acceptable' },
];

const sortOptions = [
    { value: 'createdAt,DESC', label: 'Newest first' },
    { value: 'createdAt,ASC', label: 'Oldest first' },
    { value: 'price,ASC', label: 'Price: Low to High' },
    { value: 'price,DESC', label: 'Price: High to Low' },
];

// Watch for sort option changes
watch(sortBy, (newValue) => {
    if (newValue && newValue.includes(',')) {
        const [sort, direction] = newValue.split(',');
        sortBy.value = sort;
        sortDirection.value = direction;
    }
});

// Handle search form submission
const handleSearch = () => {
    emit('search', {
        title: searchTitle.value || undefined,
        description: searchDescription.value || undefined,
        categoryId: selectedCategory.value || undefined,
        condition: selectedCondition.value || undefined,
        minPrice: minPrice.value ? parseFloat(minPrice.value) : undefined,
        maxPrice: maxPrice.value ? parseFloat(maxPrice.value) : undefined,
        modelYear: modelYear.value || undefined,
        manufacturer: manufacturer.value || undefined,
        model: model.value || undefined,
        sortBy: sortBy.value,
        sortDirection: sortDirection.value,
    });
};

// Clear all form fields
const clearForm = () => {
    searchTitle.value = '';
    searchDescription.value = '';
    selectedCategory.value = null;
    selectedCondition.value = null;
    minPrice.value = '';
    maxPrice.value = '';
    modelYear.value = '';
    manufacturer.value = '';
    model.value = '';
    sortBy.value = 'createdAt';
    sortDirection.value = 'DESC';

    handleSearch(); // Trigger search with empty values
};
</script>

<template>
    <div class="search-form">
        <div class="search-header">
            <h2>Find listings</h2>
        </div>

        <div class="search-fields">
            <div class="form-field">
                <label for="search-title">Title</label>
                <Input id="search-title" v-model="searchTitle" placeholder="Search by title..."
                    @keyup.enter="handleSearch" />
            </div>

            <div class="form-field">
                <label for="search-description">Description</label>
                <Input id="search-description" v-model="searchDescription" placeholder="Search in description..."
                    @keyup.enter="handleSearch" />
            </div>

            <div class="form-field">
                <label for="search-category">Category</label>
                <Select v-model="selectedCategory">
                    <SelectTrigger id="search-category">
                        <SelectValue placeholder="Select category" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem v-for="category in categories" :key="category.id" :value="category.id">
                                {{ category.name }}
                            </SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>

            <div class="form-field">
                <label for="search-condition">Condition</label>
                <Select v-model="selectedCondition">
                    <SelectTrigger id="search-condition">
                        <SelectValue placeholder="Select condition" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem v-for="option in conditionOptions" :key="option.value" :value="option.value">
                                {{ option.label }}
                            </SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>

            <div class="price-range">
                <label>Price Range</label>
                <div class="price-inputs">
                    <Input v-model="minPrice" type="number" placeholder="Min price" min="0" />
                    <span class="price-separator">to</span>
                    <Input v-model="maxPrice" type="number" placeholder="Max price" min="0" />
                </div>
            </div>

            <div class="form-field">
                <label for="search-model-year">Model Year</label>
                <Input id="search-model-year" v-model="modelYear" placeholder="e.g. 2022" />
            </div>

            <div class="form-field">
                <label for="search-manufacturer">Manufacturer</label>
                <Input id="search-manufacturer" v-model="manufacturer" placeholder="e.g. Honda" />
            </div>

            <div class="form-field">
                <label for="search-model">Model</label>
                <Input id="search-model" v-model="model" placeholder="e.g. Civic" />
            </div>

            <div class="form-field">
                <label for="search-sort">Sort By</label>
                <Select v-model="sortBy">
                    <SelectTrigger id="search-sort">
                        <SelectValue placeholder="Sort results by..." />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem v-for="option in sortOptions" :key="option.value" :value="option.value">
                                {{ option.label }}
                            </SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>

            <div class="search-buttons">
                <Button @click="handleSearch">Search</Button>
                <Button variant="outline" @click="clearForm">Clear</Button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.search-form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    width: 100%;
    padding: 1rem;
    border: 1px solid var(--border);
    border-radius: var(--radius);
    background-color: var(--background);
}

.search-header {
    margin-bottom: 1rem;
}

.search-header h2 {
    font-size: 1.5rem;
    font-weight: 500;
    margin: 0;
}

.search-fields {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.form-field {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.form-field label {
    font-size: 0.875rem;
    font-weight: 500;
}

.price-range {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.price-range label {
    font-size: 0.875rem;
    font-weight: 500;
}

.price-inputs {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.price-separator {
    font-size: 0.875rem;
    color: var(--muted-foreground);
}

.search-buttons {
    display: flex;
    gap: 0.5rem;
    margin-top: 1rem;
}
</style>
<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Checkbox } from '@/components/ui/checkbox';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';
import { ChevronRight } from 'lucide-vue-next';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible';
import { SliderReusable } from '@/components/ui/slider';
import type { paths } from '@/lib/api/schema';
import { useDebounceFn, useUrlSearchParams } from '@vueuse/core';
import { getCategories } from '@/lib/api/queries/categories';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

type ListingSearchParams = paths['/api/listings/search']['get']['parameters']['query']

const emit = defineEmits(['filter-change']);

const urlParams = useUrlSearchParams<NonNullable<ListingSearchParams>>('history', {
    removeFalsyValues: true,
    removeNullishValues: true,
    writeMode: 'replace',
})

const filterByPrice = ref(false);
const filterByYear = ref(false);

const localFilters = reactive<NonNullable<ListingSearchParams>>({
    q: getUrlParamValue('q', undefined),
    minPrice: getUrlParamValue('minPrice', undefined),
    maxPrice: getUrlParamValue('maxPrice', undefined),
    condition: getUrlParamValue('condition', undefined),
    manufacturer: getUrlParamValue('manufacturer', undefined),
    minModelYear: getUrlParamValue('minModelYear', undefined),
    maxModelYear: getUrlParamValue('maxModelYear', undefined),
    categoryName: getUrlParamValue('categoryName', undefined)
})

const priceValue = ref([
    localFilters.minPrice ? Number(localFilters.minPrice) : 0,
    localFilters.maxPrice ? Number(localFilters.maxPrice) : 10000
]);

const yearValue = ref([
    localFilters.minModelYear ? Number(localFilters.minModelYear) : (new Date().getFullYear() - 10),
    localFilters.maxModelYear ? Number(localFilters.maxModelYear) : new Date().getFullYear()
]);

const currentYear = new Date().getFullYear();

watch(priceValue, (newValue) => {
    if (newValue && newValue.length === 2) {
        localFilters.minPrice = newValue[0];
        localFilters.maxPrice = newValue[1];
    }
}, { deep: true });

watch(yearValue, (newValue) => {
    if (newValue && newValue.length === 2) {
        localFilters.minModelYear = newValue[0];
        localFilters.maxModelYear = newValue[1];
    }
}, { deep: true });

const { data: categories } = getCategories();

function getUrlParamValue(key: keyof NonNullable<ListingSearchParams>, defaultValue: any): any {
    return urlParams[key];
}

const updateUrlParams = useDebounceFn(() => {
    if (!filterByPrice) {
        localFilters.minPrice = undefined;
        localFilters.maxPrice = undefined;
    }
    if (!filterByYear) {
        localFilters.minModelYear = undefined;
        localFilters.maxModelYear = undefined;
    }
    emit('filter-change', { ...localFilters });
}, 500);

watch(localFilters, updateUrlParams, { deep: true });

watch(filterByPrice, (newValue) => {
    if (!newValue) {
        localFilters.minPrice = undefined;
        localFilters.maxPrice = undefined;
    } else {
        localFilters.minPrice = priceValue.value[0];
        localFilters.maxPrice = priceValue.value[1];
    }

    updateUrlParams();
}, { deep: true });

watch(filterByYear, (newValue) => {
    if (!newValue) {
        localFilters.minModelYear = undefined;
        localFilters.maxModelYear = undefined;
    } else {
        localFilters.minModelYear = yearValue.value[0];
        localFilters.maxModelYear = yearValue.value[1];
    }
    updateUrlParams();
}, { deep: true });



const clearAllFilters = () => {
    localFilters.q = '';
    localFilters.minPrice = undefined;
    localFilters.maxPrice = undefined;
    localFilters.condition = undefined;
    localFilters.manufacturer = undefined;
    localFilters.minModelYear = undefined;
    localFilters.maxModelYear = undefined;
    localFilters.categoryName = undefined;
    updateUrlParams();
}

const conditionOptions = [
    { label: t('product.conditionLabels.new'), value: 'NEW' },
    { label: t('product.conditionLabels.likeNew'), value: 'LIKE_NEW' },
    { label: t('product.conditionLabels.veryGood'), value: 'VERY_GOOD' },
    { label: t('product.conditionLabels.good'), value: 'GOOD' },
    { label: t('product.conditionLabels.acceptable'), value: 'ACCEPTABLE' },
];
</script>

<template>
    <div class="filters-sidebar">
        <div class="filters-header">
            <h3>{{ t('search.filters.title') }}</h3>
            <Button variant="ghost" size="sm" @click="clearAllFilters">{{ t('search.filters.clearAll') }}</Button>
        </div>

        <div class="filter-option">
            <div class="filter-checkbox">
                <Checkbox id="price-filter" v-model="filterByPrice" />
                <label for="price-filter">{{ t('search.filters.priceRange') }}</label>
            </div>
            <div v-if="filterByPrice" class="filter-content">
                <div class="slider-container">
                    <div class="price-values">
                        <span>{{ t('search.filters.currency') }} {{ priceValue[0] }}</span>
                        <span>{{ t('search.filters.currency') }} {{ priceValue[1] }}</span>
                    </div>
                    <SliderReusable v-model="priceValue" :min="0" :max="100000" :step="100" class="price-slider" />
                </div>
            </div>
        </div>

        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>{{ t('search.filters.condition') }}</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <Select v-model="localFilters.condition" :placeholder="t('search.filters.selectCondition')">
                    <SelectTrigger>
                        <SelectValue :placeholder="localFilters.condition" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem v-for="option in conditionOptions" :key="option.value" :value="option.value">
                            {{ option.label }}
                        </SelectItem>
                    </SelectContent>
                </Select>
            </CollapsibleContent>
        </Collapsible>

        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>{{ t('search.filters.category') }}</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <Select v-model="localFilters.categoryName" :placeholder="t('search.filters.selectCategory')">
                    <SelectTrigger>
                        <SelectValue :placeholder="localFilters.categoryName" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem v-for="option in categories" :key="option.name" :value="option.name">
                            {{ option.name }}
                        </SelectItem>
                    </SelectContent>
                </Select>
            </CollapsibleContent>
        </Collapsible>


        <Collapsible :default-open="true">
            <CollapsibleTrigger as-child>
                <Button variant="ghost" class="category-collapse">
                    <span>{{ t('search.filters.manufacturer') }}</span>
                    <ChevronRight class="category-collapse-icon" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent class="filter-container">
                <div class="manufacturer-filter">
                    <Input v-model="localFilters.manufacturer" :placeholder="t('search.filters.searchManufacturer')" />
                </div>
            </CollapsibleContent>
        </Collapsible>

        <div class="filter-option">
            <div class="filter-checkbox">
                <Checkbox id="year-filter" v-model="filterByYear" />
                <label for="year-filter">{{ t('search.filters.yearRange') }}</label>
            </div>
            <div v-if="filterByYear" class="filter-content">
                <div class="slider-container">
                    <div class="year-values">
                        <span>{{ yearValue[0] }}</span>
                        <span>{{ yearValue[1] }}</span>
                    </div>
                    <SliderReusable v-model="yearValue" :min="1950" :max="currentYear" :step="1" class="year-slider" />
                </div>
            </div>
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

.filter-option {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-bottom: 0.5rem;
    border-bottom: 1px solid var(--border-muted, #f1f1f1);
    padding-bottom: 0.5rem;
}

.filter-checkbox {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 0;
}

.filter-checkbox label {
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
}

.filter-content {
    padding: 0 0.5rem 0.5rem;
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

.slider-container {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
    padding: 0.5rem 0;
}

.price-values,
.year-values {
    display: flex;
    justify-content: space-between;
    font-size: 0.875rem;
    color: var(--muted-foreground);
}




.manufacturer-filter {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
}


@media (max-width: 850px) {
    .filters-sidebar {
        width: 100%;
        border-right: none;
        border-bottom: 1px solid var(--border);
    }
}
</style>
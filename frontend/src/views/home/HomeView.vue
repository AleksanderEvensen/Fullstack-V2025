<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { RouterLink } from 'vue-router'
import { Icon, type IconName } from '@/components/ui/icon'
import ProductGrid from '@/views/home/components/ProductGrid.vue'
import { MapIcon, Search } from 'lucide-vue-next'
import { getListings } from '@/lib/api/queries/listings'
import { getCategories } from '@/lib/api/queries/categories'
import { useTypedI18n } from '@/i18n'
const { t } = useTypedI18n()
const { data } = getListings({
  page: 0,
  size: 10,
  direction: 'DESC',
  sortBy: 'createdAt',
})
const { data: categoriesData, isLoading: categoriesLoading } = getCategories()
function icon(iconName: string): IconName {
  return iconName as IconName
}
</script>

<template>
  <div class="container home-page">
    <div class="search-header">
      <div class="search-container">
        <Input class="search-input" :placeholder="t('home.search.placeholder')" />
        <Button size="icon" class="search-button">
          <Search :size="24" />
        </Button>
      </div>

      <div class="cart-button">
        <RouterLink to="/map" class="cart-link">
          <Button variant="outline" class="map-button">
            <MapIcon :size="24" />
            <span>{{ t('home.map') }}</span>
          </Button>
        </RouterLink>
      </div>
    </div>

    <!-- Categories -->
    <div class="categories-container">
      <RouterLink v-for="category in categoriesData" :to="`/categories/${category.name}`" class="category-item"
        :key="category.name">
        <Icon :name="icon(category.icon)" />
        <div class="category-name">{{ category.name }}</div>
      </RouterLink>
    </div>

    <!-- Popular announcements -->
    <div class="popular-section">
      <h2 class="section-title">{{ t('home.recentListings') }}</h2>
      <ProductGrid :products="data?.content ?? []" />
    </div>
  </div>
</template>

<style scoped>
.home-page {
  padding: 1rem;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.search-button {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}

.map-button {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-grow: 1;
}

.search-input {
  flex: 1;
  height: 50px;
  border-radius: var(--radius);
  padding-right: 50px;
}

.cart-button {
  display: flex;
  align-items: center;
  margin-left: 1rem;
}

.cart-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  color: var(--foreground);
  font-size: 0.9rem;
}

/* Categories */
.categories-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
  margin-bottom: 2rem;
  max-width: 1000px;
  margin-left: auto;
  margin-right: auto;
  padding: 0 1rem;
}


.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  color: var(--foreground);
  font-size: 0.8rem;
  width: calc(14.28% - 10px);
  min-width: 80px;
  padding: 0.5rem;
  border-radius: var(--radius);
  transition: background-color 0.2s;
  gap: 10px;
}

.category-item:hover {
  background-color: var(--accent);
}

.category-name {
  text-align: center;
  line-height: 1.2;
  font-size: 0.8rem;
}

/* Popular listings */
.popular-section {
  margin-top: 2rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

/* Responsive adjustments */
@media (max-width: 64rem) {

  .category-item {
    width: calc(25% - 10px);
  }
}

@media (max-width: 480px) {
  .category-item {
    width: calc(33.33% - 10px);
  }

  .cart-button {
    display: none;
  }
}
</style>
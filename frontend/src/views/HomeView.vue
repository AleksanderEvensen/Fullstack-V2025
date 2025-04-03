<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { RouterLink } from 'vue-router'
import { BikeIcon, CarIcon, HeartIcon, LuggageIcon, MapIcon, PlaneIcon, SailboatIcon, SearchIcon, SofaIcon } from 'lucide-vue-next'
</script>

<template>
  <div class="home-page">
    <!-- Header with search -->
    <div class="search-container">
      <Input class="search-input" placeholder="Søk etter elbil eller elsykkel" />
      <Button size="icon">
        <SearchIcon :size="24" />
      </Button>

      <div class="cart-button">
        <RouterLink to="/cart" class="cart-link">
          <MapIcon :size="24" />    
          <span>Kart</span>
        </RouterLink>
      </div>
    </div>

    <!-- Categories -->
    <div class="categories-container">
      <div class="category-row">
        <RouterLink to="/torget" class="category-item">
          <SofaIcon :size="32" class="category-icon" />
          <div class="category-name">Torget</div>
        </RouterLink>
        
        <RouterLink to="/bil-og-campingvogn" class="category-item">
          <CarIcon :size="32" class="category-icon" />
          <div class="category-name">Bil og motorvogn</div>
        </RouterLink>
        
        <RouterLink to="/reise" class="category-item">
          <PlaneIcon :size="32" class="category-icon" />
          <div class="category-name">Reise</div>
        </RouterLink>
        
        <RouterLink to="/bat" class="category-item">
          <SailboatIcon :size="32" class="category-icon" />
          <div class="category-name">Båt</div>
        </RouterLink>
        
        <RouterLink to="/mc" class="category-item">
          <BikeIcon :size="32" class="category-icon" />
          <div class="category-name">MC</div>
        </RouterLink>

        <RouterLink to="/jobb" class="category-item">
          <LuggageIcon :size="32" class="category-icon" />
          <div class="category-name">Jobb</div>
        </RouterLink>
      </div>
    </div>

    <!-- Popular announcements -->
    <div class="popular-section">
      <h2 class="section-title">Populære annonser</h2>
      
      <div class="listing-grid">
        <div class="listing-card">
          <div class="listing-image">
            <img src="https://dummyimage.com/400x400/000/ffffff" alt="Modern living room with sofa and art" />
            <Button variant="outline" size="icon" class="favorite-button">
              <HeartIcon :size="24" />
            </Button>
          </div>
          <div class="listing-info">
            <div class="listing-price">5 700 000 kr</div>
          </div>
        </div>
        
        <div class="listing-card">
          <div class="listing-image">
            <img src="https://dummyimage.com/400x400/000/ffffff" alt="COWI logo" />
            <Button variant="outline" size="icon" class="favorite-button">
              <HeartIcon :size="24" />
            </Button>
          </div>
          <div class="listing-info">
            <!-- No price for job listing -->
          </div>
        </div>
        
        <div class="listing-card">
          <div class="listing-image">
            <div class="badge-container">
              <Badge variant="secondary" class="listing-badge">Fiks ferdig</Badge>
            </div>
            <img src="https://dummyimage.com/400x400/000/ffffff" alt="White sneakers" />
            <Button variant="outline" size="icon" class="favorite-button">
              <HeartIcon :size="24" />
            </Button>
          </div>
          <div class="listing-info">
            <div class="listing-price">700 kr</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  padding: 1rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* Search container */
.search-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
  position: relative;
}

.search-input {
  flex: 1;
  height: 50px;
  border-radius: var(--radius);
  padding-right: 50px;
}

.search-button {
  position: absolute;
  right: 5px;
  height: 40px;
  width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
}

.search-icon {
  display: flex;
  align-items: center;
  justify-content: center;
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
  margin-bottom: 2rem;
}

.category-row {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 1rem;
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
}

.category-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 45px;
  width: 45px;
  margin-bottom: 0.5rem;
  color: #4dabf5;
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

.listing-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.listing-card {
  border-radius: var(--radius);
  overflow: hidden;
  position: relative;
}

.listing-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.listing-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.favorite-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: white;
  border-radius: 50%;
  height: 40px;
  width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
}

.listing-info {
  padding: 0.5rem 0;
}

.listing-price {
  font-weight: bold;
  font-size: 1.1rem;
}

.badge-container {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 5;
}

.listing-badge {
  font-size: 0.8rem;
  padding: 0.3rem 0.6rem;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .category-row {
    justify-content: flex-start;
    gap: 5px;
  }
  
  .category-item {
    width: calc(25% - 10px);
  }
  
  .listing-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
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
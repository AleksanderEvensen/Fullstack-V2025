<script setup lang="ts">
import { GlobeIcon, LanguagesIcon, MailIcon, PlusIcon, UserIcon } from "lucide-vue-next";
import { RouterLink } from "vue-router";
import Button from "./ui/button/Button.vue";


// TODO: Fix i18n
// TODO: Fix dropdowns to use shadcn
// TODO: Add flag font and add flags to the language dropdown
// TODO: Maybe change to shadcn menu links
</script>

<template>
  <nav class="navbar">
    <RouterLink to="" class="logo">
      <img src="/logo.svg" alt="Amazoom logo">
    </RouterLink>

    <div class="navbar-right">
      <!-- Categories Dropdown -->
      <div class="nav-item dropdown">
        <div class="dropdown-label">
          <GlobeIcon class="icon" /> Categories
        </div>
        <div class="dropdown-content">
          <a href="#">Electronics</a>
          <a href="#">Clothing</a>
          <a href="#">Home & Kitchen</a>
          <a href="#">Books</a>
          <a href="#">Beauty</a>
          <a href="#">Toys & Games</a>
          <a href="#">Sports</a>
          <a href="#">Automotive</a>
        </div>
      </div>

      <!-- Language Dropdown -->
      <div class="nav-item dropdown">
        <div class="dropdown-label">
          <LanguagesIcon class="icon" />Language
        </div>
        <div class="dropdown-content">
          <a href="#">English</a>
          <a href="#">Norwegian</a>
        </div>
      </div>

      <!-- Messages -->
      <div class="nav-item">
        <MailIcon class="icon" /> Messages
      </div>

      <!-- Create New Listing -->
      <div class="nav-item">
        <PlusIcon class="icon" /> Create Listing
      </div>

      <!-- Profile Icon -->
      <div class="nav-item">
        <Button size="icon" variant="secondary" as-child>
          <UserIcon class="profile-icon" />
        </Button>
      </div>
    </div>
  </nav>
</template>

<style lang="css" scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 12px 20px;
  position: sticky;
  top: 0;
  z-index: 100;
  /* Make sure the navbar renders above everything */
  margin: 0;
  border-bottom: 1px solid #333;
}

.navbar .logo img {
  height: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.logo {
  height: 40px;
}

.navbar-right {
  display: flex;
  align-items: center;
  margin-left: auto;
  gap: 20px;
}

.nav-item {
  margin: 0 15px;
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.nav-item:hover {
  color: #ff9900;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 200px;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
  border-radius: 4px;
  top: 40px;
  left: 0;
  color: #333;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-label {
  display: flex;
  flex-direction: row;
}

.dropdown-content a {
  padding: 12px 16px;
  display: block;
  border-bottom: 1px solid #eee;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}

.icon {
  margin-right: 8px;
  font-size: 20px;
}

@media (max-width: 768px) {
  .navbar {
    flex-wrap: wrap;
  }

  .navbar-right {
    flex-wrap: wrap;
    justify-content: flex-end;
    gap: 15px;
    margin-top: 10px;
  }

  .categories-grid,
  .listings-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>

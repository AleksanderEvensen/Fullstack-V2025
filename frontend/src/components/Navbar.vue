<script setup lang="ts">
import { ref, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { useI18n } from 'vue-i18n'
import Button from './ui/button/Button.vue'
import { Select, SelectValue, SelectTrigger, SelectContent, SelectItem } from './ui/select'
import CountryFlag from './ui/country-flag/CountryFlag.vue'
import { MenuIcon, XIcon, LanguagesIcon, MailIcon, PlusIcon, UserIcon } from 'lucide-vue-next'

const { locale } = useI18n()
const isMenuOpen = ref(false)
const selectedLanguage = ref('en')

const languages = [
  { value: 'en', label: 'English', flag: 'GB' },
  { value: 'no', label: 'Norwegian', flag: 'NO' },
]

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

watch(selectedLanguage, (newValue) => {
  locale.value = newValue
})
</script>

<template>
  <nav class="navbar">
    <div class="navbar-container">
      <RouterLink to="/" class="logo">
        <img src="/logo.svg" alt="Amazoom logo">
      </RouterLink>

      <Button variant="ghost" size="icon" class="menu-toggle" @click="toggleMenu">
        <MenuIcon v-if="!isMenuOpen" class="h-6 w-6" />
        <XIcon v-else class="h-6 w-6" />
      </Button>

      <div class="navbar-content" :class="{ 'active': isMenuOpen }">
        <!-- Language Select -->
        <div class="nav-item">
          <Select v-model="selectedLanguage" :options="languages" :placeholder="$t('nav.language')">
            <SelectTrigger>
              <div class="language-trigger">
                <LanguagesIcon class="icon" />
                <CountryFlag v-if="selectedLanguage"
                  :code="languages.find(l => l.value === selectedLanguage)?.flag || ''" :size="20" />
              </div>
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-for="language in languages" :key="language.value" :value="language.value">
                <div class="language-option">
                  <CountryFlag :code="language.flag" :size="20" />
                  <span>{{ language.label }}</span>
                </div>
              </SelectItem>
            </SelectContent>
          </Select>
        </div>

        <!-- Messages -->
        <RouterLink to="/marketplace/messages" class="nav-item">
          <Button variant="ghost" class="icon-button">
            <MailIcon class="icon" />
            <span>{{ $t('nav.messages') }}</span>
          </Button>
        </RouterLink>

        <!-- Create New Listing -->
        <RouterLink to="/marketplace/create" class="nav-item">
          <Button>
            <PlusIcon class="icon" />
            <span>{{ $t('nav.createListing') }}</span>
          </Button>
        </RouterLink>

        <!-- Profile -->
        <RouterLink to="/profile" class="nav-item">
          <Button variant="ghost" class="icon-button">
            <UserIcon class="icon" />
          </Button>
        </RouterLink>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  position: sticky;
  top: 0;
  z-index: 49;
  width: 100%;
  background-color: var(--background);
  border-bottom: 1px solid var(--border);
  padding: 0.5rem 1rem;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 40px;
}

.logo {
  height: 40px;
  display: flex;
  align-items: center;
  padding: 0.25rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
}

.logo:hover {
  background-color: var(--muted);
}

.logo img {
  height: 100%;
  width: auto;
  object-fit: contain;
}

.menu-toggle {
  display: none;
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
}

.menu-toggle:hover {
  background-color: var(--muted);
}

.navbar-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.nav-item {
  position: relative;
  display: flex;
  align-items: center;
}

.nav-item :deep(.button) {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
}

.icon {
  width: 1.25rem;
  height: 1.25rem;
  stroke-width: 1.5;
}

.language-trigger {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
  cursor: pointer;
}

.language-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
}

.language-option:hover {
  background-color: var(--muted);
}

.icon-button {
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

@media (max-width: 768px) {
  .navbar {
    padding: 0.5rem;
  }

  .navbar-container {
    height: 36px;
  }

  .logo {
    height: 36px;
  }

  .menu-toggle {
    display: flex;
  }

  .navbar-content {
    display: none;
    position: fixed;
    top: 64px;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: var(--background);
    padding: 1rem;
    flex-direction: column;
    align-items: stretch;
    gap: 0.5rem;
    overflow-y: auto;
  }

  .navbar-content.active {
    display: flex;
  }

  .nav-item {
    width: 100%;
  }

  .nav-item :deep(.button) {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>

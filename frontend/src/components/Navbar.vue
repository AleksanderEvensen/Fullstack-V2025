<script setup lang="ts">
import { MailIcon, MenuIcon, PlusIcon } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
import { useI18n } from 'vue-i18n'
import Button from './ui/button/Button.vue'
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from './ui/sheet'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select'
import { Avatar, AvatarFallback, AvatarImage } from './ui/avatar'
import FlagComponent from '@/components/ui/country-flag/CountryFlag.vue'
import { ref } from 'vue'

const isOpen = ref(false)
type Locale = 'en' | 'no'
const { t, locale, availableLocales } = useI18n<{ locale: Locale }>()
const locales: Record<Locale, { flag: string; name: string }> = {
  en: {
    flag: 'US',
    name: 'English',
  },
  no: {
    flag: 'NO',
    name: 'Norwegian',
  },
}
</script>

<template>
  <nav class="navbar">
    <RouterLink to="/" class="logo">
      <img src="/logo.svg" alt="Amazoom logo" />
    </RouterLink>

    <!-- Desktop Navigation -->
    <div class="navbar-right desktop-only">
      <!-- Language Select -->
      <div class="nav-item">
        <Select v-model="locale">
          <SelectTrigger>
            <SelectValue placeholder="Language">
              <FlagComponent :code="locales[locale as Locale].flag" />
              {{ locales[locale as Locale].name }}
            </SelectValue>
          </SelectTrigger>
          <SelectContent>
            <SelectItem
              v-for="locale in availableLocales as Locale[]"
              :key="locale"
              :value="locale"
            >
              <FlagComponent :code="locales[locale].flag" />
              {{ locales[locale].name }}
            </SelectItem>
          </SelectContent>
        </Select>
      </div>

      <!-- Messages -->
      <RouterLink to="/marketplace/messages" class="nav-item">
        <MailIcon class="icon" /> {{ t('nav.messages') }}
      </RouterLink>

      <!-- Create New Listing -->
      <RouterLink to="/marketplace/product/create" class="nav-item">
        <PlusIcon class="icon" /> {{ t('nav.createListing') }}
      </RouterLink>

      <!-- Profile Avatar -->
      <div class="nav-item">
        <RouterLink to="/profile">
          <Avatar class="cursor-pointer">
            <AvatarImage src="https://github.com/shadcn.png" alt="User" />
            <AvatarFallback>U</AvatarFallback>
          </Avatar>
        </RouterLink>
      </div>
    </div>

    <!-- Mobile Navigation -->
    <div class="mobile-only">
      <Sheet v-model:open="isOpen">
        <SheetTrigger as-child>
          <Button variant="ghost" size="icon">
            <MenuIcon class="h-6 w-6" />
          </Button>
        </SheetTrigger>
        <SheetContent side="right">
          <SheetHeader>
            <SheetTitle>Menu</SheetTitle>
          </SheetHeader>
          <div class="nav-items-mobile">
            <div class="nav-items-mobile-inner">
              <!-- Language Select -->
              <div>
                <Select v-model="locale">
                  <SelectTrigger>
                    <SelectValue placeholder="Language">
                      <FlagComponent :code="locales[locale as Locale].flag" />
                      {{ locales[locale as Locale].name }}
                    </SelectValue>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem
                      v-for="locale in availableLocales as Locale[]"
                      :key="locale"
                      :value="locale"
                    >
                      <FlagComponent :code="locales[locale].flag" />
                      {{ locales[locale].name }}
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <!-- Messages -->
              <RouterLink to="/marketplace/messages" class="mobile-nav-item">
                <MailIcon class="icon" />
                <span>{{ t('nav.messages') }}</span>
              </RouterLink>

              <!-- Create Listing -->
              <RouterLink to="/marketplace/product/create" class="mobile-nav-item">
                <PlusIcon class="icon" />
                <span>{{ t('nav.createListing') }}</span>
              </RouterLink>
            </div>

            <!-- Profile -->
            <RouterLink to="/profile" class="mobile-nav-item">
              <Avatar>
                <AvatarImage src="https://github.com/shadcn.png" alt="User" />
                <AvatarFallback>U</AvatarFallback>
              </Avatar>
              <span class="ml-3">{{ t('nav.profile') }}</span>
            </RouterLink>
          </div>
        </SheetContent>
      </Sheet>
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
  margin: 0;
  border-bottom: 1px solid #333;
}

.navbar .logo img {
  height: 100%;
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
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: center;
  background: transparent;
  border: none;
  padding: 8px 12px;
}

.icon {
  margin-right: 8px;
  font-size: 20px;
}

.nav-items-mobile {
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: 100%;
  padding: 0 2rem 2rem 0;
  justify-content: space-between;
}

.nav-items-mobile-inner {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  border-radius: 4px;
  gap: calc(var(--spacing) * 2);
  transition: background-color 0.2s;
}

.mobile-nav-item:hover {
  background-color: #f1f1f1;
}

.mobile-nav-item .icon {
  margin-right: 12px;
}

/* Responsive styles */
@media (max-width: 768px) {
  .desktop-only {
    display: none;
  }

  .mobile-only {
    display: block;
  }
}

@media (min-width: 769px) {
  .desktop-only {
    display: flex;
  }

  .mobile-only {
    display: none;
  }
}
</style>

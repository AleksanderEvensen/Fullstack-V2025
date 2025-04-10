<script setup lang="ts">
import { CogIcon, LogOutIcon, MailIcon, MenuIcon, PlusIcon, UserIcon } from 'lucide-vue-next'
import { RouterLink } from 'vue-router'
import Button from './ui/button/Button.vue'
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from './ui/sheet'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select'
import { Avatar, AvatarFallback, AvatarImage } from './ui/avatar'
import FlagComponent from '@/components/ui/country-flag/CountryFlag.vue'
import { computed, ref } from 'vue'
import { type Locales, AvailableLocales, useTypedI18n } from '@/i18n'
import { useAuthStore } from '@/stores/auth'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from './ui/dropdown-menu'
import { cn } from '@/lib/utils'

const isOpen = ref(false)
const { t, locale } = useTypedI18n()
const locales: Record<Locales, { flag: string; name: string }> = {
  en: {
    flag: 'US',
    name: 'English',
  },
  no: {
    flag: 'NO',
    name: 'Norwegian',
  },
}

const profilePicture = computed(() => {
  return useAuthStore().user?.profileImageUrl ?? ''
})

const user = computed(() => {
  return useAuthStore().user
})

function logout() {
  const authStore = useAuthStore()
  authStore.logout()
  window.location.href = '/'
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
            <SelectValue placeholder="Language" class="flag-item">
              <FlagComponent :code="locales[locale as Locales].flag" />
              {{ locales[locale as Locales].name }}
            </SelectValue>
          </SelectTrigger>
          <SelectContent>
            <SelectItem v-for="locale in AvailableLocales" :key="locale" :value="locale">
              <span class="flag-item">
                <FlagComponent :code="locales[locale].flag" />
                {{ locales[locale].name }}
              </span>
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
      <DropdownMenu>
        <DropdownMenuTrigger as-child>
          <Avatar :class="cn('avatar-button', !user && 'logged-out')">
            <AvatarImage :src="profilePicture" :alt="user?.name ?? 'Guest'" />
            <AvatarFallback>
              <UserIcon />
            </AvatarFallback>
          </Avatar>
        </DropdownMenuTrigger>
        <DropdownMenuContent>
          <DropdownMenuItem as-child>
            <RouterLink to="/profile" class="profile-menu-item"> <UserIcon />Profile </RouterLink>
          </DropdownMenuItem>
          <DropdownMenuItem as-child>
            <RouterLink to="/profile/settings" class="profile-menu-item">
              <CogIcon />Settings
            </RouterLink>
          </DropdownMenuItem>
          <DropdownMenuItem @click="logout"><LogOutIcon />Logout</DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
      <RouterLink to="/login">
        <Button :class="cn('nav-login-button', !user && 'logged-out')"> Login </Button>
      </RouterLink>
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
                    <SelectValue placeholder="Language" class="flag-item">
                      <FlagComponent :code="locales[locale as Locales].flag" />
                      {{ locales[locale as Locales].name }}
                    </SelectValue>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem v-for="locale in AvailableLocales" :key="locale" :value="locale">
                      <span class="flag-item">
                        <FlagComponent :code="locales[locale].flag" />
                        {{ locales[locale].name }}
                      </span>
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <!-- Messages -->
              <RouterLink @click="isOpen = false" to="/marketplace/messages" class="mobile-nav-item">
                <MailIcon class="icon" />
                <span>{{ t('nav.messages') }}</span>
              </RouterLink>

              <!-- Create Listing -->
              <RouterLink @click="isOpen = false" to="/marketplace/product/create" class="mobile-nav-item">
                <PlusIcon class="icon" />
                <span>{{ t('nav.createListing') }}</span>
              </RouterLink>
            </div>

            <!-- Profile -->
            <DropdownMenu>
              <DropdownMenuTrigger :class="cn('mobile-nav-item', !user && 'logged-out')">
                <Avatar class="avatar-button">
                  <AvatarImage :src="profilePicture" :alt="user?.name ?? 'Guest'" />
                  <AvatarFallback>
                    <UserIcon />
                  </AvatarFallback>
                </Avatar>
                <span class="ml-3">{{ user?.name }}</span>
              </DropdownMenuTrigger>
              <DropdownMenuContent style="width: 100%;">
                <DropdownMenuItem as-child>
                  <RouterLink @click="isOpen = false" to="/profile" class="profile-menu-item">
                    <UserIcon />Profile
                  </RouterLink>
                </DropdownMenuItem>
                <DropdownMenuItem as-child>
                  <RouterLink @click="isOpen = false" to="/profile/settings" class="profile-menu-item">
                    <CogIcon />Settings
                  </RouterLink>
                </DropdownMenuItem>
                <DropdownMenuItem @click="logout"><LogOutIcon />Logout</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
            <RouterLink @click="isOpen = false" to="/login" :class="cn('nav-login-button', !user && 'logged-out')">
              <Button> Login </Button>
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

.avatar-button {
  cursor: pointer;
}

.avatar-button.logged-out {
  display: none !important;
}

.nav-login-button {
  display: none;
  button {
    width: 100%;
  }
}

.nav-login-button.logged-out {
  display: block;
}

.mobile-nav-item.logged-out {
  display: none !important;
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
}

.flag-item {
  display: flex;
  align-items: center;
  gap: var(--spacing);
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
  justify-content: flex-start;
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

.mobile-only {
  display: none;
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

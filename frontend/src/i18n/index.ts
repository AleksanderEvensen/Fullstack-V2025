import { createI18n } from 'vue-i18n'
import en from './locales/en.json'
import no from './locales/no.json'

export const i18n = createI18n({
  legacy: false, // Set to false to use Composition API
  locale: 'en', // Set default locale
  fallbackLocale: 'en', // Set fallback locale
  messages: {
    en,
    no
  }
}) 
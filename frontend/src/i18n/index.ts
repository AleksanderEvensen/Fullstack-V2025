import { createI18n, useI18n } from 'vue-i18n'
import en from './locales/en.json'
import no from './locales/no.json'

type MessageSchema = typeof en

export const DefaultLocale = 'en'
export const AvailableLocales = ['en', 'no'] as const
export type Locales = (typeof AvailableLocales)[number]

export const i18n = createI18n<[MessageSchema], Locales>({
  legacy: false,
  locale: DefaultLocale,
  fallbackLocale: DefaultLocale,
  availableLocales: AvailableLocales as unknown as string[],
  messages: {
    en,
    no,
  },
})
export const useTypedI18n = () => useI18n<{ message: MessageSchema }, Locales>()

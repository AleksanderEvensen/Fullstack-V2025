<script lang="ts" setup>
import { computed, ref } from 'vue'
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import {   Mail, MapPin, InfoIcon, CircleCheckIcon, CircleAlertIcon } from 'lucide-vue-next'
import CountryFlag from '@/components/ui/country-flag/CountryFlag.vue'
import { useTypedI18n } from '@/i18n'
import { watchDebounced } from '@vueuse/core'
import { searchGeocodeAdvanced } from '@/lib/api/geocoding'


const { t } = useTypedI18n()

const formatNorwegianPhoneNumber = (input: string): string => {
  const digits = input.replace(/\D/g, '')

  let formatted = ''
  if (digits.length <= 3) {
    formatted = digits
  } else if (digits.length <= 5) {
    formatted = `${digits.slice(0, 3)} ${digits.slice(3)}`
  } else {
    formatted = `${digits.slice(0, 3)} ${digits.slice(3, 5)} ${digits.slice(5, 8)}`
  }

  return formatted
}

const city = ref('')
const streetAddress = ref('')
const postalCode = ref('')

const locationValid = ref<'empty' | 'found' | 'not-found'>('empty')
const locationValidText = computed(() => {
  switch (locationValid.value) {
    case 'empty':
      return t('location.notSpecified')
    case 'found':
      return t('location.found')
    case 'not-found':
      return t('location.notFound')
    default:
      return '';
  }
});

const locationQuery = computed(() => {
  return {
    street: streetAddress.value,
    postalcode: postalCode.value,
    city: city.value,
  }
})

watchDebounced(
  locationQuery,
  async (newValue) => {
    if (
      newValue.street.length == 0 ||
      newValue.city.length == 0 ||
      newValue.postalcode.length == 0
    ) {
      locationValid.value = 'empty'
      return
    }
    console.log('Location query changed:', newValue)
    const location = await searchGeocodeAdvanced(newValue)
    console.log('Location:', location)
    locationValid.value = (location?.length ?? 0) > 0 ? 'found' : 'not-found'
  },
  { debounce: 500 },
)

</script>

<template>
  <div class="form-section">
    <FormField v-slot="{ componentField }" name="email">
      <FormItem>
        <FormLabel class="form-label">{{ t('auth.email') }}</FormLabel>
        <FormControl>
          <div class="input-container">
            <Mail class="input-icon" />
            <Input type="email" v-bind="componentField" :placeholder="t('auth.emailPlaceholder')"
              class="input-with-icon" />
          </div>
        </FormControl>
        <FormMessage class="form-message" />
      </FormItem>
    </FormField>

    <FormField v-slot="{ componentField, setValue }" name="phoneNumber">
      <FormItem>
        <FormLabel class="form-label">{{ t('auth.phoneNumber') }}</FormLabel>
        <FormControl>
          <div class="phone-field">
            <div class="country-prefix">
              <CountryFlag code="NO" class="country-flag" />
            </div>
            <div class="input-container">
              <Input type="tel" v-bind="componentField" :placeholder="t('auth.phoneNumberPlaceholder')"
                @input="
                  (e: Event) => {
                    const input = e.target as HTMLInputElement
                    const formattedValue = formatNorwegianPhoneNumber(input.value)
                    input.value = formattedValue
                    setValue(formattedValue)
                  }
                " />
            </div>
          </div>
        </FormControl>
        <p class="helper-text">{{ t('auth.phoneNumberHint') }}</p>
        <FormMessage class="form-message" />
      </FormItem>
    </FormField>

    <div class="address-container">
      <h3 class="address-header">{{ t('auth.address') }}</h3>

      <FormField v-slot="{ componentField }" name="city">
        <FormItem class="form-item city-item">
          <FormLabel class="form-label">{{ t('auth.city') }}</FormLabel>
          <FormControl>
            <Input type="text" v-bind="componentField" :placeholder="t('auth.cityPlaceholder')"
              v-model="city" />
          </FormControl>
          <FormMessage class="form-message" />
        </FormItem>
      </FormField>

      <FormField v-slot="{ componentField }" name="streetAddress">
        <FormItem>
          <FormLabel class="form-label">{{ t('auth.streetAddress') }} </FormLabel>
          <FormControl>
            <div class="input-container">
              <MapPin class="input-icon" />
              <Input type="text" v-bind="componentField" v-model="streetAddress"
                :placeholder="t('auth.streetAddressPlaceholder')" class="input-with-icon" />
            </div>
          </FormControl>
          <FormMessage class="form-message" />
        </FormItem>
      </FormField>

      <div class="postal-city-row">
        <FormField v-slot="{ componentField }" name="postalCode">
          <FormItem class="form-item postal-code-item">
            <FormLabel class="form-label">{{ t('auth.postalCode') }} </FormLabel>
            <FormControl>
              <Input type="text" v-bind="componentField" v-model="postalCode"
                :placeholder="t('auth.postalCodePlaceholder')" maxlength="4" class="postal-input" />
            </FormControl>
            <FormMessage class="form-message" />
          </FormItem>
        </FormField>
        <div variant="secondary" class="rounded-md location-status-badge">
          <InfoIcon v-if="locationValid === 'empty'" style="color: black" />
          <CircleCheckIcon v-if="locationValid === 'found'"
            style="color: var(--success)" />
          <CircleAlertIcon v-if="locationValid === 'not-found'"
            style="color: var(--failure)" />
          <span>{{ locationValidText }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("./shared.css");
.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.75rem;
}

.phone-field {
  display: flex;
  align-items: center;
  border: 1px solid hsl(var(--input));
  border-radius: var(--radius);
  overflow: hidden;
  width: 100%;
}

.country-flag {
  width: 1.25rem;
  height: 1.25rem;
}

.helper-text {
  font-size: 0.75rem;
  color: hsl(var(--muted-foreground));
  margin-top: 0.25rem;
}

.address-container {
  margin-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  padding-top: 1rem;
  border-top: 1px solid hsl(var(--border));
}

.address-header {
  font-size: 1.05rem;
  font-weight: 600;
  color: hsl(var(--foreground));
  margin: 0 0 0.25rem 0;
}

.postal-city-row {
  display: flex;
  gap: 1rem;
}

.postal-code-item {
  flex: 0 0 35%;
}

.city-item {
  flex: 1;
}

.postal-input {
  text-align: center;
  font-family: var(--font-mono);
  letter-spacing: 0.1em;
}

.country-prefix {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0 0.75rem;
  background: hsl(var(--accent));
  height: 2.5rem;
  font-weight: 500;
  border-right: 1px solid hsl(var(--border));
}

.location-status-badge {
  background-color: var(--secondary);
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 4px;
  height: fit-content;
  align-self: flex-end;
  padding: 0.5rem 1rem;
}
</style>
<script lang="ts" setup>
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import {  Lock } from 'lucide-vue-next'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n()
</script>

<template>
  <FormField v-slot="{ componentField }" name="password">
    <FormItem>
      <FormLabel class="form-label">{{ t('auth.password') }}</FormLabel>
      <FormControl>
        <div class="input-container">
          <Lock class="input-icon" />
          <Input type="password" v-bind="componentField" :placeholder="t('auth.passwordPlaceholder')"
            class="input-with-icon" />
        </div>
      </FormControl>
      <FormMessage class="form-message" />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="confirmPassword">
    <FormItem>
      <FormLabel class="form-label">{{ t('auth.confirmPassword') }}</FormLabel>
      <FormControl>
        <div class="input-container">
          <Lock class="input-icon" />
          <Input type="password" v-bind="componentField"
            :placeholder="t('auth.confirmPasswordPlaceholder')" class="input-with-icon" />
        </div>
      </FormControl>
      <FormMessage class="form-message" />
    </FormItem>
  </FormField>
</template>

<style lang="css" scoped>
@import url("./shared.css");
</style>
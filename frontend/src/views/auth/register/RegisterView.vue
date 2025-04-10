<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { Mail, Lock, UserIcon, MapPin, PhoneIcon } from 'lucide-vue-next'
import { toTypedSchema } from '@vee-validate/zod'
import type { SubmissionHandler } from 'vee-validate'
import * as z from 'zod'
import { useTypedI18n } from '@/i18n'
import { useUrlSearchParams } from '@vueuse/core'
import { useAuthStore } from '@/stores/auth'
import { toast } from 'vue-sonner'

const { t } = useTypedI18n()

const registerZodSchema = z.object({
  name: z.string().min(2, t('auth.validation.nameRequired')),
  email: z.string().email(t('auth.validation.emailInvalid')),
  phone: z.string().regex(/^\d{8}$/, t('auth.validation.phoneInvalid')),
  address: z.object({
    street: z.string().min(1, t('auth.validation.streetAddress')),
    postalCode: z.string().regex(/^\d+$/).min(1, t('auth.validation.postalCode')),
    city: z.string().min(1, t('auth.validation.city')),
  }),

  secret: z
    .object({
      password: z
        .string()
        .min(8, t('auth.validation.passwordMin'))
        .regex(/[A-Z]/, t('auth.validation.passwordUpper'))
        .regex(/[a-z]/, t('auth.validation.passwordLower'))
        .regex(/[0-9]/, t('auth.validation.passwordNumber')),
      confirm: z.string(),
    })
    .refine((v) => v.password === v.confirm, {
      message: t('auth.validation.passwordsNotMatch'),
      path: ['confirm'],
    }),
})

const searchParams = useUrlSearchParams('history')
const registerSchema = toTypedSchema(registerZodSchema)
const authStore = useAuthStore()
const router = useRouter()

const onRegisterSubmit: SubmissionHandler = async (values) => {
  const info = registerZodSchema.parse(values);
  const redirectUrl = searchParams.redirect as string | undefined
  console.log(values);

  const result = await authStore.register({
    name: info.name,
    email: info.email,
    password: info.secret.password,
    phoneNumber: info.phone,
    street: info.address.street,
    postalCode: info.address.postalCode,
    city: info.address.city
  })

  if (!result.success) {
    toast.error(t('auth.register.error'))
    return
  }

  toast.success("Registration successful", {
    description: "Welcome to Amazoom!",
  });
  router.push(redirectUrl ?? "/")
}
</script>

<template>
  <div class="container center-content">
    <Card class="form-card">
      <CardHeader>
        <CardTitle>{{ t('auth.register.title') }}</CardTitle>
        <CardDescription>{{ t('auth.register.description') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <Button as="a" href="/api/auth/vipps/login" class="login-vipps">
          {{ t('auth.register.vipps') }}
        </Button>
        <Form
          v-slot="{ meta }"
          :validation-schema="registerSchema"
          @submit="onRegisterSubmit"
          class="register-form"
        >
          <FormField v-slot="{ componentField }" name="name">
            <FormItem class="form-item">
              <FormLabel>{{ t('auth.fullName') }}</FormLabel>
              <FormControl>
                <div class="input-with-icon">
                  <UserIcon />
                  <Input
                    type="text"
                    v-bind="componentField"
                    :placeholder="t('auth.fullNamePlaceholder')"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <FormField v-slot="{ componentField }" name="email">
            <FormItem class="form-item">
              <FormLabel>{{ t('auth.email') }}</FormLabel>
              <FormControl>
                <div class="input-with-icon">
                  <Mail />
                  <Input
                    type="email"
                    v-bind="componentField"
                    :placeholder="t('auth.emailPlaceholder')"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>
          
          <FormField v-slot="{ componentField }" name="phone">
            <FormItem class="form-item">
              <FormLabel>{{ t('auth.phoneNumber') }}</FormLabel>
              <FormControl>
                <div class="input-with-icon">
                  <PhoneIcon />
                  <Input
                    type="email"
                    v-bind="componentField"
                    :placeholder="t('auth.phoneNumberPlaceholder')"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <FormField v-slot="{ componentField }" name="address.street">
            <FormItem class="form-item">
              <FormLabel>{{ t('auth.streetAddress') }}</FormLabel>
              <FormControl>
                <div class="input-with-icon">
                  <MapPin />
                  <Input
                    type="text"
                    v-bind="componentField"
                    :placeholder="t('auth.streetAddressPlaceholder')"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <div class="split-input">
            <FormField v-slot="{ componentField }" name="address.city">
              <FormItem class="form-item">
                <FormLabel>{{ t('auth.city') }}</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    v-bind="componentField"
                    :placeholder="t('auth.cityPlaceholder')"
                  />
                </FormControl>
                <FormMessage class="form-message" />
              </FormItem>
            </FormField>
            <FormField v-slot="{ componentField }" name="address.postalCode">
              <FormItem class="form-item">
                <FormLabel>{{ t('auth.postalCode') }}</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    v-bind="componentField"
                    :placeholder="t('auth.postalCodePlaceholder')"
                  />
                </FormControl>
                <FormMessage class="form-message" />
              </FormItem>
            </FormField>
          </div>

          <FormField v-slot="{ componentField }" name="secret.password">
            <FormItem>
              <FormLabel>{{ t('auth.password') }}</FormLabel>
              <FormControl>
                <div class="input-with-icon">
                  <Lock />
                  <Input
                    type="password"
                    v-bind="componentField"
                    :placeholder="t('auth.login.passwordPlaceholder')"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <FormField v-slot="{ componentField }" name="secret.confirm">
            <FormItem>
              <FormLabel>{{ t('auth.confirmPassword') }}</FormLabel>
              <FormControl>
                <div class="input-with-icon">
                  <Lock />
                  <Input
                    type="password"
                    v-bind="componentField"
                    :placeholder="t('auth.confirmPasswordPlaceholder')"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <Button type="submit" size="lg" class="login-button" :disabled="!meta.valid || meta.pending">
            {{ t('auth.register.button') }}
          </Button>


          <div class="center-text">
            {{ t('auth.register.haveAccount') }}
            <RouterLink :to="{ name: 'login' }" class="login-text">
              {{ t('auth.register.loginLink') }}
            </RouterLink>
          </div>
        </Form>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.center-content {
  display: flex;
  justify-content: center;
}

.form-card {
  width: 100%;
  max-width: 600px;
  margin-top: calc(var(--spacing) * 20);
  margin-bottom: calc(var(--spacing) * 20);
}

.login-vipps {
  background-color: #ff5c24 !important;
  color: white !important;
  cursor: pointer !important;
  margin-bottom: calc(var(--spacing) * 4);
  width: 100%;
}

.center-text {
  text-align: center;
}

.login-text {
  font-weight: var(--font-weight-bold);
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.input-with-icon {
  position: relative;
  width: 100%;

  svg {
    position: absolute;
    top: 50%;
    left: var(--spacing);
    transform: translateY(-50%);
    color: var(--muted-foreground);
    height: 60%;
  }

  input {
    padding-left: calc(var(--spacing) * 8);
    width: 100%;
  }
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing);
}

.split-input {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
  width: 100%;
  * {
    flex: 1;
  }
}

@media (min-width: 350px) {
  .split-input {
    align-items: start;
    flex-direction: row;
  }
}
</style>

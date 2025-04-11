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
import { Mail, Lock } from 'lucide-vue-next'
import { toTypedSchema } from '@vee-validate/zod'
import type { SubmissionHandler } from 'vee-validate'
import * as z from 'zod'
import { useTypedI18n } from '@/i18n'
import { useUrlSearchParams } from '@vueuse/core'
import { useAuthStore } from '@/stores/auth'
import { toast } from 'vue-sonner'

const { t } = useTypedI18n()

const loginZodSchema = z.object({
  email: z.string().email(t('auth.validation.emailInvalid')),
  password: z.string().min(1, t('auth.validation.passwordRequired')),
})

const searchParams = useUrlSearchParams('history')

const loginSchema = toTypedSchema(loginZodSchema)
const authStore = useAuthStore()
const router = useRouter()

const onLoginSubmit: SubmissionHandler = async (values) => {
  const redirectUrl = searchParams.redirect as string | undefined
  const result = await authStore.login({
    email: values.email,
    password: values.password,
  })

  if (result.success) {
    router.push(redirectUrl || '/')
    toast.success('Login successful', {
      description: 'Welcome back!',
    })
  } else {
    toast.error('Failed to login', {
      description: 'Check your credentials and try again.',
    })
  }
}
</script>

<template>
  <div class="container center-content">
    <Card class="form-card">
      <CardHeader>
        <CardTitle>{{ t('auth.login.title') }}</CardTitle>
        <CardDescription>{{ t('auth.login.description') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <Form
          v-slot="{ meta }"
          :validation-schema="loginSchema"
          @submit="onLoginSubmit"
          class="login-form"
        >
          <FormField v-slot="{ componentField }" name="email">
            <FormItem>
              <FormLabel>{{ t('auth.email') }}</FormLabel>
              <FormControl>
                <div class="input-container">
                  <Mail class="input-icon" />
                  <Input
                    type="email"
                    v-bind="componentField"
                    :placeholder="t('auth.emailPlaceholder')"
                    class="input-with-icon"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <FormField v-slot="{ componentField }" name="password">
            <FormItem>
              <FormLabel>{{ t('auth.password') }}</FormLabel>
              <FormControl>
                <div class="input-container">
                  <Lock class="input-icon" />
                  <Input
                    type="password"
                    v-bind="componentField"
                    :placeholder="t('auth.login.passwordPlaceholder')"
                    class="input-with-icon"
                  />
                </div>
              </FormControl>
              <FormMessage class="form-message" />
            </FormItem>
          </FormField>

          <Button type="submit" size="lg" class="login-button" :disabled="!meta.valid">
            {{ t('auth.login.button') }}
          </Button>

          <span class="center-text">or login with</span>

          <Button as="a" href="/api/auth/vipps/login" class="login-vipps">
            {{ t('auth.login.vipps') }}
          </Button>

          <div class="center-text">
            {{ t('auth.login.noAccount') }}
            <RouterLink :to="{ name: 'register' }" class="register-text">
              {{ t('auth.login.registerLink') }}
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
}

.login-vipps {
  background-color: #ff5c24 !important;
  color: white !important;
  cursor: pointer !important;
}

.center-text {
  text-align: center;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.input-container {
  position: relative;
  width: 100%;
}

.input-icon {
  position: absolute;
  left: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  color: hsl(var(--muted-foreground));
  height: 1.25rem;
  width: 1.25rem;
}

.input-with-icon {
  padding-left: 2.5rem;
}

.form-message {
  font-size: 0.875rem;
  font-weight: 400;
}

.register-text {
  color: hsl(var(--primary));
  font-weight: 500;
  text-decoration: none;
  margin-left: 0.25rem;
}

.register-text:hover {
  text-decoration: underline;
}
</style>

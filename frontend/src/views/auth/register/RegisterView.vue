<script setup lang="ts">
import { ref } from 'vue'
import { Button } from '@/components/ui/button'
import { Form } from '@/components/ui/form'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import {
  Stepper,
  StepperItem,
  StepperTrigger,
  StepperTitle,
  StepperDescription,
  StepperSeparator,
} from '@/components/ui/stepper'
import { User, Mail, Lock, Check } from 'lucide-vue-next'
import { toTypedSchema } from '@vee-validate/zod'
import type { GenericObject } from 'vee-validate'
import * as z from 'zod'
import { useTypedI18n } from '@/i18n'
import ProfileStep from './steps/ProfileStep.vue'
import ContactInfoStep from './steps/ContactInfoStep.vue'
import SecurityStep from './steps/SecurityStep.vue'

const { t } = useTypedI18n()

const stepIndex = ref(1)
const steps = [
  {
    step: 1,
    title: t('auth.steps.profile.title'),
    description: t('auth.steps.profile.description'),
    icon: User,
  },
  {
    step: 2,
    title: t('auth.steps.contact.title'),
    description: t('auth.steps.contact.description'),
    icon: Mail,
  },
  {
    step: 3,
    title: t('auth.steps.security.title'),
    description: t('auth.steps.security.description'),
    icon: Lock,
  },
]

const norwegianPhoneRegex = /^(\d{3})\s(\d{2})\s(\d{3})$/

const formSchema = [
  z.object({
    fullName: z.string().min(2, t('auth.validation.nameMin')),
    profilePicture: z.any().optional(),
  }),
  z.object({
    email: z.string().email(t('auth.validation.emailInvalid')),
    phoneNumber: z.string().regex(norwegianPhoneRegex, t('auth.validation.phoneInvalid')),
    streetAddress: z.string().min(5, t('auth.validation.streetAddressMin')),
    postalCode: z.string().regex(/^\d{4}$/, t('auth.validation.postalCodeInvalid')),
    city: z.string().min(2, t('auth.validation.cityMin')),
  }),
  z
    .object({
      password: z
        .string()
        .min(8, t('auth.validation.passwordMin'))
        .regex(/[A-Z]/, t('auth.validation.passwordUpper'))
        .regex(/[a-z]/, t('auth.validation.passwordLower'))
        .regex(/[0-9]/, t('auth.validation.passwordNumber')),
      confirmPassword: z.string().min(8, t('auth.validation.passwordMin')),
    })
    .refine((data) => data.password === data.confirmPassword, {
      message: t('auth.validation.passwordsNotMatch'),
      path: ['confirmPassword'],
    }),
]

function onSubmit(values: GenericObject) {
  console.log('Form submitted:', values)
}
</script>

<template>
  <div class="container">
    <Card>
      <CardHeader>
        <CardTitle>{{ t('auth.createAccount') }}</CardTitle>
        <CardDescription>{{ t('auth.fillOutForm') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <Form
          v-slot="{ meta, values, validate }"
          as=""
          keep-values
          :validation-schema="toTypedSchema(formSchema[stepIndex - 1])"
        >
          <Stepper
            v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }"
            v-model="stepIndex"
            class="stepper-block"
          >
            <form
              @submit="
                (e) => {
                  e.preventDefault()
                  validate()

                  if (stepIndex === steps.length && meta.valid) {
                    onSubmit(values)
                  }
                }
              "
            >
              <div class="stepper-nav-container">
                <StepperItem
                  v-for="step in steps"
                  :key="step.step"
                  v-slot="{ state }"
                  class="stepper-item"
                  :step="step.step"
                >
                  <StepperSeparator
                    v-if="step.step !== steps[steps.length - 1].step"
                    class="stepper-separator"
                    :class="{ 'separator-completed': state === 'completed' }"
                  />

                  <StepperTrigger as-child>
                    <Button
                      :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'"
                      size="icon"
                      class="stepper-button"
                      :class="[
                        state === 'active' && 'active-button',
                        state === 'completed' && 'completed-button',
                      ]"
                      :disabled="state !== 'completed' && !meta.valid"
                    >
                      <Check v-if="state === 'completed'" class="step-icon" />
                      <span v-else class="step-number">{{ step.step }}</span>
                    </Button>
                  </StepperTrigger>

                  <div class="stepper-title-container">
                    <StepperTitle
                      :class="[
                        state === 'active' && 'active-text',
                        state === 'completed' && 'completed-text',
                      ]"
                      class="stepper-title"
                    >
                      {{ step.title }}
                    </StepperTitle>
                    <StepperDescription
                      :class="[
                        state === 'active' && 'active-text-muted',
                        state === 'completed' && 'completed-text-muted',
                      ]"
                      class="stepper-description"
                    >
                      {{ step.description }}
                    </StepperDescription>
                  </div>
                </StepperItem>
              </div>

              <div class="form-fields-container">
                <ProfileStep v-if="stepIndex === 1" />
                <ContactInfoStep v-if="stepIndex === 2" />
                <SecurityStep v-if="stepIndex === 3" />
              </div>

              <div class="form-actions">
                <Button
                  :disabled="isPrevDisabled"
                  variant="outline"
                  size="lg"
                  class="action-button back-button"
                  @click="prevStep()"
                >
                  {{ t('common.back') }}
                </Button>
                <div class="button-group">
                  <Button
                    v-if="stepIndex !== steps.length"
                    :type="meta.valid ? 'button' : 'submit'"
                    :disabled="isNextDisabled"
                    size="lg"
                    class="action-button next-button"
                    @click="meta.valid && nextStep()"
                  >
                    {{ t('common.next') }}
                  </Button>
                  <Button
                    v-if="stepIndex === steps.length"
                    size="lg"
                    type="submit"
                    class="action-button submit-button"
                  >
                    {{ t('auth.submitButton') }}
                  </Button>
                </div>
              </div>
            </form>
          </Stepper>
        </Form>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.container {
  width: 900px;
  margin: 2rem auto;
  padding: 1.5rem;
}

.stepper-block {
  display: block;
  width: 100%;
}

.stepper-nav-container {
  display: flex;
  width: 100%;
  align-items: flex-start;
  gap: 0.5rem;
  margin-bottom: 2.5rem;
  padding: 0.75rem 0.5rem;
}

.stepper-item {
  position: relative;
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.stepper-separator {
  position: absolute;
  left: calc(50% + 20px);
  right: calc(-50% + 10px);
  top: 1.25rem;
  display: block;
  height: 0.125rem;
  flex-shrink: 0;
  border-radius: 9999px;
  background-color: hsl(var(--muted));
  transition: background-color 0.3s ease;
}

.separator-completed {
  background-color: hsl(var(--primary));
}

.stepper-button {
  z-index: 10;
  border-radius: 9999px;
  flex-shrink: 0;
  transition: all 0.3s ease;
  width: 2.5rem;
  height: 2.5rem;
}

.step-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.step-number {
  font-size: 0.875rem;
  font-weight: 500;
}

.active-button {
  border: 2px solid hsl(var(--primary));
  box-shadow: 0 0 0 4px hsla(var(--primary), 0.2);
}

.completed-button {
  background-color: hsl(var(--primary));
  color: hsl(var(--primary-foreground));
}

.stepper-title-container {
  margin-top: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stepper-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: hsl(var(--foreground));
  transition: color 0.3s ease;
}

.stepper-description {
  font-size: 0.75rem;
  color: hsl(var(--muted-foreground));
  transition: color 0.3s ease;
  margin-top: 0.25rem;
}

.active-text {
  color: hsl(var(--primary));
  font-weight: 600;
}

.completed-text {
  color: hsl(var(--primary));
  font-weight: 500;
}

.active-text-muted {
  color: hsl(var(--primary) / 0.8);
}

.completed-text-muted {
  color: hsl(var(--primary) / 0.7);
}

.form-fields-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin-top: 2rem;
  padding: 0.5rem 0.25rem;
}

.form-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 2.5rem;
}

.action-button {
  padding-left: 1.5rem;
  padding-right: 1.5rem;
  height: 2.75rem;
  font-weight: 500;
}

.button-group {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

@media (min-width: 768px) {
  .container {
    padding: 2rem;
  }

  .stepper-title {
    font-size: 1rem;
  }

  .stepper-description {
    display: block;
  }

  .form-fields-container {
    padding: 0.5rem 1rem;
  }
}

@media (max-width: 640px) {
  .container {
    padding: 1rem;
  }

  .stepper-description {
    display: none;
  }

  .form-actions {
    flex-direction: column-reverse;
    gap: 1rem;
  }

  .button-group {
    width: 100%;
  }

  .button-group button {
    flex: 1;
  }

  .form-actions button:first-child {
    width: 100%;
  }

  .container {
    width: 100%;
  }

  .form-fields-container {
    width: 100%;
  }
}
</style>

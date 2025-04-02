<template>
    <div class="container">
        <Card>
            <CardHeader>
                <CardTitle>{{ t('auth.createAccount') }}</CardTitle>
                <CardDescription>{{ t('auth.fillOutForm') }}</CardDescription>
            </CardHeader>
            <CardContent>
                <Form v-slot="{ meta, values, validate }" as="" keep-values
                    :validation-schema="toTypedSchema(formSchema[stepIndex - 1])">
                    <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }" v-model="stepIndex"
                        class="stepper-block">
                        <form @submit="(e) => {
                            e.preventDefault()
                            validate()

                            if (stepIndex === steps.length && meta.valid) {
                                onSubmit(values)
                            }
                        }">
                            <div class="stepper-nav-container">
                                <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }"
                                    class="stepper-item" :step="step.step">
                                    <StepperSeparator v-if="step.step !== steps[steps.length - 1].step"
                                        class="stepper-separator"
                                        :class="{ 'separator-completed': state === 'completed' }" />

                                    <StepperTrigger as-child>
                                        <Button
                                            :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'"
                                            size="icon" class="stepper-button" :class="[state === 'active' && 'active-button',
                                            state === 'completed' && 'completed-button']"
                                            :disabled="state !== 'completed' && !meta.valid">
                                            <Check v-if="state === 'completed'" class="step-icon" />
                                            <span v-else class="step-number">{{ step.step }}</span>
                                        </Button>
                                    </StepperTrigger>

                                    <div class="stepper-title-container">
                                        <StepperTitle :class="[state === 'active' && 'active-text',
                                        state === 'completed' && 'completed-text']" class="stepper-title">
                                            {{ step.title }}
                                        </StepperTitle>
                                        <StepperDescription :class="[state === 'active' && 'active-text-muted',
                                        state === 'completed' && 'completed-text-muted']" class="stepper-description">
                                            {{ step.description }}
                                        </StepperDescription>
                                    </div>
                                </StepperItem>
                            </div>

                            <div class="form-fields-container">
                                <template v-if="stepIndex === 1">
                                    <div class="profile-upload-container">
                                        <div class="avatar-container">
                                            <Avatar class="profile-avatar">
                                                <AvatarImage :src="previewImage || ''" />
                                                <AvatarFallback class="avatar-fallback">
                                                    <User class="avatar-icon" />
                                                </AvatarFallback>
                                            </Avatar>
                                            <Label for="profile-picture" class="upload-button">
                                                <Upload class="upload-icon" />
                                            </Label>
                                            <input id="profile-picture" type="file" accept="image/*" class="hidden"
                                                @change="handleFileChange" />
                                        </div>
                                        <span class="upload-hint">{{ t('auth.uploadProfilePicture') }}</span>
                                    </div>

                                    <FormField v-slot="{ componentField }" name="fullName">
                                        <FormItem class="form-item">
                                            <FormLabel class="form-label">{{ t('auth.fullName') }}</FormLabel>
                                            <FormControl>
                                                <div class="input-container">
                                                    <User class="input-icon" />
                                                    <Input type="text" v-bind="componentField"
                                                        :placeholder="t('auth.fullNamePlaceholder')"
                                                        class="input-with-icon" />
                                                </div>
                                            </FormControl>
                                            <FormMessage class="form-message" />
                                        </FormItem>
                                    </FormField>
                                </template>

                                <!-- Step 2: Contact -->
                                <template v-if="stepIndex === 2">
                                    <div class="form-section">
                                        <FormField v-slot="{ componentField }" name="email">
                                            <FormItem class="form-item">
                                                <FormLabel class="form-label">{{ t('auth.email') }}</FormLabel>
                                                <FormControl>
                                                    <div class="input-container">
                                                        <Mail class="input-icon" />
                                                        <Input type="email" v-bind="componentField"
                                                            :placeholder="t('auth.emailPlaceholder')"
                                                            class="input-with-icon" />
                                                    </div>
                                                </FormControl>
                                                <FormMessage class="form-message" />
                                            </FormItem>
                                        </FormField>

                                        <FormField v-slot="{ componentField, setValue }" name="phoneNumber">
                                            <FormItem class="form-item">
                                                <FormLabel class="form-label">{{ t('auth.phoneNumber') }}</FormLabel>
                                                <FormControl>
                                                    <div class="phone-field">
                                                        <div class="country-prefix">
                                                            <CountryFlag code="NO" class="country-flag" />
                                                        </div>
                                                        <Input type="tel" v-bind="componentField"
                                                            :placeholder="t('auth.phoneNumberPlaceholder')" @input="(e: Event) => {
                                                                const input = e.target as HTMLInputElement;
                                                                const formattedValue = formatNorwegianPhoneNumber(input.value);
                                                                input.value = formattedValue;
                                                                setValue(formattedValue);
                                                            }" />
                                                    </div>
                                                </FormControl>
                                                <p class="helper-text">{{ t('auth.phoneNumberHint') }}</p>
                                                <FormMessage class="form-message" />
                                            </FormItem>
                                        </FormField>

                                        <div class="address-container">
                                            <h3 class="address-header">{{ t('auth.address') }}</h3>

                                            <FormField v-slot="{ componentField }" name="streetAddress">
                                                <FormItem class="form-item">
                                                    <FormLabel class="form-label">{{ t('auth.streetAddress') }}
                                                    </FormLabel>
                                                    <FormControl>
                                                        <div class="input-container">
                                                            <MapPin class="input-icon" />
                                                            <Input type="text" v-bind="componentField"
                                                                :placeholder="t('auth.streetAddressPlaceholder')"
                                                                class="input-with-icon" />
                                                        </div>
                                                    </FormControl>
                                                    <FormMessage class="form-message" />
                                                </FormItem>
                                            </FormField>

                                            <div class="postal-city-row">
                                                <FormField v-slot="{ componentField }" name="postalCode">
                                                    <FormItem class="form-item postal-code-item">
                                                        <FormLabel class="form-label">{{ t('auth.postalCode') }}
                                                        </FormLabel>
                                                        <FormControl>
                                                            <Input type="text" v-bind="componentField"
                                                                :placeholder="t('auth.postalCodePlaceholder')"
                                                                maxlength="4" class="postal-input" />
                                                        </FormControl>
                                                        <FormMessage class="form-message" />
                                                    </FormItem>
                                                </FormField>

                                                <FormField v-slot="{ componentField }" name="city">
                                                    <FormItem class="form-item city-item">
                                                        <FormLabel class="form-label">{{ t('auth.city') }}</FormLabel>
                                                        <FormControl>
                                                            <Input type="text" v-bind="componentField"
                                                                :placeholder="t('auth.cityPlaceholder')" />
                                                        </FormControl>
                                                        <FormMessage class="form-message" />
                                                    </FormItem>
                                                </FormField>
                                            </div>
                                        </div>
                                    </div>
                                </template>

                                <!-- Step 3: Security -->
                                <template v-if="stepIndex === 3">
                                    <FormField v-slot="{ componentField }" name="password">
                                        <FormItem class="form-item">
                                            <FormLabel class="form-label">{{ t('auth.password') }}</FormLabel>
                                            <FormControl>
                                                <div class="input-container">
                                                    <Lock class="input-icon" />
                                                    <Input type="password" v-bind="componentField"
                                                        :placeholder="t('auth.passwordPlaceholder')"
                                                        class="input-with-icon" />
                                                </div>
                                            </FormControl>
                                            <FormMessage class="form-message" />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="confirmPassword">
                                        <FormItem class="form-item">
                                            <FormLabel class="form-label">{{ t('auth.confirmPassword') }}</FormLabel>
                                            <FormControl>
                                                <div class="input-container">
                                                    <Lock class="input-icon" />
                                                    <Input type="password" v-bind="componentField"
                                                        :placeholder="t('auth.confirmPasswordPlaceholder')"
                                                        class="input-with-icon" />
                                                </div>
                                            </FormControl>
                                            <FormMessage class="form-message" />
                                        </FormItem>
                                    </FormField>
                                </template>
                            </div>

                            <div class="form-actions">
                                <Button :disabled="isPrevDisabled" variant="outline" size="lg"
                                    class="action-button back-button" @click="prevStep()">
                                    {{ t('common.back') }}
                                </Button>
                                <div class="button-group">
                                    <Button v-if="stepIndex !== steps.length" :type="meta.valid ? 'button' : 'submit'"
                                        :disabled="isNextDisabled" size="lg" class="action-button next-button"
                                        @click="meta.valid && nextStep()">
                                        {{ t('common.next') }}
                                    </Button>
                                    <Button v-if="stepIndex === steps.length" size="lg" type="submit"
                                        class="action-button submit-button">
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

<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { Button } from '@/components/ui/button';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Stepper, StepperItem, StepperTrigger, StepperTitle, StepperDescription, StepperIndicator, StepperSeparator } from '@/components/ui/stepper';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Upload, User, Mail, Phone, MapPin, Lock, Check, Circle, Dot } from 'lucide-vue-next';
import { toTypedSchema } from '@vee-validate/zod';
import type { GenericObject } from 'vee-validate';
import * as z from 'zod';
import Label from '@/components/ui/label/Label.vue';
import CountryFlag from '@/components/ui/country-flag/CountryFlag.vue';

const { t } = useI18n();

const stepIndex = ref(1);
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
];

const norwegianPhoneRegex = /^(\d{3})\s(\d{2})\s(\d{3})$/;

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
    z.object({
        password: z.string()
            .min(8, t('auth.validation.passwordMin'))
            .regex(/[A-Z]/, t('auth.validation.passwordUpper'))
            .regex(/[a-z]/, t('auth.validation.passwordLower'))
            .regex(/[0-9]/, t('auth.validation.passwordNumber')),
        confirmPassword: z.string().min(8, t('auth.validation.passwordMin')),
    }).refine(data => data.password === data.confirmPassword, {
        message: t('auth.validation.passwordsNotMatch'),
        path: ["confirmPassword"]
    }),
];

const previewImage = ref<string | null>(null);

const handleFileChange = (e: Event) => {
    const input = e.target as HTMLInputElement;
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = (e) => {
            previewImage.value = e.target?.result as string;
        };
        reader.readAsDataURL(input.files[0]);
    }
};

function onSubmit(values: GenericObject) {
    console.log('Form submitted:', values);
}

const formatNorwegianPhoneNumber = (input: string): string => {
    const digits = input.replace(/\D/g, '');

    let formatted = '';
    if (digits.length <= 3) {
        formatted = digits;
    } else if (digits.length <= 5) {
        formatted = `${digits.slice(0, 3)} ${digits.slice(3)}`;
    } else {
        formatted = `${digits.slice(0, 3)} ${digits.slice(3, 5)} ${digits.slice(5, 8)}`;
    }

    return formatted;
};
</script>

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

.profile-upload-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1.5rem;
}

.avatar-container {
    position: relative;
}

.profile-avatar {
    height: 6rem;
    width: 6rem;
    border: 4px solid hsl(var(--muted));
    background-color: hsl(var(--background));
}

.avatar-fallback {
    background-color: hsla(var(--primary), 0.1);
    color: hsl(var(--primary));
}

.avatar-icon {
    height: 3rem;
    width: 3rem;
}

.upload-button {
    position: absolute;
    bottom: 0;
    right: 0;
    background-color: var(--secondary);
    color: hsl(var(--secondary-foreground));
    border-radius: 9999px;
    padding: 0.5rem;
    cursor: pointer;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    transition: background-color 0.3s ease;
}

.upload-icon {
    height: 1rem;
    width: 1rem;
}

.upload-hint {
    font-size: 0.875rem;
    color: hsl(var(--muted-foreground));
}

.hidden {
    display: none;
}

.form-item {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.form-label {
    font-weight: 500;
}

.input-container {
    position: relative;
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

.country-flag {
    width: 1.25rem;
    height: 1.25rem;
}

.phone-number-input {
    flex: 1;
    border: none;
    box-shadow: none;
}

.phone-number-input:focus {
    box-shadow: none;
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
}
</style>
<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'
import { Stepper, StepperDescription, StepperItem, StepperSeparator, StepperTitle, StepperTrigger, StepperIndicator } from '@/components/ui/stepper'
import { toTypedSchema } from '@vee-validate/zod'
import { Check, Circle, Dot, BookUser, CreditCard, Truck } from 'lucide-vue-next'
import type { GenericObject } from 'vee-validate'
import { h, ref } from 'vue'
import * as z from 'zod'

const formSchema = [
    z.object({
        fullName: z.string(),
        email: z.string().email(),
    }),
    z.object({
        password: z.string().min(2).max(50),
        confirmPassword: z.string(),
    }).refine(
        (values) => {
            return values.password === values.confirmPassword
        },
        {
            message: 'Passwords must match!',
            path: ['confirmPassword'],
        },
    ),
    z.object({
        favoriteDrink: z.union([z.literal('coffee'), z.literal('tea'), z.literal('soda')]),
    }),
]

const stepIndex = ref(1)
const steps = [
    {
        step: 1,
        title: 'Your details',
        description: 'Provide your name and email',
    },
    {
        step: 2,
        title: 'Your password',
        description: 'Choose a password',
    },
    {
        step: 3,
        title: 'Your Favorite Drink',
        description: 'Choose a drink',
    },
]

const checkoutSteps = [{
    step: 1,
    title: 'Address',
    description: 'Add your address here',
    icon: BookUser,
}, {
    step: 2,
    title: 'Shipping',
    description: 'Set your preferred shipping method',
    icon: Truck,
}, {
    step: 3,
    title: 'Payment',
    description: 'Add any payment information you have',
    icon: CreditCard,
}, {
    step: 4,
    title: 'Checkout',
    description: 'Confirm your order',
    icon: Check,
}]

function onSubmit(values: GenericObject) {
    console.log('Form submitted:', values)
}
</script>

<template>
    <section class="component-section">
        <h2>Steppers</h2>
        <div class="component-grid">
            <div class="component-example">
                <h3>Horizontal Stepper</h3>
                <div class="example-content">
                    <Stepper class="horizontal-stepper">
                        <StepperItem v-for="item in checkoutSteps" :key="item.step" class="basis-1/4" :step="item.step">
                            <StepperTrigger>
                                <StepperIndicator>
                                    <component :is="item.icon" class="stepper-icon" />
                                </StepperIndicator>
                                <div class="stepper-content">
                                    <StepperTitle>
                                        {{ item.title }}
                                    </StepperTitle>
                                    <StepperDescription>
                                        {{ item.description }}
                                    </StepperDescription>
                                </div>
                            </StepperTrigger>
                            <StepperSeparator v-if="item.step !== checkoutSteps[checkoutSteps.length - 1].step"
                                class="stepper-separator" />
                        </StepperItem>
                    </Stepper>
                </div>
            </div>

            <div class="component-example">
                <h3>Form Stepper</h3>
                <div class="example-content">
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
                                            class="stepper-form-separator" />

                                        <StepperTrigger as-child>
                                            <Button
                                                :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'"
                                                size="icon" class="stepper-button"
                                                :class="[state === 'active' && 'active-button']"
                                                :disabled="state !== 'completed' && !meta.valid">
                                                <Check v-if="state === 'completed'" class="stepper-icon-small" />
                                                <Circle v-if="state === 'active'" />
                                                <Dot v-if="state === 'inactive'" />
                                            </Button>
                                        </StepperTrigger>

                                        <div class="stepper-title-container">
                                            <StepperTitle :class="[state === 'active' && 'active-text']"
                                                class="stepper-title">
                                                {{ step.title }}
                                            </StepperTitle>
                                            <StepperDescription :class="[state === 'active' && 'active-text']"
                                                class="stepper-description">
                                                {{ step.description }}
                                            </StepperDescription>
                                        </div>
                                    </StepperItem>
                                </div>

                                <div class="form-fields-container">
                                    <template v-if="stepIndex === 1">
                                        <FormField v-slot="{ componentField }" name="fullName">
                                            <FormItem>
                                                <FormLabel>Full Name</FormLabel>
                                                <FormControl>
                                                    <Input type="text" v-bind="componentField" />
                                                </FormControl>
                                                <FormMessage />
                                            </FormItem>
                                        </FormField>

                                        <FormField v-slot="{ componentField }" name="email">
                                            <FormItem>
                                                <FormLabel>Email</FormLabel>
                                                <FormControl>
                                                    <Input type="email" v-bind="componentField" />
                                                </FormControl>
                                                <FormMessage />
                                            </FormItem>
                                        </FormField>
                                    </template>

                                    <template v-if="stepIndex === 2">
                                        <FormField v-slot="{ componentField }" name="password">
                                            <FormItem>
                                                <FormLabel>Password</FormLabel>
                                                <FormControl>
                                                    <Input type="password" v-bind="componentField" />
                                                </FormControl>
                                                <FormMessage />
                                            </FormItem>
                                        </FormField>

                                        <FormField v-slot="{ componentField }" name="confirmPassword">
                                            <FormItem>
                                                <FormLabel>Confirm Password</FormLabel>
                                                <FormControl>
                                                    <Input type="password" v-bind="componentField" />
                                                </FormControl>
                                                <FormMessage />
                                            </FormItem>
                                        </FormField>
                                    </template>

                                    <template v-if="stepIndex === 3">
                                        <FormField v-slot="{ componentField }" name="favoriteDrink">
                                            <FormItem>
                                                <FormLabel>Drink</FormLabel>
                                                <Select v-bind="componentField">
                                                    <FormControl>
                                                        <SelectTrigger>
                                                            <SelectValue placeholder="Select a drink" />
                                                        </SelectTrigger>
                                                    </FormControl>
                                                    <SelectContent>
                                                        <SelectGroup>
                                                            <SelectItem value="coffee">Coffee</SelectItem>
                                                            <SelectItem value="tea">Tea</SelectItem>
                                                            <SelectItem value="soda">Soda</SelectItem>
                                                        </SelectGroup>
                                                    </SelectContent>
                                                </Select>
                                                <FormMessage />
                                            </FormItem>
                                        </FormField>
                                    </template>
                                </div>

                                <div class="form-actions">
                                    <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()">
                                        Back
                                    </Button>
                                    <div class="button-group">
                                        <Button v-if="stepIndex !== 3" :type="meta.valid ? 'button' : 'submit'"
                                            :disabled="isNextDisabled" size="sm" @click="meta.valid && nextStep()">
                                            Next
                                        </Button>
                                        <Button v-if="stepIndex === 3" size="sm" type="submit">
                                            Submit
                                        </Button>
                                    </div>
                                </div>
                            </form>
                        </Stepper>
                    </Form>
                </div>
            </div>
        </div>
    </section>
</template>

<style scoped>
.component-section {
    margin: 2rem 0;
}

h2 {
    font-size: 1.8rem;
    font-weight: 600;
    margin-bottom: 1.5rem;
}

h3 {
    font-size: 1.4rem;
    font-weight: 500;
    margin-bottom: 1rem;
}

.component-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 2rem;
}

@media (min-width: 768px) {
    .component-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

.component-example {
    background-color: #f9f9f9;
    border: 1px solid #e0e0e0;
    border-radius: 0.5rem;
    padding: 1.5rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.example-content {
    margin-top: 1rem;
    width: 100%;
}

.horizontal-stepper {
    display: flex;
    width: 100%;
    gap: 1rem;
}

.stepper-icon {
    width: 1.25rem;
    height: 1.25rem;
}

.stepper-content {
    display: flex;
    flex-direction: column;
    margin-left: 0.75rem;
}

.stepper-separator {
    width: 100%;
    height: 1px;
    background-color: #e0e0e0;
    margin-top: 0.75rem;
}

/* New CSS for Form Stepper */
.stepper-block {
    display: block;
    width: 100%;
}

.stepper-nav-container {
    display: flex;
    width: 100%;
    align-items: flex-start;
    gap: 0.5rem;
}

.stepper-item {
    position: relative;
    display: flex;
    width: 100%;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.stepper-form-separator {
    position: absolute;
    left: calc(50% + 20px);
    right: calc(-50% + 10px);
    top: 1.25rem;
    display: block;
    height: 0.125rem;
    flex-shrink: 0;
    border-radius: 9999px;
    background-color: #e0e0e0;
}

.stepper-button {
    z-index: 10;
    border-radius: 9999px;
    flex-shrink: 0;
}

.active-button {
    border: 2px solid #0070f3;
    outline: 2px solid #e0e0e0;
    outline-offset: 2px;
}

.stepper-icon-small {
    width: 1.25rem;
    height: 1.25rem;
}

.stepper-title-container {
    margin-top: 1.25rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.stepper-title {
    font-size: 0.875rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.stepper-description {
    display: none;
    font-size: 0.75rem;
    color: #666;
    transition: all 0.3s ease;
}

.active-text {
    color: #0070f3;
}

.form-fields-container {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin-top: 1rem;
}

.form-actions {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 1rem;
}

.button-group {
    display: flex;
    align-items: center;
    gap: 0.75rem;
}

@media (min-width: 768px) {
    .stepper-title {
        font-size: 1rem;
    }

    .stepper-description {
        display: block;
    }
}

@media (max-width: 640px) {
    .stepper-description {
        display: none;
    }
}
</style>
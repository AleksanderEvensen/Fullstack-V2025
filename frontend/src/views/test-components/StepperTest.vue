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

function onSubmit(values: any) {
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
                            class="block w-full">
                            <form @submit="(e) => {
                                e.preventDefault()
                                validate()

                                if (stepIndex === steps.length && meta.valid) {
                                    onSubmit(values)
                                }
                            }">
                                <div class="flex w-full flex-start gap-2">
                                    <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }"
                                        class="relative flex w-full flex-col items-center justify-center"
                                        :step="step.step">
                                        <StepperSeparator v-if="step.step !== steps[steps.length - 1].step"
                                            class="absolute left-[calc(50%+20px)] right-[calc(-50%+10px)] top-5 block h-0.5 shrink-0 rounded-full bg-muted group-data-[state=completed]:bg-primary" />

                                        <StepperTrigger as-child>
                                            <Button
                                                :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'"
                                                size="icon" class="z-10 rounded-full shrink-0"
                                                :class="[state === 'active' && 'ring-2 ring-ring ring-offset-2 ring-offset-background']"
                                                :disabled="state !== 'completed' && !meta.valid">
                                                <Check v-if="state === 'completed'" class="size-5" />
                                                <Circle v-if="state === 'active'" />
                                                <Dot v-if="state === 'inactive'" />
                                            </Button>
                                        </StepperTrigger>

                                        <div class="mt-5 flex flex-col items-center text-center">
                                            <StepperTitle :class="[state === 'active' && 'text-primary']"
                                                class="text-sm font-semibold transition lg:text-base">
                                                {{ step.title }}
                                            </StepperTitle>
                                            <StepperDescription :class="[state === 'active' && 'text-primary']"
                                                class="sr-only text-xs text-muted-foreground transition md:not-sr-only lg:text-sm">
                                                {{ step.description }}
                                            </StepperDescription>
                                        </div>
                                    </StepperItem>
                                </div>

                                <div class="flex flex-col gap-4 mt-4">
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

                                <div class="flex items-center justify-between mt-4">
                                    <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()">
                                        Back
                                    </Button>
                                    <div class="flex items-center gap-3">
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
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 6);
}

.component-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: calc(var(--spacing) * 6);
}

.component-example {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 4);
    padding: calc(var(--spacing) * 4);
    background-color: var(--background);
    border: 1px solid var(--border);
    border-radius: var(--radius);
}

.example-content {
    display: flex;
    flex-wrap: wrap;
    gap: calc(var(--spacing) * 2);
    align-items: flex-start;
    width: 100%;
}

.horizontal-stepper {
    display: flex;
    width: 100%;
    gap: calc(var(--spacing) * 2);
}

.stepper-icon {
    width: calc(var(--spacing) * 4);
    height: calc(var(--spacing) * 4);
}

.stepper-content {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 1);
    margin-left: calc(var(--spacing) * 2);
}

.stepper-separator {
    width: 100%;
    height: 1px;
    background-color: var(--border);
    margin-top: calc(var(--spacing) * 2);
}

@media (max-width: 768px) {
    .stepper-description {
        display: none;
    }
}
</style>
<script setup lang="ts">
import { ref } from 'vue'
import * as z from 'zod'
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'

const formSchema = z.object({
    username: z.string()
        .min(3, 'Username must be at least 3 characters')
        .max(50, 'Username must be less than 50 characters'),
    email: z.string()
        .email('Please enter a valid email address'),
    password: z.string()
        .min(6, 'Password must be at least 6 characters')
        .max(100, 'Password must be less than 100 characters')
})

type FormData = z.infer<typeof formSchema>

const form = useForm<FormData>({
    validationSchema: toTypedSchema(formSchema),
})

// For the states example
const readOnlyValue = ref('This input is read-only')
</script>

<template>
    <div class="test-container">
        <h2>Form</h2>
        <p class="test-description">Form components with Zod validation.</p>

        <div class="test-cases">
            <!-- Basic Form -->
            <div class="test-case">
                <h3>Basic Form</h3>
                <p class="test-case-description">A form with Zod validation schema.</p>

                <Form @submit="form.handleSubmit((values) => console.log('Form submitted:', values))"
                    class="form-container">
                    <FormField name="username">
                        <template #default="{ field, errors }">
                            <FormItem>
                                <FormLabel>Username</FormLabel>
                                <FormControl :data-error="!!errors">
                                    <Input v-bind="field" placeholder="Enter username" />
                                </FormControl>
                                <FormDescription>
                                    Choose a username that is at least 3 characters long.
                                </FormDescription>
                                <FormMessage>{{ errors?.[0] }}</FormMessage>
                            </FormItem>
                        </template>
                    </FormField>

                    <FormField name="email">
                        <template #default="{ field, errors }">
                            <FormItem>
                                <FormLabel>Email</FormLabel>
                                <FormControl :data-error="!!errors">
                                    <Input v-bind="field" type="email" placeholder="Enter email" />
                                </FormControl>
                                <FormDescription>
                                    Enter your email address.
                                </FormDescription>
                                <FormMessage>{{ errors?.[0] }}</FormMessage>
                            </FormItem>
                        </template>
                    </FormField>

                    <FormField name="password">
                        <template #default="{ field, errors }">
                            <FormItem>
                                <FormLabel>Password</FormLabel>
                                <FormControl :data-error="!!errors">
                                    <Input v-bind="field" type="password" placeholder="Enter password" />
                                </FormControl>
                                <FormDescription>
                                    Password must be at least 6 characters long.
                                </FormDescription>
                                <FormMessage>{{ errors?.[0] }}</FormMessage>
                            </FormItem>
                        </template>
                    </FormField>

                    <Button type="submit" class="submit-button">Submit</Button>
                </Form>
            </div>

            <!-- Form States -->
            <div class="test-case">
                <h3>Form States</h3>
                <p class="test-case-description">Different states of form components.</p>

                <Form class="states-container">
                    <!-- Disabled State -->
                    <FormField name="disabled">
                        <FormItem>
                            <FormLabel>Disabled Input</FormLabel>
                            <FormControl>
                                <Input disabled placeholder="This input is disabled" />
                            </FormControl>
                            <FormDescription>
                                This input cannot be modified.
                            </FormDescription>
                        </FormItem>
                    </FormField>

                    <!-- Read-only State -->
                    <FormField name="readonly">
                        <FormItem>
                            <FormLabel>Read-only Input</FormLabel>
                            <FormControl>
                                <Input v-model="readOnlyValue" readonly />
                            </FormControl>
                            <FormDescription>
                                This input can only be read.
                            </FormDescription>
                        </FormItem>
                    </FormField>
                </Form>
            </div>
        </div>
    </div>
</template>

<style scoped>
.test-container {
    padding: calc(var(--spacing) * 4);
}

h2 {
    font-size: var(--font-size-2xl);
    font-weight: var(--font-weight-bold);
    margin-bottom: calc(var(--spacing) * 2);
    color: var(--foreground);
}

.test-description {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    margin-bottom: calc(var(--spacing) * 6);
}

.test-cases {
    display: grid;
    gap: calc(var(--spacing) * 8);
}

.test-case {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 4);
}

h3 {
    font-size: var(--font-size-lg);
    font-weight: var(--font-weight-semibold);
    color: var(--foreground);
}

.test-case-description {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    margin-bottom: calc(var(--spacing) * 4);
}

.form-container {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 4);
    max-width: calc(var(--spacing) * 120);
}

.states-container {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 4);
    max-width: calc(var(--spacing) * 120);
}

.submit-button {
    margin-top: calc(var(--spacing) * 2);
}

@media (max-width: 768px) {

    .form-container,
    .states-container {
        max-width: 100%;
    }
}
</style>
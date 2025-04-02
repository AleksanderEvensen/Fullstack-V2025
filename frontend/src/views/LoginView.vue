<template>
    <div class="container">
        <Card>
            <CardHeader>
                <CardTitle>{{ t('auth.login.title') }}</CardTitle>
                <CardDescription>{{ t('auth.login.description') }}</CardDescription>
            </CardHeader>
            <CardContent>
                <Form v-slot="{ meta, errors }" :validation-schema="loginSchema" @submit="onLoginSubmit"
                    class="login-form">
                    <FormField v-slot="{ componentField }" name="email">
                        <FormItem>
                            <FormLabel>{{ t('auth.email') }}</FormLabel>
                            <FormControl>
                                <div class="input-container">
                                    <Mail class="input-icon" />
                                    <Input type="email" v-bind="componentField"
                                        :placeholder="t('auth.emailPlaceholder')" class="input-with-icon" />
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
                                    <Input type="password" v-bind="componentField"
                                        :placeholder="t('auth.login.passwordPlaceholder')" class="input-with-icon" />
                                </div>
                            </FormControl>
                            <FormMessage class="form-message" />
                        </FormItem>
                    </FormField>



                    <Button type="submit" size="lg" class="login-button" :disabled="!meta.valid">
                        {{ t('auth.login.button') }}
                    </Button>

                    <div class="register-link">
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

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { RouterLink } from 'vue-router';
import { Button } from '@/components/ui/button';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
import { Mail, Lock } from 'lucide-vue-next';
import { toTypedSchema } from '@vee-validate/zod';
import type { GenericObject, SubmissionHandler } from 'vee-validate';
import * as z from 'zod';

const { t } = useI18n();

const loginZodSchema = z.object({
    email: z.string().email(t('auth.validation.emailInvalid')),
    password: z.string().min(1, t('auth.login.validation.passwordRequired')),
});

const loginSchema = toTypedSchema(loginZodSchema);

const onLoginSubmit: SubmissionHandler = (values) => {
    console.log('Login form submitted:', values);
};
</script>

<style scoped>
.container {
    width: 500px;
    margin: 2rem auto;
    padding: 1.5rem;
}

.login-form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
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



.register-text {
    color: hsl(var(--primary));
    font-weight: 500;
    text-decoration: none;
    margin-left: 0.25rem;
}

.register-text:hover {
    text-decoration: underline;
}

@media (max-width: 640px) {
    .container {
        width: 100%;
        padding: 1rem;
    }

}
</style>
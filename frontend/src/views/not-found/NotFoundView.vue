<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
import { useTypedI18n } from '@/i18n';
const { t } = useTypedI18n()
</script>

<template>
    <div class="not-found">
        <div class="not-found-content">
            <h1 class="not-found-title">{{ t('notFound.title') }}</h1>
            <p class="not-found-message">{{ t('notFound.message') }}</p>
            <Button as-child variant="default" size="lg">
                <RouterLink to="/">
                    {{ t('notFound.backToHome') }}
                </RouterLink>
            </Button>
        </div>
    </div>
</template>

<style scoped lang="css">
.not-found {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: calc(100vh - 64px);
    /* Account for navbar height */
    padding: 2rem;
    background-color: var(--background);
}

.not-found-content {
    text-align: center;
    max-width: 600px;
    padding: 2rem;
    border-radius: 0.5rem;
    background-color: var(--card);
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.not-found-title {
    font-size: 2.5rem;
    font-weight: 700;
    color: var(--foreground);
    margin-bottom: 1rem;
}

.not-found-message {
    font-size: 1.125rem;
    color: var(--muted-foreground);
    margin-bottom: 2rem;
}

.not-found a {
    text-decoration: none;
    color: inherit;
}
</style>
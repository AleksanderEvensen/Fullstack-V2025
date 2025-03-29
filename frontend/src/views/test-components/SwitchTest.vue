<script setup lang="ts">
import { ref } from 'vue'
import { Switch } from '@/components/ui/switch'

const enabled = ref(false)
const notifications = ref(true)
const disabledSwitch = ref(true)
</script>

<template>
    <div class="switch-test">
        <h2>Switch Component</h2>

        <div class="switch-examples">
            <!-- Basic Switch -->
            <div class="switch-example">
                <h3>Basic Switch</h3>
                <p class="switch-description">A basic switch with a label.</p>
                <div class="switch-with-label">
                    <Switch v-model="enabled" />
                    <label>Airplane Mode</label>
                </div>
                <p class="switch-value">Value: {{ enabled }}</p>
            </div>

            <!-- Default Checked -->
            <div class="switch-example">
                <h3>Default Checked</h3>
                <p class="switch-description">A switch that is checked by default.</p>
                <div class="switch-with-label">
                    <Switch v-model="notifications" />
                    <label>Enable notifications</label>
                </div>
                <p class="switch-value">Value: {{ notifications }}</p>
            </div>

            <!-- Disabled State -->
            <div class="switch-example">
                <h3>Disabled State</h3>
                <p class="switch-description">A switch that cannot be toggled.</p>
                <div class="switch-with-label">
                    <Switch v-model="disabledSwitch" disabled />
                    <label>Disabled switch</label>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.switch-test {
    padding: calc(var(--spacing) * 4);
}

h2 {
    font-size: var(--font-size-2xl);
    font-weight: var(--font-weight-bold);
    margin-bottom: calc(var(--spacing) * 4);
    color: var(--foreground);
}

h3 {
    font-size: var(--font-size-lg);
    font-weight: var(--font-weight-semibold);
    margin-bottom: calc(var(--spacing) * 2);
    color: var(--foreground);
}

.switch-examples {
    display: grid;
    gap: calc(var(--spacing) * 8);
}

.switch-example {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 2);
}

.switch-description {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    margin-bottom: calc(var(--spacing) * 2);
}

.switch-with-label {
    display: flex;
    align-items: center;
    gap: calc(var(--spacing) * 2);
}

.switch-with-label label {
    font-size: var(--font-size-sm);
    color: var(--foreground);
    cursor: pointer;
}

.switch-value {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    margin-top: calc(var(--spacing) * 2);
}
</style>
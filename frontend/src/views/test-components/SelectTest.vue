<script setup lang="ts">
import { ref } from 'vue'
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectSeparator,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'

const frameworks = [
    { value: 'vue', label: 'Vue' },
    { value: 'react', label: 'React' },
    { value: 'angular', label: 'Angular' },
    { value: 'svelte', label: 'Svelte' },
]

const browsers = [
    { value: 'chrome', label: 'Chrome' },
    { value: 'firefox', label: 'Firefox' },
    { value: 'safari', label: 'Safari' },
    { value: 'edge', label: 'Edge' },
]

const selectedFramework = ref('vue')
const selectedBrowser = ref('')
const disabledValue = ref('disabled')
</script>

<template>
    <div class="select-test">
        <h2>Select Component</h2>

        <div class="select-examples">
            <!-- Basic Select -->
            <div class="select-example">
                <h3>Basic Select</h3>
                <p class="select-description">A basic select with a placeholder and options.</p>
                <Select v-model="selectedFramework">
                    <SelectTrigger>
                        <SelectValue placeholder="Select a framework" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem v-for="framework in frameworks" :key="framework.value" :value="framework.value">
                                {{ framework.label }}
                            </SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
                <p class="select-value">Selected value: {{ selectedFramework }}</p>
            </div>

            <!-- Select with Groups and Labels -->
            <div class="select-example">
                <h3>Groups and Labels</h3>
                <p class="select-description">Select with grouped options and labels.</p>
                <Select v-model="selectedBrowser">
                    <SelectTrigger>
                        <SelectValue placeholder="Select a browser" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectLabel>Popular Browsers</SelectLabel>
                            <SelectItem v-for="browser in browsers.slice(0, 2)" :key="browser.value"
                                :value="browser.value">
                                {{ browser.label }}
                            </SelectItem>
                        </SelectGroup>
                        <SelectSeparator />
                        <SelectGroup>
                            <SelectLabel>Other Browsers</SelectLabel>
                            <SelectItem v-for="browser in browsers.slice(2)" :key="browser.value"
                                :value="browser.value">
                                {{ browser.label }}
                            </SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
                <p class="select-value">Selected value: {{ selectedBrowser || 'None' }}</p>
            </div>

            <!-- Disabled States -->
            <div class="select-example">
                <h3>Disabled States</h3>
                <p class="select-description">Examples of disabled select and options.</p>

                <!-- Disabled Select -->
                <Select v-model="disabledValue" disabled class="select-item-space">
                    <SelectTrigger>
                        <SelectValue placeholder="Disabled select" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem value="disabled">Disabled Option</SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>

                <!-- Select with Disabled Options -->
                <Select v-model="selectedFramework" class="select-item-space">
                    <SelectTrigger>
                        <SelectValue placeholder="Select with disabled options" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem value="vue">Vue</SelectItem>
                            <SelectItem value="react" disabled>React (Disabled)</SelectItem>
                            <SelectItem value="angular">Angular</SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
        </div>
    </div>
</template>

<style scoped>
.select-test {
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

.select-examples {
    display: grid;
    gap: calc(var(--spacing) * 8);
}

.select-example {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 2);
}

.select-description {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    margin-bottom: calc(var(--spacing) * 2);
}

.select-value {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    margin-top: calc(var(--spacing) * 2);
}

.select-item-space {
    margin-bottom: calc(var(--spacing) * 2);
}
</style>
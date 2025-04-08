<script setup lang="ts">
import { ref } from 'vue'
import {
    SliderRoot,
    SliderTrack,
    SliderRange,
    SliderThumb,
    SliderReusable
} from '@/components/ui/slider'

const singleValue = ref([50])
const rangeValue = ref([25, 75])
const stepValue = ref([30])
const verticalValue = ref([40])
const reusableValue = ref([60])
const reusableRangeValue = ref([20, 80])
</script>

<template>
    <div class="slider-demo-page">
        <h2>Slider Components</h2>

        <section>
            <h3>Basic Usage - Individual Components</h3>
            <div class="demo-container">
                <div class="demo-item">
                    <h4>Single Value</h4>
                    <SliderRoot v-model="singleValue">
                        <SliderTrack>
                            <SliderRange />
                        </SliderTrack>
                        <SliderThumb />
                    </SliderRoot>
                    <div class="value-display">Value: {{ singleValue[0] }}</div>
                </div>

                <div class="demo-item">
                    <h4>Range Slider</h4>
                    <SliderRoot v-model="rangeValue">
                        <SliderTrack>
                            <SliderRange />
                        </SliderTrack>
                        <SliderThumb />
                        <SliderThumb />
                    </SliderRoot>
                    <div class="value-display">Range: {{ rangeValue[0] }} - {{ rangeValue[1] }}</div>
                </div>

                <div class="demo-item">
                    <h4>Step Slider (step=10)</h4>
                    <SliderRoot v-model="stepValue" :step="10">
                        <SliderTrack>
                            <SliderRange />
                        </SliderTrack>
                        <SliderThumb />
                    </SliderRoot>
                    <div class="value-display">Value: {{ stepValue[0] }}</div>
                </div>

                <div class="demo-item vertical-demo">
                    <h4>Vertical Orientation</h4>
                    <SliderRoot v-model="verticalValue" orientation="vertical">
                        <SliderTrack>
                            <SliderRange />
                        </SliderTrack>
                        <SliderThumb />
                    </SliderRoot>
                    <div class="value-display">Value: {{ verticalValue[0] }}</div>
                </div>
            </div>
        </section>

        <section>
            <h3>Reusable Component</h3>
            <div class="demo-container">
                <div class="demo-item">
                    <h4>Single Value</h4>
                    <SliderReusable v-model="reusableValue" />
                    <div class="value-display">Value: {{ reusableValue[0] }}</div>
                </div>

                <div class="demo-item">
                    <h4>Range</h4>
                    <SliderReusable v-model="reusableRangeValue" />
                    <div class="value-display">Range: {{ reusableRangeValue[0] }} - {{ reusableRangeValue[1] }}</div>
                </div>

                <div class="demo-item vertical-demo">
                    <h4>Vertical</h4>
                    <SliderReusable v-model="verticalValue" orientation="vertical" />
                    <div class="value-display">Value: {{ verticalValue[0] }}</div>
                </div>
            </div>
        </section>
    </div>
</template>

<style lang="css" scoped>
.slider-demo-page {
    padding: 2rem;
    max-width: 800px;
    margin: 0 auto;
}

h2 {
    font-size: 1.5rem;
    margin-bottom: 1.5rem;
    font-weight: 600;
}

h3 {
    font-size: 1.25rem;
    margin-bottom: 1rem;
    font-weight: 500;
}

h4 {
    font-size: 0.875rem;
    margin-bottom: 0.75rem;
    font-weight: 500;
}

section {
    margin-bottom: 2rem;
    padding: 1rem;
    border-radius: 8px;
    border: 1px solid rgb(229, 231, 235);
    background-color: rgb(249, 250, 251);
}

.demo-container {
    display: flex;
    flex-wrap: wrap;
    gap: 2rem;
}

.demo-item {
    flex: 1;
    min-width: 250px;
    padding: 1rem;
    border-radius: 8px;
    background-color: white;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.vertical-demo {
    height: 200px;
}

.value-display {
    margin-top: 1rem;
    font-size: 0.875rem;
    color: rgb(75, 85, 99);
}
</style>
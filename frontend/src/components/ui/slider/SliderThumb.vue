<script setup lang="ts">
import {
    SliderThumb,
    type SliderThumbProps,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<SliderThumbProps & { class?: HTMLAttributes['class'] }>()
</script>

<template>
    <SliderThumb v-bind="props" :class="['slider-thumb', props.class]">
        <slot />
    </SliderThumb>
</template>

<style lang="css" scoped>
.slider-thumb {
    display: block;
    width: 20px;
    height: 20px;
    background-color: white;
    border: 2px solid rgb(51, 65, 85);
    border-radius: 50%;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: border-color 0.2s ease;
    cursor: pointer;
}

.slider-thumb:hover {
    border-color: rgb(71, 85, 105);
}

.slider-thumb:focus-visible {
    outline: none;
    border-color: rgb(59, 130, 246);
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

.slider-thumb[data-disabled] {
    cursor: not-allowed;
    opacity: 0.5;
}
</style>
<script setup lang="ts">
import {
    SliderTrack,
    type SliderTrackProps,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<SliderTrackProps & { class?: HTMLAttributes['class'] }>()
</script>

<template>
    <SliderTrack v-bind="props" :class="['slider-track', props.class]">
        <slot />
    </SliderTrack>
</template>

<style lang="css" scoped>
.slider-track {
    position: relative;
    flex-grow: 1;
    border-radius: 9999px;
    height: 8px;
    background-color: rgb(231, 235, 240);
    overflow: hidden;
}

.slider-track[data-orientation="vertical"] {
    height: 100%;
    width: 8px;
}

.slider-track[data-disabled] {
    opacity: 0.5;
}
</style>
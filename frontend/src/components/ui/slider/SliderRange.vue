<script setup lang="ts">
import {
    SliderRange,
    type SliderRangeProps,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<SliderRangeProps & { class?: HTMLAttributes['class'] }>()
</script>

<template>
    <SliderRange v-bind="props" :class="['slider-range', props.class]">
        <slot />
    </SliderRange>
</template>

<style lang="css" scoped>
.slider-range {
    position: absolute;
    background-color: rgb(51, 65, 85);
    border-radius: 9999px;
    height: 100%;
}

.slider-range[data-orientation="vertical"] {
    width: 100%;
}

.slider-range[data-disabled] {
    opacity: 0.5;
}
</style>
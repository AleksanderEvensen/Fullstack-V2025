<script setup lang="ts">
import {
    SliderRoot,
    type SliderRootEmits,
    type SliderRootProps,
    useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'
import { SliderRange } from './index'
import { SliderThumb } from './index'
import { SliderTrack } from './index'

const props = defineProps<SliderRootProps & {
    class?: HTMLAttributes['class'],
    trackClass?: HTMLAttributes['class'],
    rangeClass?: HTMLAttributes['class'],
    thumbClass?: HTMLAttributes['class']
}>()

const emits = defineEmits<SliderRootEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
    <SliderRoot v-slot="{ modelValue }" v-bind="forwarded" :class="['slider-reusable', props.class]">
        <SliderTrack :class="props.trackClass">
            <SliderRange :class="props.rangeClass" />
        </SliderTrack>

        <SliderThumb v-for="(_, i) in modelValue" :key="i" :class="props.thumbClass" />
    </SliderRoot>
</template>

<style lang="css" scoped>
.slider-reusable {
    position: relative;
    display: flex;
    align-items: center;
    width: 100%;
    height: 20px;
    touch-action: none;
    user-select: none;
}

.slider-reusable[data-orientation="vertical"] {
    flex-direction: column;
    width: 20px;
    height: 100px;
}

.slider-reusable[data-disabled] {
    opacity: 0.5;
    pointer-events: none;
}
</style>
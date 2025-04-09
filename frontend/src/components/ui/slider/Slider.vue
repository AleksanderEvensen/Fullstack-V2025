<script setup lang="ts">
import {
    SliderRoot as RekaSliderRoot,
    type SliderRootEmits,
    type SliderRootProps,
    useForwardPropsEmits,
} from 'reka-ui'
import type { HTMLAttributes } from 'vue'

const props = defineProps<SliderRootProps & { class?: HTMLAttributes['class'] }>()
const emits = defineEmits<SliderRootEmits>()

const forwarded = useForwardPropsEmits(props, emits)
</script>

<template>
    <RekaSliderRoot v-bind="forwarded" :class="['slider', props.class]">
        <slot />
    </RekaSliderRoot>
</template>

<style lang="css" scoped>
.slider {
    position: relative;
    display: flex;
    touch-action: none;
    user-select: none;
}
</style>
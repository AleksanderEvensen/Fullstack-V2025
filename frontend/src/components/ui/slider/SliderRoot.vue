<script setup lang="ts">
import {
    SliderRoot,
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
    <SliderRoot v-bind="forwarded" :class="['slider-root', props.class]" v-slot="slotProps">
        <slot v-bind="slotProps" />
    </SliderRoot>
</template>

<style lang="css" scoped>
.slider-root {
    position: relative;
    display: flex;
    align-items: center;
    width: 100%;
    height: 20px;
    touch-action: none;
    user-select: none;
}

.slider-root[data-orientation="vertical"] {
    flex-direction: column;
    width: 20px;
    height: 100px;
}

.slider-root[data-disabled] {
    opacity: 0.5;
    pointer-events: none;
}
</style>
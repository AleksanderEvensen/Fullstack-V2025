<template>
    <component :is="icon" />
</template>

<script setup lang="ts">
import * as icons from 'lucide-vue-next'
import { computed } from 'vue'
import type { FunctionalComponent } from 'vue'
import type { LucideProps } from 'lucide-vue-next'

type IconType = FunctionalComponent<LucideProps>
export type IconName = keyof typeof icons

interface Props {
    name: IconName
}

const props = defineProps<Props>()

const icon = computed<IconType>(() => {
    if (icons[props.name] === undefined) {
        return icons['AlertTriangle'] as IconType
    }
    return icons[props.name] as IconType
})
</script>
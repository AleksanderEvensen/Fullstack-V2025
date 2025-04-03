<template>
    <component :is="icon" />
</template>

<script setup lang="ts">
import * as icons from 'lucide-vue-next'
import { computed } from 'vue'
import type { FunctionalComponent } from 'vue'
import type { LucideProps } from 'lucide-vue-next'

type IconType = FunctionalComponent<LucideProps>

interface Props {
    name: keyof typeof icons
}

const props = defineProps<Props>()

const icon = computed<IconType>(() => {
    return icons[props.name] as IconType
})
</script>
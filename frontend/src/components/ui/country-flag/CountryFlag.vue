<script setup lang="ts">
const props = defineProps<{
    code: string
    size?: number
}>()

const getFlagEmoji = (countryCode: string) => {
    const codePoints = countryCode
        .toUpperCase()
        .split('')
        .map(char => 127397 + char.charCodeAt(0))
    return String.fromCodePoint(...codePoints)
}
</script>

<template>
    <span class="flag" :style="{ fontSize: size ? `${size}px` : '1.25rem' }">
        {{ getFlagEmoji(code) }}
    </span>
</template>

<style scoped>
.flag {
    line-height: 1;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-family: "Twemoji Country Flags", sans-serif;
}
</style>
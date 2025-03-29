<script setup lang="ts">
import type { AvatarImageProps } from 'reka-ui'
import { AvatarImage } from 'reka-ui'

const props = defineProps<AvatarImageProps>()
</script>

<template>
  <AvatarImage v-bind="props" class="avatar-image">
    <slot />
  </AvatarImage>
</template>

<style scoped>
.avatar-image {
  height: 100%;
  width: 100%;
  object-fit: cover;
}
</style>

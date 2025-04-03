<template>
  <i v-if="icon" :class="[icon]" :style="iconStyle"></i>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'

const props = defineProps({
  icon: {
    type: String,
    required: true,
  },
  size: {
    type: String,
    default: '1em',
  },
  color: {
    type: String,
    default: 'currentColor',
  },
})

const iconStyle = computed(() => ({
  fontSize: props.size,
  color: props.color,
}))

const fontAwesomeLoaded = ref(false)

onMounted(() => {
  if (!document.getElementById('font-awesome-css')) {
    const link = document.createElement('link')
    link.id = 'font-awesome-css'
    link.rel = 'stylesheet'
    link.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css'
    link.integrity =
      'sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA=='
    link.crossOrigin = 'anonymous'
    link.referrerPolicy = 'no-referrer'
    document.head.appendChild(link)

    link.onload = () => {
      fontAwesomeLoaded.value = true
    }
  } else {
    fontAwesomeLoaded.value = true
  }
})
</script>

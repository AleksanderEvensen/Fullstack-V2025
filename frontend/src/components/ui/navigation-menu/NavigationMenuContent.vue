<script setup lang="ts">
import { cn } from '@/lib/utils'
import {
  NavigationMenuContent,
  type NavigationMenuContentEmits,
  type NavigationMenuContentProps,
  useForwardPropsEmits,
} from 'reka-ui'
import { computed, type HTMLAttributes } from 'vue'

const props = defineProps<NavigationMenuContentProps & { class?: HTMLAttributes['class'] }>()

const emits = defineEmits<NavigationMenuContentEmits>()

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const forwarded = useForwardPropsEmits(delegatedProps, emits)
</script>

<template>
  <NavigationMenuContent v-bind="forwarded" :class="cn('nav-menu-content', props.class)">
    <slot />
  </NavigationMenuContent>
</template>

<style>
.nav-menu-content {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
}

.nav-menu-content[data-motion^="from-"] {
  animation: contentFadeIn 0.2s ease;
}

.nav-menu-content[data-motion^="to-"] {
  animation: contentFadeOut 0.2s ease;
}

.nav-menu-content[data-motion="from-end"] {
  animation: slideInFromRight 0.2s ease;
}

.nav-menu-content[data-motion="from-start"] {
  animation: slideInFromLeft 0.2s ease;
}

.nav-menu-content[data-motion="to-end"] {
  animation: slideOutToRight 0.2s ease;
}

.nav-menu-content[data-motion="to-start"] {
  animation: slideOutToLeft 0.2s ease;
}

@media (min-width: 768px) {
  .nav-menu-content {
    position: absolute;
    width: auto;
  }
}

@keyframes contentFadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes contentFadeOut {
  from {
    opacity: 1;
  }

  to {
    opacity: 0;
  }
}

@keyframes slideInFromRight {
  from {
    transform: translateX(calc(var(--spacing) * 13));
  }

  to {
    transform: translateX(0);
  }
}

@keyframes slideInFromLeft {
  from {
    transform: translateX(calc(var(--spacing) * -13));
  }

  to {
    transform: translateX(0);
  }
}

@keyframes slideOutToRight {
  from {
    transform: translateX(0);
  }

  to {
    transform: translateX(calc(var(--spacing) * 13));
  }
}

@keyframes slideOutToLeft {
  from {
    transform: translateX(0);
  }

  to {
    transform: translateX(calc(var(--spacing) * -13));
  }
}
</style>

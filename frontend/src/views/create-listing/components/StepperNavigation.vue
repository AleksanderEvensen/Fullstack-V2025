<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Check, Circle, Dot } from 'lucide-vue-next'
import { useTypedI18n } from '@/i18n'

const props = defineProps<{
  steps: {
    step: number
    title: string
    description: string
    icon: any
  }[]
  currentStep: number
  isNextDisabled: boolean
  isPrevDisabled: boolean
  meta: {
    valid: boolean
  }
}>()

const emit = defineEmits<{
  (e: 'next'): void
  (e: 'prev'): void
}>()

const { t } = useTypedI18n()
</script>

<template>
  <div class="stepper-nav-container">
    <div v-for="step in steps" :key="step.step" class="stepper-item">
      <div class="stepper-separator" v-if="step.step !== steps[steps.length - 1].step"></div>

      <Button
        :variant="currentStep >= step.step ? 'default' : 'outline'"
        size="icon"
        class="stepper-button"
        :class="[currentStep === step.step && 'active-button']"
        :disabled="currentStep < step.step && !meta.valid"
      >
        <Check v-if="currentStep > step.step" class="stepper-icon-small" />
        <Circle v-if="currentStep === step.step" />
        <Dot v-if="currentStep < step.step" />
      </Button>

      <div class="stepper-title-container">
        <span :class="[currentStep === step.step && 'active-text']" class="stepper-title">
          {{ step.title }}
        </span>
        <span :class="[currentStep === step.step && 'active-text']" class="stepper-description">
          {{ step.description }}
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.stepper-nav-container {
  display: flex;
  width: 100%;
  align-items: flex-start;
  gap: 0.5rem;
  margin-bottom: calc(var(--spacing) * 4);
}

.stepper-item {
  position: relative;
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.stepper-separator {
  position: absolute;
  left: calc(50% + 20px);
  right: calc(-50% + 10px);
  top: 1.25rem;
  display: block;
  height: 0.125rem;
  flex-shrink: 0;
  border-radius: 9999px;
  background-color: var(--muted);
}

.stepper-button {
  z-index: 10;
  border-radius: 9999px;
  flex-shrink: 0;
}

.active-button {
  border: 2px solid var(--primary);
  outline: 2px solid var(--muted);
  outline-offset: 2px;
}

.stepper-icon-small {
  width: 1.25rem;
  height: 1.25rem;
}

.stepper-title-container {
  margin-top: 1.25rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stepper-title {
  font-size: 0.875rem;
  font-weight: var(--font-weight-semibold);
  transition: all 0.3s ease;
}

.stepper-description {
  display: none;
  font-size: 0.75rem;
  color: var(--muted-foreground);
  transition: all 0.3s ease;
}

.active-text {
  color: var(--primary);
}

@media (min-width: 768px) {
  .stepper-title {
    font-size: 1rem;
  }

  .stepper-description {
    display: block;
  }
}

@media (max-width: 640px) {
  .stepper-description {
    display: none;
  }

  .stepper-nav-container {
    display: none;
  }
}
</style> 
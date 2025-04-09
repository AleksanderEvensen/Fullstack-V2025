<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { useTypedI18n } from '@/i18n'

defineProps<{
  isNextDisabled: boolean
  isPrevDisabled: boolean
  meta: {
    valid: boolean
  }
  currentStep: number
  totalSteps: number
}>()

const emit = defineEmits<{
  (e: 'next'): void
  (e: 'prev'): void
  (e: 'submit'): void
}>()

const { t } = useTypedI18n()
</script>

<template>
  <div class="form-actions">
    <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="emit('prev')">
      {{ t('common.back') }}
    </Button>
    <div class="button-group">
      <Button v-if="currentStep !== totalSteps" :type="meta.valid ? 'button' : 'submit'" :disabled="isNextDisabled"
        size="sm" @click="meta.valid && emit('next')">
        {{ t('common.next') }}
      </Button>
      <Button v-if="currentStep === totalSteps" size="sm" type="submit" @click="emit('submit')">
        {{ t('createListing.createButton') }}
      </Button>
    </div>
  </div>
</template>

<style scoped>
.form-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: calc(var(--spacing) * 4);
}

.button-group {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}
</style>
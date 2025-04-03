<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'

import { Calendar } from '@/components/ui/calendar'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { DateFormatter, type DateValue, getLocalTimeZone } from '@internationalized/date'
import { CalendarIcon } from 'lucide-vue-next'
import { ref } from 'vue'

const df = new DateFormatter('en-US', {
  dateStyle: 'long',
})

const value = ref<DateValue>()
</script>

<template>
  <Popover>
    <PopoverTrigger as-child>
      <Button variant="outline" :class="cn('_datepicker-component-button', !value && '_mute-text')">
        <CalendarIcon class="_datepicker-icon-component" />
        {{ value ? df.format(value.toDate(getLocalTimeZone())) : 'Pick a date' }}
      </Button>
    </PopoverTrigger>
    <PopoverContent class="_datepicker-popover-content-component">
      <Calendar v-model="value" initial-focus />
    </PopoverContent>
  </Popover>
</template>

<style lang="css">
._datepicker-component-button {
  width: 280px;
  justify-content: start !important;
  text-align: left !important;
  font-weight: var(--font-weight-normal);

  &._mute-text {
    color: var(--muted-foreground);
  }
}

._datepicker-icon-component {
  margin-right: calc(var(--spacing) * 2);
  width: calc(var(--spacing) * 4);
  height: calc(var(--spacing) * 4);
}

._datepicker-popover-content-component {
  width: auto;
  padding: 0 !important;
}
</style>

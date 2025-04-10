<script setup lang="ts">
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Datepicker } from '@/components/ui/datepicker'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n()
</script>

<template>
  <FormField v-slot="{ value, setValue }" name="modelYear">
    <FormItem>
      <FormLabel>{{ t('createListing.form.modelYear') }}</FormLabel>
      <FormControl>
        <Input type="number" v-bind:modelValue="value" @update:modelValue="($event) => setValue(Number($event))"
          :placeholder="t('createListing.form.modelYearPlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="manufacturer">
    <FormItem>
      <FormLabel>{{ t('createListing.form.manufacturer') }}</FormLabel>
      <FormControl>
        <Input type="text" v-bind="componentField" :placeholder="t('createListing.form.manufacturerPlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="model">
    <FormItem>
      <FormLabel>{{ t('createListing.form.model') }}</FormLabel>
      <FormControl>
        <Input type="text" v-bind="componentField" :placeholder="t('createListing.form.modelPlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="serialNumber">
    <FormItem>
      <FormLabel>{{ t('createListing.form.serialNumber') }}</FormLabel>
      <FormControl>
        <Input type="text" v-bind="componentField" :placeholder="t('createListing.form.serialNumberPlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="purchaseDate">
    <FormItem>
      <FormLabel>{{ t('createListing.form.purchaseDate') }}</FormLabel>
      <FormControl>
        <Datepicker v-bind="componentField" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="usageDuration">
    <FormItem>
      <FormLabel>{{ t('createListing.form.usageDuration') }}</FormLabel>
      <FormControl>
        <Input type="text" v-bind="componentField" :placeholder="t('createListing.form.usageDurationPlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="reasonForSelling">
    <FormItem>
      <FormLabel>{{ t('createListing.form.reasonForSelling') }}</FormLabel>
      <FormControl>
        <Textarea v-bind="componentField" :placeholder="t('createListing.form.reasonForSellingPlaceholder')"
          :rows="3" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>
</template>
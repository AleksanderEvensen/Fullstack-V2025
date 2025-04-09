<script setup lang="ts">
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { useTypedI18n } from '@/i18n'

defineProps<{
  categories: {
    id: number
    name: string
    description: string
    translationString: string
    icon: string
  }[]
}>()

const { t } = useTypedI18n()
</script>

<template>
  <FormField v-slot="{ componentField }" name="title">
    <FormItem>
      <FormLabel>{{ t('createListing.form.title') }}</FormLabel>
      <FormControl>
        <Input type="text" v-bind="componentField" :placeholder="t('createListing.form.titlePlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="categoryId">
    <FormItem>
      <FormLabel>{{ t('createListing.form.category') }}</FormLabel>
      <Select v-bind="componentField">
        <FormControl>
          <SelectTrigger>
            <SelectValue :placeholder="t('createListing.form.categoryPlaceholder')" />
          </SelectTrigger>
        </FormControl>
        <SelectContent>
          <SelectGroup>
            <SelectItem v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="condition">
    <FormItem>
      <FormLabel>{{ t('createListing.form.condition') }}</FormLabel>
      <Select v-bind="componentField">
        <FormControl>
          <SelectTrigger>
            <SelectValue :placeholder="t('createListing.form.conditionPlaceholder')" />
          </SelectTrigger>
        </FormControl>
        <SelectContent>
          <SelectGroup>
            <SelectItem value="NEW">{{ t('createListing.form.conditionOptions.new') }}</SelectItem>
            <SelectItem value="LIKE_NEW">{{ t('createListing.form.conditionOptions.likeNew') }}</SelectItem>
            <SelectItem value="VERY_GOOD">{{ t('createListing.form.conditionOptions.veryGood') }}</SelectItem>
            <SelectItem value="GOOD">{{ t('createListing.form.conditionOptions.good') }}</SelectItem>
            <SelectItem value="ACCEPTABLE">{{ t('createListing.form.conditionOptions.acceptable') }}</SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="price">
    <FormItem>
      <FormLabel>{{ t('createListing.form.price') }}</FormLabel>
      <FormControl>
        <Input type="number" v-bind="componentField" :placeholder="t('createListing.form.pricePlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="originalPrice">
    <FormItem>
      <FormLabel>{{ t('createListing.form.originalPrice') }}</FormLabel>
      <FormControl>
        <Input type="number" v-bind="componentField" :placeholder="t('createListing.form.originalPricePlaceholder')" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>

  <FormField v-slot="{ componentField }" name="description">
    <FormItem>
      <FormLabel>{{ t('createListing.form.description') }}</FormLabel>
      <FormControl>
        <Textarea v-bind="componentField" :placeholder="t('createListing.form.descriptionPlaceholder')" :rows="6" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>
</template>
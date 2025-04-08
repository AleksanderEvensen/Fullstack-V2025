<script setup lang="ts">
import { ref } from 'vue'
import { Button } from '@/components/ui/button'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Datepicker } from '@/components/ui/datepicker'
import {
  Stepper,
  StepperDescription,
  StepperItem,
  StepperSeparator,
  StepperTitle,
  StepperTrigger,
} from '@/components/ui/stepper'
import { toTypedSchema } from '@vee-validate/zod'
import { Check, Circle, Dot, Package, Info, Camera, Eye } from 'lucide-vue-next'
import type { GenericObject } from 'vee-validate'
import * as z from 'zod'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { useTypedI18n } from '@/i18n'

const { t } = useTypedI18n();

const formSchema = [
  // Basic Information
  z.object({
    title: z.string().min(3, t('createListing.form.titleValidation')),
    category: z.string().min(1, t('createListing.form.categoryValidation')),
    condition: z.enum(['New', 'Like New', 'Very Good', 'Good', 'Acceptable']),
    price: z.number().min(1, t('createListing.form.priceValidation')),
    originalPrice: z.number().optional(),
    description: z.string().min(50, t('createListing.form.descriptionValidation')),
  }),
  // Product Details
  z.object({
    modelYear: z.string().optional(),
    manufacturer: z.string().optional(),
    model: z.string().optional(),
    serialNumber: z.string().optional(),
    purchaseDate: z.string().optional(),
    usageDuration: z.string().optional(),
    defects: z.array(z.string()).optional(),
    modifications: z.array(z.string()).optional(),
    reasonForSelling: z.string().optional(),
  }),
  // Images
  z.object({
    images: z.array(z.string()).min(1, t('createListing.form.productImages')).optional(),
  }),
]

const stepIndex = ref(1)
const steps = [
  {
    step: 1,
    title: t('createListing.steps.basicInfo.title'),
    description: t('createListing.steps.basicInfo.description'),
    icon: Package,
  },
  {
    step: 2,
    title: t('createListing.steps.productDetails.title'),
    description: t('createListing.steps.productDetails.description'),
    icon: Info,
  },
  {
    step: 3,
    title: t('createListing.steps.images.title'),
    description: t('createListing.steps.images.description'),
    icon: Camera,
  },
  {
    step: 4,
    title: t('createListing.steps.preview.title'),
    description: t('createListing.steps.preview.description'),
    icon: Eye,
  },
]

interface Category {
  value: string
  label: string
}

const categories: Category[] = [
  { value: 'electronics', label: t('createListing.categories.electronics') },
  { value: 'fashion', label: t('createListing.categories.fashion') },
  { value: 'home', label: t('createListing.categories.home') },
  { value: 'sports', label: t('createListing.categories.sports') },
  { value: 'toys', label: t('createListing.categories.toys') },
]

function onSubmit(values: GenericObject) {
  console.log('Form submitted:', values)
}
</script>

<template>
  <div class="container">
    <Card class="form-container">
      <CardHeader>
        <CardTitle>{{ t('createListing.title') }}</CardTitle>
        <CardDescription>{{ t('createListing.description') }}</CardDescription>
      </CardHeader>
      <CardContent>
        <Form v-slot="{ meta, values, validate }" as="" keep-values
          :validation-schema="toTypedSchema(formSchema[stepIndex - 1])">
          <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }" v-model="stepIndex"
            class="stepper-block">
            <form @submit="
              (e) => {
                e.preventDefault()
                validate()

                if (stepIndex === steps.length && meta.valid) {
                  onSubmit(values)
                }
              }
            ">
              <div class="stepper-nav-container">
                <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }" class="stepper-item"
                  :step="step.step">
                  <StepperSeparator v-if="step.step !== steps[steps.length - 1].step" class="stepper-form-separator" />

                  <StepperTrigger as-child>
                    <Button :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'" size="icon"
                      class="stepper-button" :class="[state === 'active' && 'active-button']"
                      :disabled="state !== 'completed' && !meta.valid">
                      <Check v-if="state === 'completed'" class="stepper-icon-small" />
                      <Circle v-if="state === 'active'" />
                      <Dot v-if="state === 'inactive'" />
                    </Button>
                  </StepperTrigger>

                  <div class="stepper-title-container">
                    <StepperTitle :class="[state === 'active' && 'active-text']" class="stepper-title">
                      {{ step.title }}
                    </StepperTitle>
                    <StepperDescription :class="[state === 'active' && 'active-text']" class="stepper-description">
                      {{ step.description }}
                    </StepperDescription>
                  </div>
                </StepperItem>
              </div>

              <div class="form-fields-container">
                <!-- Step 1: Basic Information -->
                <template v-if="stepIndex === 1">
                  <FormField v-slot="{ componentField }" name="title">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.title') }}</FormLabel>
                      <FormControl>
                        <Input type="text" v-bind="componentField"
                          :placeholder="t('createListing.form.titlePlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="category">
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
                            <SelectItem v-for="category in categories" :key="category.value" :value="category.value">
                              {{ category.label }}
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
                            <SelectItem value="New">{{
                              t('createListing.form.conditionOptions.new')
                            }}</SelectItem>
                            <SelectItem value="Like New">{{ t('createListing.form.conditionOptions.likeNew') }}
                            </SelectItem>
                            <SelectItem value="Very Good">{{ t('createListing.form.conditionOptions.veryGood') }}
                            </SelectItem>
                            <SelectItem value="Good">{{ t('createListing.form.conditionOptions.good') }}
                            </SelectItem>
                            <SelectItem value="Acceptable">{{ t('createListing.form.conditionOptions.acceptable') }}
                            </SelectItem>
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
                        <Input type="number" v-bind="componentField"
                          :placeholder="t('createListing.form.pricePlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="originalPrice">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.originalPrice') }}</FormLabel>
                      <FormControl>
                        <Input type="number" v-bind="componentField"
                          :placeholder="t('createListing.form.originalPricePlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="description">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.description') }}</FormLabel>
                      <FormControl>
                        <Textarea v-bind="componentField" :placeholder="t('createListing.form.descriptionPlaceholder')"
                          :rows="6" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </template>

                <!-- Step 2: Product Details -->
                <template v-if="stepIndex === 2">
                  <FormField v-slot="{ componentField }" name="modelYear">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.modelYear') }}</FormLabel>
                      <FormControl>
                        <Input type="text" v-bind="componentField"
                          :placeholder="t('createListing.form.modelYearPlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="manufacturer">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.manufacturer') }}</FormLabel>
                      <FormControl>
                        <Input type="text" v-bind="componentField"
                          :placeholder="t('createListing.form.manufacturerPlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="model">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.model') }}</FormLabel>
                      <FormControl>
                        <Input type="text" v-bind="componentField"
                          :placeholder="t('createListing.form.modelPlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="serialNumber">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.serialNumber') }}</FormLabel>
                      <FormControl>
                        <Input type="text" v-bind="componentField"
                          :placeholder="t('createListing.form.serialNumberPlaceholder')" />
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
                        <Input type="text" v-bind="componentField"
                          :placeholder="t('createListing.form.usageDurationPlaceholder')" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="reasonForSelling">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.reasonForSelling') }}</FormLabel>
                      <FormControl>
                        <Textarea v-bind="componentField"
                          :placeholder="t('createListing.form.reasonForSellingPlaceholder')" :rows="3" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </template>

                <!-- Step 3: Images -->
                <template v-if="stepIndex === 3">
                  <div class="image-upload-section">
                    <FormField name="images">
                      <FormItem>
                        <FormLabel>{{ t('createListing.form.productImages') }}</FormLabel>
                        <FormControl>
                          <div class="image-upload-container">
                            <div class="image-upload-placeholder">
                              <Camera class="upload-icon" />
                              <p>{{ t('createListing.form.imageUploadText') }}</p>
                            </div>
                          </div>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    </FormField>
                  </div>
                </template>

                <!-- Step 4: Preview -->
                <template v-if="stepIndex === 4">
                  <div class="preview-container">
                    <Card>
                      <CardHeader>
                        <CardTitle>{{ t('createListing.preview.basicInfo') }}</CardTitle>
                        <CardDescription>{{ t('createListing.preview.basicInfoDescription') }}
                        </CardDescription>
                      </CardHeader>
                      <CardContent>
                        <div class="preview-grid">
                          <div class="preview-item">
                            <span class="preview-label">{{ t('createListing.form.title') }}</span>
                            <span class="preview-value">{{ values.title }}</span>
                          </div>
                          <div class="preview-item">
                            <span class="preview-label">{{
                              t('createListing.form.category')
                            }}</span>
                            <span class="preview-value">{{
                              categories.find((c) => c.value === values.category)?.label
                            }}</span>
                          </div>
                          <div class="preview-item">
                            <span class="preview-label">{{
                              t('createListing.form.condition')
                            }}</span>
                            <span class="preview-value">{{ values.condition }}</span>
                          </div>
                          <div class="preview-item">
                            <span class="preview-label">{{ t('createListing.form.price') }}</span>
                            <span class="preview-value">${{ values.price }}</span>
                          </div>
                          <div class="preview-item" v-if="values.originalPrice">
                            <span class="preview-label">{{
                              t('createListing.form.originalPrice')
                            }}</span>
                            <span class="preview-value">${{ values.originalPrice }}</span>
                          </div>
                          <div class="preview-item full-width">
                            <span class="preview-label">{{
                              t('createListing.form.description')
                            }}</span>
                            <span class="preview-value">{{ values.description }}</span>
                          </div>
                        </div>
                      </CardContent>
                    </Card>

                    <Card>
                      <CardHeader>
                        <CardTitle>{{ t('createListing.preview.productDetails') }}</CardTitle>
                        <CardDescription>{{
                          t('createListing.preview.productDetailsDescription')
                        }}</CardDescription>
                      </CardHeader>
                      <CardContent>
                        <div class="preview-grid">
                          <div class="preview-item" v-if="values.modelYear">
                            <span class="preview-label">{{
                              t('createListing.form.modelYear')
                            }}</span>
                            <span class="preview-value">{{ values.modelYear }}</span>
                          </div>
                          <div class="preview-item" v-if="values.manufacturer">
                            <span class="preview-label">{{
                              t('createListing.form.manufacturer')
                            }}</span>
                            <span class="preview-value">{{ values.manufacturer }}</span>
                          </div>
                          <div class="preview-item" v-if="values.model">
                            <span class="preview-label">{{ t('createListing.form.model') }}</span>
                            <span class="preview-value">{{ values.model }}</span>
                          </div>
                          <div class="preview-item" v-if="values.serialNumber">
                            <span class="preview-label">{{
                              t('createListing.form.serialNumber')
                            }}</span>
                            <span class="preview-value">{{ values.serialNumber }}</span>
                          </div>
                          <div class="preview-item" v-if="values.purchaseDate">
                            <span class="preview-label">{{
                              t('createListing.form.purchaseDate')
                            }}</span>
                            <span class="preview-value">{{ values.purchaseDate }}</span>
                          </div>
                          <div class="preview-item" v-if="values.usageDuration">
                            <span class="preview-label">{{
                              t('createListing.form.usageDuration')
                            }}</span>
                            <span class="preview-value">{{ values.usageDuration }}</span>
                          </div>
                          <div class="preview-item full-width" v-if="values.reasonForSelling">
                            <span class="preview-label">{{
                              t('createListing.form.reasonForSelling')
                            }}</span>
                            <span class="preview-value">{{ values.reasonForSelling }}</span>
                          </div>
                        </div>
                      </CardContent>
                    </Card>

                    <Card>
                      <CardHeader>
                        <CardTitle>{{ t('createListing.preview.imagesTitle') }}</CardTitle>
                        <CardDescription>{{ t('createListing.preview.imagesDescription') }}
                        </CardDescription>
                      </CardHeader>
                      <CardContent>
                        <div class="preview-images" v-if="values.images?.length">
                          <div class="image-grid">
                            <div v-for="(image, index) in values.images" :key="index" class="image-preview">
                              <img :src="image" :alt="`${t('createListing.preview.imageAlt')} ${index + 1}`" />
                            </div>
                          </div>
                        </div>
                        <div v-else class="no-images">
                          <Camera class="no-images-icon" />
                          <p>{{ t('createListing.form.noImages') }}</p>
                        </div>
                      </CardContent>
                    </Card>
                  </div>
                </template>
              </div>

              <div class="form-actions">
                <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()">
                  {{ t('common.back') }}
                </Button>
                <div class="button-group">
                  <Button v-if="stepIndex !== 4" :type="meta.valid ? 'button' : 'submit'" :disabled="isNextDisabled"
                    size="sm" @click="meta.valid && nextStep()">
                    {{ t('common.next') }}
                  </Button>
                  <Button v-if="stepIndex === 4" size="sm" type="submit">
                    {{ t('createListing.createButton') }}
                  </Button>
                </div>
              </div>
            </form>
          </Stepper>
        </Form>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.container {
  width: 900px;
  margin: 2rem auto;
  padding: 1.5rem;
}

h1 {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  margin-bottom: calc(var(--spacing) * 4);
}

.form-container {
  margin-bottom: calc(var(--spacing) * 4);
}

.stepper-block {
  display: block;
  width: 100%;
}

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

.stepper-form-separator {
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

.form-fields-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
  margin-top: calc(var(--spacing) * 4);
}

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

.image-upload-section {
  width: 100%;
}

.image-upload-container {
  border: 2px dashed var(--muted);
  border-radius: var(--radius-lg);
  padding: calc(var(--spacing) * 4);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.image-upload-container:hover {
  border-color: var(--primary);
}

.image-upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: calc(var(--spacing) * 2);
}

.upload-icon {
  width: 2rem;
  height: 2rem;
  color: var(--muted-foreground);
}

.preview-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
}

.preview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: calc(var(--spacing) * 3);
}

.preview-item {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 1);
}

.preview-item.full-width {
  grid-column: 1 / -1;
}

.preview-label {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

.preview-value {
  font-size: var(--font-size-base);
  color: var(--foreground);
  line-height: 1.5;
}

.preview-images {
  width: 100%;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: calc(var(--spacing) * 2);
}

.image-preview {
  aspect-ratio: 1;
  border-radius: var(--radius);
  overflow: hidden;
  border: 1px solid var(--border);
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-images {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: calc(var(--spacing) * 2);
  padding: calc(var(--spacing) * 4);
  border: 2px dashed var(--border);
  border-radius: var(--radius-lg);
  background-color: var(--background);
}

.no-images-icon {
  width: 2rem;
  height: 2rem;
  color: var(--muted-foreground);
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

  .preview-grid {
    grid-template-columns: 1fr;
  }

  .stepper-nav-container {
    display: none;
  }

  .container {
    width: 100%;
  }
}
</style>

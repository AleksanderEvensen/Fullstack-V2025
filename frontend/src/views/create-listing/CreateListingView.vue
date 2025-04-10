<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { toTypedSchema } from '@vee-validate/zod'
import { Package, MapPin, Image, Info } from 'lucide-vue-next'
import type { GenericObject } from 'vee-validate'
import * as z from 'zod'
import { useTypedI18n } from '@/i18n'
import { getCategories } from '@/lib/api/queries/categories'
import { useCreateListing } from '@/lib/api/queries/listings'
import { toast } from 'vue-sonner'
import { useRouter } from 'vue-router'

// Import the LocationInfoForm component
import LocationInfoForm from './components/LocationInfoForm.vue'
import ImageUploadForm from './components/ImageUploadForm.vue'

const { t } = useTypedI18n()
const router = useRouter()
const { data: categories } = getCategories()
const { mutate: createListing, isPending } = useCreateListing()
const formRef = ref<InstanceType<typeof Form> | null>(null)
const uploadedImageNames = ref<string[]>([])
const categoriesList = computed(() => categories?.value ?? [])

watch(
  uploadedImageNames,
  (newImages) => {
    formRef.value?.setFieldValue('images', newImages)
  },
  { deep: true },
)

// Single comprehensive form schema
const formSchema = z.object({
  // Basic Info
  title: z.string().min(3, t('createListing.form.titleValidation')),
  categoryId: z.number().min(1, t('createListing.form.categoryValidation')),
  condition: z.enum(['NEW', 'LIKE_NEW', 'VERY_GOOD', 'GOOD', 'ACCEPTABLE']),
  price: z.number().min(1, t('createListing.form.priceValidation')),
  originalPrice: z.number().optional(),
  description: z.string().min(50, t('createListing.form.descriptionValidation')),

  // Product Details
  modelYear: z.number().optional(),
  manufacturer: z.string().optional(),
  model: z.string().optional(),
  serialNumber: z.string().optional(),
  purchaseDate: z.string().optional(),
  usageDuration: z.string().optional(),
  defects: z.array(z.string()).optional(),
  modifications: z.array(z.string()).optional(),
  reasonForSelling: z.string().optional(),

  // Location
  streetName: z.string().optional(),
  streetNumber: z.string().optional(),
  city: z.string().optional(),
  postalCode: z.string().optional(),
  country: z.string().default('Norway'),
  latitude: z.number().optional(),
  longitude: z.number().optional(),

  // Images
  images: z.array(z.string()).optional(),
})

export type ListingFormValues = z.infer<typeof formSchema>

async function onSubmit(values: GenericObject) {
  await createListing(
    {
      title: values.title,
      categoryId: values.categoryId,
      condition: values.condition,
      price: values.price,
      originalPrice: values.originalPrice,
      description: values.description,
      modelYear: values.modelYear,
      manufacturer: values.manufacturer,
      model: values.model,
      serialNumber: values.serialNumber,
      purchaseDate: values.purchaseDate,
      usageDuration: values.usageDuration,
      defects: values.defects,
      modifications: values.modifications,
      reasonForSelling: values.reasonForSelling,
      images: values.images,
      latitude: values.latitude,
      longitude: values.longitude,
    },
    {
      onSuccess: (data) => {
        toast.success(t('createListing.success'))
        if (data?.id) {
          router.push({ name: 'product', params: { id: data?.id } })
        }
      },
      onError: () => {
        toast.error(t('createListing.error'))
      },
    },
  )
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
        <Form
          v-slot="{ meta, values, validate }"
          as=""
          keep-values
          :validation-schema="toTypedSchema(formSchema)"
          ref="formRef"
        >
          <form
            @submit="
              (e) => {
                e.preventDefault()
                validate()

                if (meta.valid) {
                  onSubmit(values as ListingFormValues)
                }
              }
            "
          >
            <div class="form-fields-container">
              <!-- Basic Information Section -->
              <div class="form-section">
                <div class="section-header">
                  <Package class="section-icon" />
                  <h2 class="section-title">{{ t('createListing.steps.basicInfo.title') }}</h2>
                </div>

                <FormField v-slot="{ componentField }" name="title">
                  <FormItem>
                    <FormLabel>{{ t('createListing.form.title') }}</FormLabel>
                    <FormControl>
                      <Input
                        type="text"
                        v-bind="componentField"
                        :placeholder="t('createListing.form.titlePlaceholder')"
                      />
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
                          <SelectItem
                            v-for="category in categoriesList"
                            :key="category.id"
                            :value="category.id"
                          >
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
                          <SelectValue
                            :placeholder="t('createListing.form.conditionPlaceholder')"
                          />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectGroup>
                          <SelectItem value="NEW">{{
                            t('createListing.form.conditionOptions.new')
                          }}</SelectItem>
                          <SelectItem value="LIKE_NEW">{{
                            t('createListing.form.conditionOptions.likeNew')
                          }}</SelectItem>
                          <SelectItem value="VERY_GOOD">{{
                            t('createListing.form.conditionOptions.veryGood')
                          }}</SelectItem>
                          <SelectItem value="GOOD">{{
                            t('createListing.form.conditionOptions.good')
                          }}</SelectItem>
                          <SelectItem value="ACCEPTABLE">{{
                            t('createListing.form.conditionOptions.acceptable')
                          }}</SelectItem>
                        </SelectGroup>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                </FormField>

                <div class="price-row">
                  <FormField v-slot="{ componentField }" name="price" class="price-field">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.price') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="number"
                          v-bind="componentField"
                          :placeholder="t('createListing.form.pricePlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField
                    v-slot="{ componentField }"
                    name="originalPrice"
                    class="original-price-field"
                  >
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.originalPrice') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="number"
                          v-bind="componentField"
                          :placeholder="t('createListing.form.originalPricePlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </div>

                <FormField v-slot="{ componentField }" name="description">
                  <FormItem>
                    <FormLabel>{{ t('createListing.form.description') }}</FormLabel>
                    <FormControl>
                      <Textarea
                        v-bind="componentField"
                        :placeholder="t('createListing.form.descriptionPlaceholder')"
                        :rows="6"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>

              <!-- Product Details Section -->
              <div class="form-section">
                <div class="section-header">
                  <Info class="section-icon" />
                  <h2 class="section-title">{{ t('createListing.steps.productDetails.title') }}</h2>
                </div>

                <div class="details-row">
                  <FormField v-slot="{ value, setValue }" name="modelYear">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.modelYear') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="number"
                          v-bind:modelValue="value"
                          @update:modelValue="($event) => setValue(Number($event))"
                          :placeholder="t('createListing.form.modelYearPlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="manufacturer">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.manufacturer') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="text"
                          v-bind="componentField"
                          :placeholder="t('createListing.form.manufacturerPlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </div>

                <div class="details-row">
                  <FormField v-slot="{ componentField }" name="model">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.model') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="text"
                          v-bind="componentField"
                          :placeholder="t('createListing.form.modelPlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="serialNumber">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.serialNumber') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="text"
                          v-bind="componentField"
                          :placeholder="t('createListing.form.serialNumberPlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </div>

                <div class="details-row">
                  <FormField v-slot="{ componentField }" name="purchaseDate">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.purchaseDate') }}</FormLabel>
                      <FormControl>
                        <Input type="date" v-bind="componentField" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>

                  <FormField v-slot="{ componentField }" name="usageDuration">
                    <FormItem>
                      <FormLabel>{{ t('createListing.form.usageDuration') }}</FormLabel>
                      <FormControl>
                        <Input
                          type="text"
                          v-bind="componentField"
                          :placeholder="t('createListing.form.usageDurationPlaceholder')"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  </FormField>
                </div>

                <FormField v-slot="{ componentField }" name="reasonForSelling">
                  <FormItem>
                    <FormLabel>{{ t('createListing.form.reasonForSelling') }}</FormLabel>
                    <FormControl>
                      <Textarea
                        v-bind="componentField"
                        :placeholder="t('createListing.form.reasonForSellingPlaceholder')"
                        :rows="3"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                </FormField>
              </div>

              <!-- Location Section -->
              <div class="form-section">
                <div class="section-header">
                  <MapPin class="section-icon" />
                  <h2 class="section-title">{{ t('createListing.steps.location.title') }}</h2>
                </div>

                <LocationInfoForm />
              </div>

              <!-- Images Section -->
              <div class="form-section">
                <div class="section-header">
                  <Image class="section-icon" />
                  <h2 class="section-title">{{ t('createListing.steps.images.title') }}</h2>
                </div>

                <ImageUploadForm v-model="uploadedImageNames" />
              </div>
            </div>

            <!-- Submit Button -->
            <div class="form-actions">
              <Button
                type="submit"
                size="lg"
                :disabled="isPending || !meta.valid"
                class="submit-button"
              >
                {{ isPending ? t('common.submitting') : t('createListing.createButton') }}
              </Button>
            </div>
          </form>
        </Form>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 1.5rem;
}

.form-container {
  margin-bottom: calc(var(--spacing) * 4);
}

.form-fields-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 8);
  margin-bottom: calc(var(--spacing) * 6);
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
  padding-bottom: calc(var(--spacing) * 4);
  border-bottom: 1px solid var(--border);
}

.section-header {
  display: flex;
  align-items: center;
  gap: calc(var(--spacing) * 2);
  margin-bottom: calc(var(--spacing) * 2);
}

.section-icon {
  width: calc(var(--spacing) * 6);
  height: calc(var(--spacing) * 6);
  color: var(--primary);
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--foreground);
  margin: 0;
}

.price-row,
.details-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: calc(var(--spacing) * 4);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: calc(var(--spacing) * 6);
}

.submit-button {
  min-width: 150px;
}

@media (max-width: 640px) {
  .container {
    width: 100%;
    padding: 1rem;
  }

  .price-row,
  .details-row {
    grid-template-columns: 1fr;
    gap: calc(var(--spacing) * 3);
  }
}
</style>

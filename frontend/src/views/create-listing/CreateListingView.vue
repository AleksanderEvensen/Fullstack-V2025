<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Form } from '@/components/ui/form'
import { toTypedSchema } from '@vee-validate/zod'
import { Package, Info, Camera, Eye } from 'lucide-vue-next'
import type { GenericObject } from 'vee-validate'
import * as z from 'zod'
import { useTypedI18n } from '@/i18n'
import { getCategories } from '@/lib/api/queries/categories'
import { useCreateListing } from '@/lib/api/queries/listings'
import type { components } from '@/lib/api/schema'
import { toast } from 'vue-sonner'
import BasicInfoForm from './components/BasicInfoForm.vue'
import ProductDetailsForm from './components/ProductDetailsForm.vue'
import ImageUploadForm from './components/ImageUploadForm.vue'
import PreviewForm from './components/PreviewForm.vue'
import StepperNavigation from './components/StepperNavigation.vue'
import FormActions from './components/FormActions.vue'
import { useRouter } from 'vue-router'

const { t } = useTypedI18n()
const router = useRouter()
const { data: categories } = getCategories()
const { mutate: createListing } = useCreateListing()
const formRef = ref<InstanceType<typeof Form> | null>(null)
const uploadedImageNames = ref<string[]>([])
const categoriesList = computed(() => categories?.value ?? [])

watch(uploadedImageNames, (newImages) => {
  formRef.value?.setFieldValue('images', newImages)
}, { deep: true })


const formSchema = [
  // Basic Information
  z.object({
    title: z.string().min(3, t('createListing.form.titleValidation')),
    categoryId: z.number().min(1, t('createListing.form.categoryValidation')),
    condition: z.enum(['NEW', 'LIKE_NEW', 'VERY_GOOD', 'GOOD', 'ACCEPTABLE']),
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
    images: z.array(z.string()).optional(),
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

async function onSubmit(values: GenericObject) {
  await createListing({
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
    images: values.images
  }, {
    onSuccess: (data) => {
      toast.success(t('createListing.success'))
      if (data?.id) {
        router.push({ name: 'product', params: { id: data?.id } })
      }
    },
    onError: () => {
      toast.error(t('createListing.error'))
    }
  })
}

const handleNext = () => {
  stepIndex.value++
}

const handlePrev = () => {
  stepIndex.value--
}

const handleSubmit = () => {
  if (formRef.value) {
    const form = formRef.value
    if (form.validate) {
      form.validate()
    }
  }
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
          :validation-schema="toTypedSchema(formSchema[stepIndex - 1])" ref="formRef">
          <form @submit="
            (e) => {
              e.preventDefault()
              validate()

              if (stepIndex === steps.length && meta.valid) {
                onSubmit(values)
              }
            }
          ">
            <StepperNavigation :steps="steps" :current-step="stepIndex" :is-next-disabled="!meta.valid"
              :is-prev-disabled="stepIndex === 1" :meta="meta" />

            <div class="form-fields-container">
              <!-- Step 1: Basic Information -->
              <template v-if="stepIndex === 1">
                <BasicInfoForm :categories="categoriesList" />
              </template>

              <!-- Step 2: Product Details -->
              <template v-if="stepIndex === 2">
                <ProductDetailsForm />
              </template>

              <!-- Step 3: Images -->
              <template v-if="stepIndex === 3">
                <ImageUploadForm v-model="uploadedImageNames" />
              </template>

              <!-- Step 4: Preview -->
              <template v-if="stepIndex === 4">
                <PreviewForm :values="values" :categories="categories || []" :uploaded-images="uploadedImageNames" />
              </template>
            </div>

            <FormActions :is-next-disabled="!meta.valid" :is-prev-disabled="stepIndex === 1" :meta="meta"
              :current-step="stepIndex" :total-steps="steps.length" @next="handleNext" @prev="handlePrev"
              @submit="handleSubmit" />
          </form>
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

.form-container {
  margin-bottom: calc(var(--spacing) * 4);
}

.form-fields-container {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
  margin-top: calc(var(--spacing) * 4);
}

@media (max-width: 640px) {
  .container {
    width: 100%;
  }
}
</style>

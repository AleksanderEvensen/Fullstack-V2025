<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { PlusIcon, TrashIcon } from 'lucide-vue-next'
import { useTypedI18n } from '@/i18n'
import { toast } from 'vue-sonner'
import { fetchClient } from '@/lib/api/client'
import type { components } from '@/lib/api/schema'

const { t } = useTypedI18n()

type Category = components['schemas']['CategoryDto']
type CreateCategoryDto = {
  name: string
  description: string
  translationString: string
  icon: string
}

const categories = ref<Category[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const isAdding = ref(false)

const newCategory = ref<CreateCategoryDto>({
  name: '',
  description: '',
  translationString: '',
  icon: '',
})

const formErrors = ref<Record<string, string>>({})

const fetchCategories = async () => {
  loading.value = true
  error.value = null

  try {
    const response = await fetchClient.GET('/api/categories')
    if (response.error) {
      throw new Error('Failed to fetch categories')
    }
    categories.value = response.data || []
  } catch (err) {
    console.error('Error fetching categories:', err)
    error.value = 'Failed to load categories'
    toast.error(t('errors.loadFailed'))
  } finally {
    loading.value = false
  }
}

const deleteCategory = async (id: number) => {
  if (!confirm(t('categories.confirmDelete'))) return

  loading.value = true
  try {
    const response = await fetchClient.DELETE('/api/categories/{id}', {
      params: { path: { id } },
    })

    if (response.error) {
      throw new Error(`Failed to delete category: ${response.error.message}`)
    }

    toast.success(t('categories.deleted'))

    await fetchCategories()
  } catch (err) {
    console.error('Error deleting category:', err)
    toast.error(t('errors.deleteFailed'))
  } finally {
    loading.value = false
  }
}

const validateForm = () => {
  const errors: Record<string, string> = {}

  if (!newCategory.value.name) {
    errors.name = t('validation.required')
  }

  if (!newCategory.value.description) {
    errors.description = t('validation.required')
  }

  if (!newCategory.value.translationString) {
    errors.translationString = t('validation.required')
  }

  if (!newCategory.value.icon) {
    errors.icon = t('validation.required')
  }

  formErrors.value = errors
  return Object.keys(errors).length === 0
}

const addCategory = async () => {
  if (!validateForm()) return

  loading.value = true
  try {
    const response = await fetchClient.POST('/api/categories', {
      body: newCategory.value,
    })

    if (response.error) {
      throw new Error(`Failed to add category: ${response.error.message}`)
    }

    toast.success(t('categories.added'))

    newCategory.value = {
      name: '',
      description: '',
      translationString: '',
      icon: '',
    }
    isAdding.value = false

    await fetchCategories()
  } catch (err) {
    console.error('Error adding category:', err)
    toast.error(t('errors.addFailed'))
  } finally {
    loading.value = false
  }
}

const cancelAdd = () => {
  isAdding.value = false
  newCategory.value = {
    name: '',
    description: '',
    translationString: '',
    icon: '',
  }
  formErrors.value = {}
}

onMounted(() => {
  fetchCategories()
})
</script>

<template>
  <div class="category-management">
    <Card>
      <CardHeader>
        <CardTitle>{{ t('categories.management') }}</CardTitle>
      </CardHeader>
      <CardContent>
        <div v-if="error" class="error-message">{{ error }}</div>

        <div class="header-container">
          <h3 class="section-title">{{ t('categories.existing') }}</h3>
          <Button v-if="!isAdding" @click="isAdding = true" size="sm">
            <PlusIcon class="add-icon" />
            {{ t('categories.add') }}
          </Button>
        </div>

        <!-- New Category Form -->
        <div v-if="isAdding" class="form-container">
          <h4 class="form-title">{{ t('categories.new') }}</h4>

          <div class="form-grid">
            <div class="form-row">
              <div class="form-group">
                <Label for="name">{{ t('categories.name') }}</Label>
                <Input
                  id="name"
                  v-model="newCategory.name"
                  :class="{ 'error-input': formErrors.name }"
                />
                <p v-if="formErrors.name" class="error-text">{{ formErrors.name }}</p>
              </div>

              <div class="form-group">
                <Label for="icon">{{ t('categories.icon') }}</Label>
                <Input
                  id="icon"
                  v-model="newCategory.icon"
                  :class="{ 'error-input': formErrors.icon }"
                />
                <p v-if="formErrors.icon" class="error-text">{{ formErrors.icon }}</p>
              </div>
            </div>

            <div class="form-group">
              <Label for="translationString">{{ t('categories.translationKey') }}</Label>
              <Input
                id="translationString"
                v-model="newCategory.translationString"
                :class="{ 'error-input': formErrors.translationString }"
              />
              <p v-if="formErrors.translationString" class="error-text">
                {{ formErrors.translationString }}
              </p>
            </div>

            <div class="form-group">
              <Label for="description">{{ t('categories.description') }}</Label>
              <Input
                id="description"
                v-model="newCategory.description"
                :class="{ 'error-input': formErrors.description }"
              />
              <p v-if="formErrors.description" class="error-text">{{ formErrors.description }}</p>
            </div>

            <div class="form-actions">
              <Button variant="outline" @click="cancelAdd">
                {{ t('common.cancel') }}
              </Button>
              <Button @click="addCategory" :disabled="loading">
                {{ t('categories.add') }}
              </Button>
            </div>
          </div>
        </div>

        <!-- Category List -->
        <div class="category-list">
          <div v-if="loading" class="loading-state">{{ t('common.loading') }}...</div>

          <div v-else-if="categories.length === 0" class="empty-state">
            {{ t('categories.noCategories') }}
          </div>

          <div v-else class="categories-grid">
            <div v-for="category in categories" :key="category.id" class="category-item">
              <div class="category-details">
                <h4 class="category-name">{{ category.name }}</h4>
                <p class="category-description">{{ category.description }}</p>
                <div class="category-meta">
                  <span class="meta-label">{{ t('categories.translationKey') }}:</span>
                  {{ category.translationString }}
                </div>
                <div class="category-meta">
                  <span class="meta-label">{{ t('categories.icon') }}:</span>
                  {{ category.icon }}
                </div>
              </div>

              <div class="category-actions">
                <Button
                  variant="destructive"
                  class="delete-button"
                  size="sm"
                  @click="deleteCategory(category.id)"
                  :disabled="loading"
                >
                  <TrashIcon class="icon" />
                </Button>
              </div>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>
.category-management {
  width: 600px;
  margin: 0 auto;
  padding: calc(var(--spacing) * 4);
}

.error-message {
  color: var(--destructive);
  margin-bottom: calc(var(--spacing) * 4);
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: calc(var(--spacing) * 2);
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-medium);
}

.add-icon {
  width: 16px;
  height: 16px;
  margin-right: calc(var(--spacing) * 2);
}

.icon {
  width: 16px;
  height: 16px;
}

.form-container {
  border: 1px solid var(--border);
  padding: calc(var(--spacing) * 4);
  border-radius: var(--radius);
  margin-bottom: calc(var(--spacing) * 6);
}

.form-title {
  font-weight: var(--font-weight-medium);
  margin-bottom: calc(var(--spacing) * 4);
}

.form-grid {
  display: grid;
  gap: calc(var(--spacing) * 4);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr;
  gap: calc(var(--spacing) * 4);
}

@media (min-width: 768px) {
  .form-row {
    grid-template-columns: 1fr 1fr;
  }
}

.form-group {
  margin-bottom: calc(var(--spacing) * 2);
}

.error-input {
  border-color: var(--destructive);
}

.error-text {
  color: var(--destructive);
  font-size: var(--font-size-sm);
  margin-top: calc(var(--spacing));
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: calc(var(--spacing) * 2);
}

.loading-state,
.empty-state {
  text-align: center;
  padding: calc(var(--spacing) * 4) 0;
}

.empty-state {
  color: var(--muted-foreground);
}

.categories-grid {
  display: grid;
  gap: calc(var(--spacing) * 3);
}

.category-item {
  border: 1px solid var(--border);
  padding: calc(var(--spacing) * 3);
  border-radius: var(--radius);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category-name {
  font-weight: var(--font-weight-medium);
}

.category-description {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
}

.category-meta {
  font-size: var(--font-size-xs);
  margin-top: var(--spacing);
}

.meta-label {
  color: var(--muted-foreground);
  margin-right: calc(var(--spacing) * 2);
}

.category-actions {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

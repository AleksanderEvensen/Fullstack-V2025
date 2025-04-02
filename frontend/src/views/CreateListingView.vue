<script setup lang="ts">
import { ref } from 'vue';
import { Button } from '@/components/ui/button';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';
import { Datepicker } from '@/components/ui/datepicker';
import { Stepper, StepperDescription, StepperItem, StepperSeparator, StepperTitle, StepperTrigger, StepperIndicator } from '@/components/ui/stepper';
import { toTypedSchema } from '@vee-validate/zod';
import { Check, Circle, Dot, Package, Info, Camera, CreditCard, Eye } from 'lucide-vue-next';
import type { GenericObject } from 'vee-validate';
import { h } from 'vue';
import * as z from 'zod';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';

const formSchema = [
    // Basic Information
    z.object({
        title: z.string().min(3, 'Title must be at least 3 characters'),
        category: z.string().min(1, 'Please select a category'),
        condition: z.enum(['New', 'Like New', 'Very Good', 'Good', 'Acceptable']),
        price: z.number().min(1, 'Please enter a price'),
        originalPrice: z.number().optional(),
        description: z.string().min(50, 'Description must be at least 50 characters'),
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
        images: z.array(z.string()).min(1, 'Please upload at least one image').optional(),
    }),
];

const stepIndex = ref(1);
const steps = [
    {
        step: 1,
        title: 'Basic Information',
        description: 'Enter product details',
        icon: Package,
    },
    {
        step: 2,
        title: 'Product Details',
        description: 'Add specifications',
        icon: Info,
    },
    {
        step: 3,
        title: 'Images',
        description: 'Upload product photos',
        icon: Camera,
    },
    {
        step: 4,
        title: 'Preview',
        description: 'Review your listing',
        icon: Eye,
    },
];

interface Category {
    value: string;
    label: string;
}

const categories: Category[] = [
    { value: 'electronics', label: 'Electronics' },
    { value: 'fashion', label: 'Fashion' },
    { value: 'home', label: 'Home & Garden' },
    { value: 'sports', label: 'Sports & Outdoors' },
    { value: 'toys', label: 'Toys & Games' },
];

function onSubmit(values: GenericObject) {
    console.log('Form submitted:', values);
}
</script>

<template>
    <div class="container">
        <Card class="form-container">
            <CardHeader>
                <CardTitle>Create New Listing</CardTitle>
                <CardDescription>Fill out the form below to create your listing</CardDescription>
            </CardHeader>
            <CardContent>
                <Form v-slot="{ meta, values, validate }" as="" keep-values
                    :validation-schema="toTypedSchema(formSchema[stepIndex - 1])">
                    <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }" v-model="stepIndex"
                        class="stepper-block">
                        <form @submit="(e) => {
                            e.preventDefault()
                            validate()

                            if (stepIndex === steps.length && meta.valid) {
                                onSubmit(values)
                            }
                        }">
                            <div class="stepper-nav-container">
                                <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }"
                                    class="stepper-item" :step="step.step">
                                    <StepperSeparator v-if="step.step !== steps[steps.length - 1].step"
                                        class="stepper-form-separator" />

                                    <StepperTrigger as-child>
                                        <Button
                                            :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'"
                                            size="icon" class="stepper-button"
                                            :class="[state === 'active' && 'active-button']"
                                            :disabled="state !== 'completed' && !meta.valid">
                                            <Check v-if="state === 'completed'" class="stepper-icon-small" />
                                            <Circle v-if="state === 'active'" />
                                            <Dot v-if="state === 'inactive'" />
                                        </Button>
                                    </StepperTrigger>

                                    <div class="stepper-title-container">
                                        <StepperTitle :class="[state === 'active' && 'active-text']"
                                            class="stepper-title">
                                            {{ step.title }}
                                        </StepperTitle>
                                        <StepperDescription :class="[state === 'active' && 'active-text']"
                                            class="stepper-description">
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
                                            <FormLabel>Title</FormLabel>
                                            <FormControl>
                                                <Input type="text" v-bind="componentField"
                                                    placeholder="Enter product title" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="category">
                                        <FormItem>
                                            <FormLabel>Category</FormLabel>
                                            <Select v-bind="componentField">
                                                <FormControl>
                                                    <SelectTrigger>
                                                        <SelectValue placeholder="Select a category" />
                                                    </SelectTrigger>
                                                </FormControl>
                                                <SelectContent>
                                                    <SelectGroup>
                                                        <SelectItem v-for="category in categories" :key="category.value"
                                                            :value="category.value">
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
                                            <FormLabel>Condition</FormLabel>
                                            <Select v-bind="componentField">
                                                <FormControl>
                                                    <SelectTrigger>
                                                        <SelectValue placeholder="Select condition" />
                                                    </SelectTrigger>
                                                </FormControl>
                                                <SelectContent>
                                                    <SelectGroup>
                                                        <SelectItem value="New">New</SelectItem>
                                                        <SelectItem value="Like New">Like New</SelectItem>
                                                        <SelectItem value="Very Good">Very Good</SelectItem>
                                                        <SelectItem value="Good">Good</SelectItem>
                                                        <SelectItem value="Acceptable">Acceptable</SelectItem>
                                                    </SelectGroup>
                                                </SelectContent>
                                            </Select>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField, setValue }" name="price">
                                        <FormItem>
                                            <FormLabel>Price</FormLabel>
                                            <FormControl>
                                                <Input type="number" v-bind="componentField"
                                                    placeholder="Enter price" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="originalPrice">
                                        <FormItem>
                                            <FormLabel>Original Price (Optional)</FormLabel>
                                            <FormControl>
                                                <Input type="number" v-bind="componentField"
                                                    placeholder="Enter original price" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="description">
                                        <FormItem>
                                            <FormLabel>Description</FormLabel>
                                            <FormControl>
                                                <Textarea v-bind="componentField"
                                                    placeholder="Describe your item in detail" :rows="6" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>
                                </template>

                                <!-- Step 2: Product Details -->
                                <template v-if="stepIndex === 2">
                                    <FormField v-slot="{ componentField }" name="modelYear">
                                        <FormItem>
                                            <FormLabel>Model Year (Optional)</FormLabel>
                                            <FormControl>
                                                <Input type="text" v-bind="componentField"
                                                    placeholder="Enter model year" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="manufacturer">
                                        <FormItem>
                                            <FormLabel>Manufacturer (Optional)</FormLabel>
                                            <FormControl>
                                                <Input type="text" v-bind="componentField"
                                                    placeholder="Enter manufacturer" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="model">
                                        <FormItem>
                                            <FormLabel>Model (Optional)</FormLabel>
                                            <FormControl>
                                                <Input type="text" v-bind="componentField" placeholder="Enter model" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="serialNumber">
                                        <FormItem>
                                            <FormLabel>Serial Number (Optional)</FormLabel>
                                            <FormControl>
                                                <Input type="text" v-bind="componentField"
                                                    placeholder="Enter serial number" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="purchaseDate">
                                        <FormItem>
                                            <FormLabel>Purchase Date (Optional)</FormLabel>
                                            <FormControl>
                                                <Datepicker v-bind="componentField" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="usageDuration">
                                        <FormItem>
                                            <FormLabel>Usage Duration (Optional)</FormLabel>
                                            <FormControl>
                                                <Input type="text" v-bind="componentField"
                                                    placeholder="e.g., 6 months" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>

                                    <FormField v-slot="{ componentField }" name="reasonForSelling">
                                        <FormItem>
                                            <FormLabel>Reason for Selling (Optional)</FormLabel>
                                            <FormControl>
                                                <Textarea v-bind="componentField"
                                                    placeholder="Why are you selling this item?" :rows="3" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    </FormField>
                                </template>

                                <!-- Step 3: Images -->
                                <template v-if="stepIndex === 3">
                                    <div class="image-upload-section">
                                        <FormField v-slot="{ componentField }" name="images">
                                            <FormItem>
                                                <FormLabel>Product Images</FormLabel>
                                                <FormControl>
                                                    <div class="image-upload-container">
                                                        <div class="image-upload-placeholder">
                                                            <Camera class="upload-icon" />
                                                            <p>Drag and drop images here or click to upload</p>
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
                                                <CardTitle>Basic Information</CardTitle>
                                                <CardDescription>Review your listing's basic details</CardDescription>
                                            </CardHeader>
                                            <CardContent>
                                                <div class="preview-grid">
                                                    <div class="preview-item">
                                                        <span class="preview-label">Title</span>
                                                        <span class="preview-value">{{ values.title }}</span>
                                                    </div>
                                                    <div class="preview-item">
                                                        <span class="preview-label">Category</span>
                                                        <span class="preview-value">{{categories.find(c => c.value ===
                                                            values.category)?.label}}</span>
                                                    </div>
                                                    <div class="preview-item">
                                                        <span class="preview-label">Condition</span>
                                                        <span class="preview-value">{{ values.condition }}</span>
                                                    </div>
                                                    <div class="preview-item">
                                                        <span class="preview-label">Price</span>
                                                        <span class="preview-value">${{ values.price }}</span>
                                                    </div>
                                                    <div class="preview-item" v-if="values.originalPrice">
                                                        <span class="preview-label">Original Price</span>
                                                        <span class="preview-value">${{ values.originalPrice }}</span>
                                                    </div>
                                                    <div class="preview-item full-width">
                                                        <span class="preview-label">Description</span>
                                                        <span class="preview-value">{{ values.description }}</span>
                                                    </div>
                                                </div>
                                            </CardContent>
                                        </Card>

                                        <Card>
                                            <CardHeader>
                                                <CardTitle>Product Details</CardTitle>
                                                <CardDescription>Review your product specifications</CardDescription>
                                            </CardHeader>
                                            <CardContent>
                                                <div class="preview-grid">
                                                    <div class="preview-item" v-if="values.modelYear">
                                                        <span class="preview-label">Model Year</span>
                                                        <span class="preview-value">{{ values.modelYear }}</span>
                                                    </div>
                                                    <div class="preview-item" v-if="values.manufacturer">
                                                        <span class="preview-label">Manufacturer</span>
                                                        <span class="preview-value">{{ values.manufacturer }}</span>
                                                    </div>
                                                    <div class="preview-item" v-if="values.model">
                                                        <span class="preview-label">Model</span>
                                                        <span class="preview-value">{{ values.model }}</span>
                                                    </div>
                                                    <div class="preview-item" v-if="values.serialNumber">
                                                        <span class="preview-label">Serial Number</span>
                                                        <span class="preview-value">{{ values.serialNumber }}</span>
                                                    </div>
                                                    <div class="preview-item" v-if="values.purchaseDate">
                                                        <span class="preview-label">Purchase Date</span>
                                                        <span class="preview-value">{{ values.purchaseDate }}</span>
                                                    </div>
                                                    <div class="preview-item" v-if="values.usageDuration">
                                                        <span class="preview-label">Usage Duration</span>
                                                        <span class="preview-value">{{ values.usageDuration }}</span>
                                                    </div>
                                                    <div class="preview-item full-width" v-if="values.reasonForSelling">
                                                        <span class="preview-label">Reason for Selling</span>
                                                        <span class="preview-value">{{ values.reasonForSelling }}</span>
                                                    </div>
                                                </div>
                                            </CardContent>
                                        </Card>

                                        <Card>
                                            <CardHeader>
                                                <CardTitle>Images</CardTitle>
                                                <CardDescription>Review your product images</CardDescription>
                                            </CardHeader>
                                            <CardContent>
                                                <div class="preview-images" v-if="values.images?.length">
                                                    <div class="image-grid">
                                                        <div v-for="(image, index) in values.images" :key="index"
                                                            class="image-preview">
                                                            <img :src="image" :alt="`Product image ${index + 1}`" />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div v-else class="no-images">
                                                    <Camera class="no-images-icon" />
                                                    <p>No images uploaded</p>
                                                </div>
                                            </CardContent>
                                        </Card>
                                    </div>
                                </template>
                            </div>

                            <div class="form-actions">
                                <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()">
                                    Back
                                </Button>
                                <div class="button-group">
                                    <Button v-if="stepIndex !== 4" :type="meta.valid ? 'button' : 'submit'"
                                        :disabled="isNextDisabled" size="sm" @click="meta.valid && nextStep()">
                                        Next
                                    </Button>
                                    <Button v-if="stepIndex === 4" size="sm" type="submit">
                                        Create Listing
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
    max-width: 800px;
    margin: 0 auto;
    padding: calc(var(--spacing) * 4);
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
}
</style>
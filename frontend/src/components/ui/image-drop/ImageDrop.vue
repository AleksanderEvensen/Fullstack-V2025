<script setup lang="ts">
import { ref } from 'vue'
import { Camera } from 'lucide-vue-next'

const props = defineProps<{
    modelValue?: File[]
    maxFiles?: number
    maxSize?: number // in MB
}>()

const emit = defineEmits<{
    (e: 'update:modelValue', files: File[]): void
    (e: 'error', message: string): void
}>()

const files = ref<File[]>(props.modelValue || [])
const isDragging = ref(false)
const fileInput = ref<HTMLInputElement | null>(null)


const handleDragOver = (e: DragEvent) => {
    e.preventDefault()
    isDragging.value = true
}

const handleDragLeave = () => {
    isDragging.value = false
}

const handleDrop = (e: DragEvent) => {
    e.preventDefault()
    isDragging.value = false

    if (!e.dataTransfer?.files.length) return

    const newFiles = Array.from(e.dataTransfer.files)
    validateAndAddFiles(newFiles)
}

const handleFileInput = (e: Event) => {
    const input = e.target as HTMLInputElement
    if (!input.files?.length) return

    const newFiles = Array.from(input.files)
    validateAndAddFiles(newFiles)

    // Reset input value to allow selecting the same file again
    input.value = ''
}

const validateAndAddFiles = (newFiles: File[]) => {
    // Check max files
    if (props.maxFiles && files.value.length + newFiles.length > props.maxFiles) {
        emit('error', `Maximum ${props.maxFiles} files allowed`)
        return
    }

    // Check file size
    if (props.maxSize) {
        const oversizedFiles = newFiles.filter(file => file.size > props.maxSize! * 1024 * 1024)
        if (oversizedFiles.length) {
            emit('error', `Some files exceed the maximum size of ${props.maxSize}MB`)
            return
        }
    }

    // Add valid files
    const updatedFiles = [...files.value, ...newFiles]
    files.value = updatedFiles
    emit('update:modelValue', updatedFiles)
}

const triggerFileInput = () => {
    fileInput.value?.click()
}
</script>

<template>
    <div class="image-drop-container">
        <input ref="fileInput" type="file" multiple accept="image/*" class="file-input" @change="handleFileInput" />

        <div class="drop-zone" :class="{ 'is-dragging': isDragging }" @dragover="handleDragOver"
            @dragleave="handleDragLeave" @drop="handleDrop" @click="triggerFileInput">
            <div class="drop-zone-content">
                <Camera class="upload-icon" />
                <p>{{ $t('createListing.form.imageUploadText') }}</p>
            </div>
        </div>
    </div>
</template>

<style scoped lang="css">
.image-drop-container {
    width: 100%;
}

.file-input {
    display: none;
}

.drop-zone {
    width: 100%;
    min-height: 200px;
    border: 2px dashed var(--border);
    border-radius: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s ease;
    background-color: var(--background);
}

.drop-zone.is-dragging {
    border-color: var(--primary);
    background-color: var(--muted);
}

.drop-zone-content {
    text-align: center;
    padding: 2rem;
}

.upload-icon {
    width: 48px;
    height: 48px;
    margin: 0 auto;
    color: var(--muted-foreground);
    margin-bottom: 1rem;
}

.preview-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 1rem;
    margin-top: 1rem;
}

.preview-item {
    position: relative;
    aspect-ratio: 1;
    border-radius: 0.5rem;
    overflow: hidden;
}

.preview-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.remove-button {
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.25rem;
    line-height: 1;
    transition: background-color 0.2s ease;
}

.remove-button:hover {
    background-color: rgba(0, 0, 0, 0.7);
}
</style>
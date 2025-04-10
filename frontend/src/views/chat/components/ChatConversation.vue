<script setup lang="ts">
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import type { components } from '@/lib/api/schema'

type ChatMessage = components['schemas']['ChatMessageDto']
type ConversationSummary = components['schemas']['ConversationSummaryDto']

// Props
const props = defineProps<{
  messages: ChatMessage[]
  isLoading: boolean
  isSending: boolean
  currentConversation: { otherUserId: number; listingId: number } | null
  conversations: ConversationSummary[]
  message: string
}>()

// Emits
const emit = defineEmits<{
  'update:message': [value: string]
  'send-message': []
}>()

// Local state
const messagesContainer = ref<HTMLElement | null>(null)
const messageInput = computed({
  get: () => props.message,
  set: (value) => emit('update:message', value),
})

// Get user store
const userStore = useAuthStore()
const currentUserId = computed(() => userStore.user?.id)

// Find active conversation details
const activeConversation = computed(() => {
  if (!props.currentConversation) return null

  return props.conversations.find(
    (conv) =>
      conv.user.id === props.currentConversation?.otherUserId &&
      conv.listingId === props.currentConversation?.listingId,
  )
})

// Check if message is from current user
const isOwnMessage = (message: ChatMessage) => {
  return message.sender.id === currentUserId.value
}

// Format timestamp for messages
const formatTime = (timestamp: string) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

// Handle send message
const handleSendMessage = () => {
  if (!messageInput.value.trim() || !props.currentConversation) return
  emit('send-message')
}

// Auto scroll to bottom when messages change
watch(
  () => props.messages,
  () => {
    scrollToBottom()
  },
  { deep: true },
)

// Scroll to bottom of messages
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// Initial scroll when component mounts
onMounted(() => {
  scrollToBottom()
})
</script>

<template>
  <div class="chat-conversation">
    <!-- Header with user and listing info -->
    <div v-if="activeConversation" class="conversation-header">
      <h2>{{ activeConversation.user.firstName }} {{ activeConversation.user.lastName }}</h2>
      <div class="listing-title">{{ activeConversation.listingTitle }}</div>
    </div>

    <div v-else class="conversation-header empty">
      <h2>Select a conversation</h2>
    </div>

    <!-- Messages area -->
    <div class="messages-container" ref="messagesContainer">
      <!-- Loading state -->
      <div v-if="isLoading" class="loading-state">
        <div class="spinner"></div>
        <p>Loading messages...</p>
      </div>

      <!-- Select conversation prompt -->
      <div v-else-if="!currentConversation" class="empty-state">
        <p>Select a conversation to start chatting</p>
      </div>

      <!-- No messages yet -->
      <div v-else-if="messages.length === 0" class="empty-state">
        <p>No messages yet</p>
        <p class="text-sm text-gray-500">Send a message to start the conversation</p>
      </div>

      <!-- Messages -->
      <div v-else class="messages-list">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message', isOwnMessage(message) ? 'sent' : 'received']"
        >
          <div class="message-content">
            {{ message.content }}
          </div>
          <div class="message-meta">
            {{ formatTime(message.timestamp) }}
          </div>
        </div>
      </div>
    </div>

    <!-- Message input -->
    <div v-if="currentConversation" class="message-input">
      <input
        v-model="messageInput"
        type="text"
        placeholder="Type a message..."
        @keyup.enter="handleSendMessage"
        :disabled="isSending"
      />
      <button
        @click="handleSendMessage"
        :disabled="!messageInput.trim() || isSending"
        class="send-button"
      >
        <span>Send</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.chat-conversation {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.conversation-header {
  padding: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.conversation-header h2 {
  margin: 0 0 4px 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.conversation-header .listing-title {
  color: #6b7280;
  font-size: 0.875rem;
}

.conversation-header.empty {
  text-align: center;
  color: #6b7280;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #6b7280;
  text-align: center;
}

.spinner {
  width: 24px;
  height: 24px;
  border: 2px solid #e5e7eb;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.messages-list {
  display: flex;
  flex-direction: column;
}

.message {
  max-width: 70%;
  margin-bottom: 12px;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.message.sent {
  align-self: flex-end;
}

.message.received {
  align-self: flex-start;
}

.message-content {
  padding: 10px 14px;
  border-radius: 16px;
  white-space: pre-wrap;
  word-break: break-word;
}

.message.sent .message-content {
  background-color: #3b82f6;
  color: white;
  border-bottom-right-radius: 4px;
}

.message.received .message-content {
  background-color: #f3f4f6;
  color: #1f2937;
  border-bottom-left-radius: 4px;
}

.message-meta {
  font-size: 0.75rem;
  color: #6b7280;
  margin-top: 2px;
  padding: 0 4px;
}

.message.sent .message-meta {
  align-self: flex-end;
}

.message-input {
  display: flex;
  padding: 12px 16px;
  border-top: 1px solid #e5e7eb;
}

.message-input input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #d1d5db;
  border-radius: 24px;
  font-size: 0.875rem;
  margin-right: 8px;
}

.message-input input:focus {
  outline: none;
  border-color: #3b82f6;
}

.send-button {
  padding: 0 20px;
  height: 40px;
  background-color: #3b82f6;
  color: white;
  font-weight: 500;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.send-button:hover:not(:disabled) {
  background-color: #2563eb;
}

.send-button:disabled {
  background-color: #93c5fd;
  cursor: not-allowed;
}
</style>

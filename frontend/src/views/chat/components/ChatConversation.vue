<script setup lang="ts">
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import type { components } from '@/lib/api/schema'
import { CardHeader, CardContent, CardFooter } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'

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
    <CardHeader v-if="activeConversation" class="conversation-header">
      <h2>{{ activeConversation.user.firstName }} {{ activeConversation.user.lastName }}</h2>
      <div class="listing-title">{{ activeConversation.listingTitle }}</div>
    </CardHeader>

    <CardHeader v-else class="conversation-header empty">
      <h2>Select a conversation</h2>
    </CardHeader>

    <!-- Messages area -->
    <CardContent class="messages-container" ref="messagesContainer">
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
        <p class="text-sm text-muted-foreground">Send a message to start the conversation</p>
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
    </CardContent>

    <!-- Message input -->
    <CardFooter v-if="currentConversation" class="message-input">
      <Input
        v-model="messageInput"
        type="text"
        placeholder="Type a message..."
        @keyup.enter="handleSendMessage"
        :disabled="isSending"
        class="message-input-field"
      />
      <Button
        @click="handleSendMessage"
        :disabled="!messageInput.trim() || isSending"
        class="send-button"
      >
        Send
      </Button>
    </CardFooter>
  </div>
</template>

<style scoped>
.chat-conversation {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.conversation-header h2 {
  margin: 0 0 4px 0;
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-medium);
}

.conversation-header .listing-title {
  color: var(--muted-foreground);
  font-size: var(--font-size-sm);
}

.conversation-header.empty {
  text-align: center;
  color: var(--muted-foreground);
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding-top: var(--spacing);
  padding-bottom: var(--spacing);
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--muted-foreground);
  text-align: center;
}

.spinner {
  width: 24px;
  height: 24px;
  border: 2px solid var(--muted);
  border-top-color: var(--primary);
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
  margin-bottom: calc(var(--spacing) * 2);
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
  padding: calc(var(--spacing) * 2) calc(var(--spacing) * 3);
  border-radius: var(--radius);
  white-space: pre-wrap;
  word-break: break-word;
}

.message.sent .message-content {
  background-color: var(--primary);
  color: var(--primary-foreground);
  border-bottom-right-radius: calc(var(--radius) / 2);
}

.message.received .message-content {
  background-color: var(--accent);
  color: var(--accent-foreground);
  border-bottom-left-radius: calc(var(--radius) / 2);
}

.message-meta {
  font-size: var(--font-size-xs);
  color: var(--muted-foreground);
  margin-top: 2px;
  padding: 0 4px;
}

.message.sent .message-meta {
  align-self: flex-end;
}

.message-input {
  display: flex;
  gap: calc(var(--spacing) * 2);
  padding: calc(var(--spacing) * 3);
}

.message-input-field {
  flex: 1;
}

.send-button {
  min-width: 80px;
}
</style>

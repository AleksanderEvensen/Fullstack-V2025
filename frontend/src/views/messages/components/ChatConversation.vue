<script setup lang="ts">
import { computed } from 'vue'
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
  isFetchingMore: boolean
  hasMore: boolean
  currentConversation: { otherUserId: number; listingId: number } | null
  conversations: ConversationSummary[]
  message: string
}>()

// Emits
const emit = defineEmits<{
  'update:message': [value: string]
  'send-message': []
  'load-more': []
}>()

const messageInput = computed({
  get: () => props.message,
  set: (value) => emit('update:message', value),
})

const userStore = useAuthStore()
const currentUserId = computed(() => userStore.user?.id)

const activeConversation = computed(() => {
  if (!props.currentConversation) return null

  return props.conversations.find(
    (conv) =>
      conv.user.id === props.currentConversation?.otherUserId &&
      conv.listingId === props.currentConversation?.listingId,
  )
})

const isOwnMessage = (message: ChatMessage) => {
  return message.sender.id === currentUserId.value
}

const formatTime = (timestamp: string) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const handleSendMessage = () => {
  if (!messageInput.value.trim() || !props.currentConversation) return
  emit('send-message')
}

const handleLoadMore = () => {
  emit('load-more')
}
</script>

<template>
  <div class="chat-conversation">
    <!-- Header with user and listing info -->
    <CardHeader v-if="activeConversation" class="conversation-header">
      <h2>{{ activeConversation.user.name }}</h2>
      <RouterLink
        :to="`/marketplace/product/${activeConversation.listingId}`"
        class="listing-title"
        >{{ activeConversation.listingTitle }}</RouterLink
      >
    </CardHeader>

    <CardHeader v-else class="conversation-header empty">
      <h2>Select a conversation</h2>
    </CardHeader>

    <!-- Messages area -->
    <CardContent class="messages-container">
      <!-- Loading state -->
      <div v-if="isLoading && !messages.length" class="loading-state">
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
        <!-- Load more button at the top -->
        <div v-if="hasMore" class="load-more-container">
          <Button variant="outline" size="sm" @click="handleLoadMore" :disabled="isFetchingMore">
            <span v-if="isFetchingMore">Loading earlier messages...</span>
            <span v-else>Load earlier messages</span>
          </Button>
        </div>

        <!-- Message list -->
        <div class="messages-chats">
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
        <span v-if="isSending">Sending...</span>
        <span v-else>Send</span>
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

.conversation-header {
  border-bottom: 1px solid var(--border);
}

.conversation-header h2 {
  margin: 0 0 4px 0;
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-medium);
}

.conversation-header .listing-title {
  color: var(--muted-foreground);
  font-size: var(--font-size-sm);
  text-decoration: underline;
}

.conversation-header .listing-title:hover {
  color: var(--foreground);
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

.messages-chats {
  display: flex;
  flex-direction: column-reverse;
}

.load-more-container {
  display: flex;
  justify-content: center;
  margin: 10px 0;
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

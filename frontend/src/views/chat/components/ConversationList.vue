<script setup lang="ts">
import type { components } from '@/lib/api/schema'

type ConversationSummary = components['schemas']['ConversationSummaryDto']

const props = defineProps<{
  conversations: ConversationSummary[]
  currentConversation: { otherUserId: number; listingId: number } | null
  isLoading: boolean
}>()

const emit = defineEmits<{
  (e: 'select-conversation', otherUserId: number, listingId: number): void
}>()

const isSelectedConversation = (conversation: ConversationSummary) => {
  if (!props.currentConversation) return false

  return (
    props.currentConversation.otherUserId === conversation.user.id &&
    props.currentConversation.listingId === conversation.listingId
  )
}

const selectConversation = (conversation: ConversationSummary) => {
  emit('select-conversation', conversation.user.id, conversation.listingId)
}

const formatTime = (timestamp: string) => {
  const date = new Date(timestamp)
  const now = new Date()

  if (date.toDateString() === now.toDateString()) {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  }

  if (date.getFullYear() === now.getFullYear()) {
    return date.toLocaleDateString([], { month: 'short', day: 'numeric' })
  }

  return date.toLocaleDateString([], { year: 'numeric', month: 'short', day: 'numeric' })
}

const truncateMessage = (message: string, length = 40) => {
  if (!message) return ''
  if (message.length <= length) return message
  return message.substring(0, length) + '...'
}

const getInitial = (name: string) => {
  return name ? name.charAt(0).toUpperCase() : '?'
}
</script>

<template>
  <div class="conversation-list">
    <div class="header">
      <h2>Conversations</h2>
    </div>

    <!-- Loading state -->
    <div v-if="isLoading" class="loading-state">
      <div class="spinner"></div>
      <p>Loading conversations...</p>
    </div>

    <!-- Empty state -->
    <div v-else-if="conversations.length === 0" class="empty-state">
      <p>No conversations yet</p>
      <p class="text-sm text-gray-500">Start chatting about a listing to see conversations here</p>
    </div>

    <!-- Conversations -->
    <div v-else class="conversations">
      <div
        v-for="conversation in conversations"
        :key="`${conversation.listingId}-${conversation.user.id}`"
        :class="['conversation-item', isSelectedConversation(conversation) ? 'selected' : '']"
        @click="selectConversation(conversation)"
      >
        <!-- User avatar or initial -->
        <div class="avatar">
          <img
            v-if="conversation.user.profileImageUrl"
            :src="conversation.user.profileImageUrl"
            alt="Profile"
          />
          <div v-else class="avatar-fallback">
            {{ getInitial(conversation.user.firstName) }}
          </div>
        </div>

        <!-- Conversation info -->
        <div class="conversation-info">
          <div class="conversation-header">
            <span class="user-name"
              >{{ conversation.user.firstName }} {{ conversation.user.lastName }}</span
            >
            <span v-if="conversation.lastMessage" class="timestamp">
              {{ formatTime(conversation.lastMessage.timestamp) }}
            </span>
          </div>

          <div class="listing-title">{{ conversation.listingTitle }}</div>

          <div v-if="conversation.lastMessage" class="last-message">
            {{ truncateMessage(conversation.lastMessage.content) }}
          </div>
          <div v-else class="no-messages">No messages yet</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.conversation-list {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.header {
  padding: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.header h2 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 2rem;
  text-align: center;
  color: #6b7280;
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

.conversations {
  flex: 1;
  overflow-y: auto;
}

.conversation-item {
  display: flex;
  padding: 12px 16px;
  border-bottom: 1px solid #e5e7eb;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.conversation-item:hover {
  background-color: #f9fafb;
}

.conversation-item.selected {
  background-color: #eff6ff;
  border-left: 3px solid #3b82f6;
}

.avatar {
  width: 40px;
  height: 40px;
  margin-right: 12px;
  flex-shrink: 0;
}

.avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-fallback {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #3b82f6;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 18px;
}

.conversation-info {
  flex: 1;
  min-width: 0; /* Helps text truncation */
}

.conversation-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.user-name {
  font-weight: 500;
  color: #111827;
}

.timestamp {
  font-size: 0.75rem;
  color: #6b7280;
}

.listing-title {
  font-size: 0.875rem;
  font-weight: 500;
  color: #4b5563;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.last-message {
  font-size: 0.875rem;
  color: #6b7280;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.no-messages {
  font-size: 0.875rem;
  color: #9ca3af;
  font-style: italic;
}
</style>

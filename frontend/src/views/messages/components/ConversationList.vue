<script setup lang="ts">
import type { components } from '@/lib/api/schema'
import { CardHeader, CardContent } from '@/components/ui/card'
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { useTypedI18n } from '@/i18n'
const { t } = useTypedI18n()

type ConversationSummary = components['schemas']['ConversationSummaryDto']

// Props
const props = defineProps<{
  conversations: ConversationSummary[]
  currentConversation: { otherUserId: number; listingId: number } | null
  isLoading: boolean
  isFetchingMore: boolean
  hasMore: boolean
}>()

// Emits
const emit = defineEmits<{
  (e: 'select-conversation', otherUserId: number, listingId: number): void
  (e: 'load-more'): void
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

const loadMoreConversations = () => {
  emit('load-more')
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
    <CardHeader class="header">
      <h2>{{ t('messages.title') }}</h2>
    </CardHeader>

    <CardContent class="conversations-content">
      <!-- Loading state -->
      <div v-if="isLoading && conversations.length === 0" class="loading-state">
        <div class="spinner"></div>
        <p>Loading conversations...</p>
      </div>

      <!-- Empty state -->
      <div v-else-if="conversations.length === 0" class="empty-state">
        <p>No conversations yet</p>
        <p class="text-sm text-muted-foreground">
          Start chatting about a listing to see conversations here
        </p>
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
          <Avatar class="avatar">
            <AvatarImage
              v-if="conversation.user.profileImageUrl"
              :src="conversation.user.profileImageUrl"
              alt="Profile"
            />
            <AvatarFallback>
              {{ getInitial(conversation.user.name) }}
            </AvatarFallback>
          </Avatar>

          <!-- Conversation info -->
          <div class="conversation-info">
            <div class="conversation-header">
              <span class="user-name">
                {{ conversation.user.name }}
              </span>
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

        <!-- Load more button -->
        <div v-if="hasMore" class="load-more-container">
          <Button
            variant="outline"
            size="sm"
            @click="loadMoreConversations"
            :disabled="isFetchingMore"
            class="load-more-button"
          >
            <span v-if="isFetchingMore">Loading more...</span>
            <span v-else>Load more conversations</span>
          </Button>
        </div>
      </div>
    </CardContent>
  </div>
</template>

<style scoped>
.conversation-list {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.header h2 {
  margin: 0;
  font-weight: var(--font-weight-medium);
}

.conversations-content {
  flex: 1;
  overflow-y: auto;
  padding-top: 0;
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
  color: var(--muted-foreground);
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

.conversations {
  flex: 1;
  overflow-y: auto;
}

.conversation-item {
  display: flex;
  padding: calc(var(--spacing) * 2);
  /* border-bottom: 1px solid var(--border); */
  cursor: pointer;
  transition: background-color 0.2s ease;
  position: relative;
}

.conversation-item:not(:last-child)::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background-color: var(--border);
  transform: scaleX(1);
  transition: transform 0.2s ease;
}

.conversation-item:hover {
  background-color: var(--accent);
}

.conversation-item.selected {
  background-color: var(--accent);
  border-left: 3px solid var(--primary);
}

.avatar {
  margin-right: calc(var(--spacing) * 2);
  flex-shrink: 0;
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
  font-weight: var(--font-weight-medium);
  color: var(--foreground);
}

.timestamp {
  font-size: var(--font-size-xs);
  color: var(--muted-foreground);
}

.listing-title {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--muted-foreground);
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.last-message {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.no-messages {
  font-size: var(--font-size-sm);
  color: var(--muted-foreground);
  font-style: italic;
}

.load-more-container {
  display: flex;
  justify-content: center;
  padding: 1rem;
}

.load-more-button {
  width: 100%;
}
</style>

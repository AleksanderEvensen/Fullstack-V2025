<script setup lang="ts">
import { useChatMessages } from '@/lib/composables/useChatMessages'
import ConversationList from './components/ConversationList.vue'
import ChatConversation from './components/ChatConversation.vue'
import { Card } from '@/components/ui/card'
import { watch } from 'vue'

const {
  conversations,
  messages,
  currentConversation,
  newMessage,
  isLoadingConversations,
  isLoadingMessages,
  isSendingMessage,
  hasMoreConversations,
  hasMoreMessages,
  isFetchingNextConversations,
  isFetchingNextMessages,
  setConversation,
  sendMessage,
  fetchNextConversationsPage,
  fetchNextMessagesPage,
} = useChatMessages()

// We auto-select the first conversation when data loads
watch(
  conversations,
  (newConversations) => {
    if (newConversations.length > 0 && !currentConversation.value) {
      const firstConversation = newConversations[0]
      setConversation(firstConversation.user.id, firstConversation.listingId)
    }
  },
  { immediate: true },
)
</script>

<template>
  <div class="chat-page container">
    <Card class="conversation-sidebar">
      <ConversationList
        :conversations="conversations"
        :current-conversation="currentConversation"
        :is-loading="isLoadingConversations"
        :is-fetching-more="isFetchingNextConversations"
        :has-more="hasMoreConversations"
        @select-conversation="setConversation"
        @load-more="fetchNextConversationsPage"
      />
    </Card>

    <Card class="conversation-main">
      <ChatConversation
        :messages="messages"
        :is-loading="isLoadingMessages"
        :is-sending="isSendingMessage"
        :is-fetching-more="isFetchingNextMessages"
        :has-more="hasMoreMessages"
        :current-conversation="currentConversation"
        :conversations="conversations"
        v-model:message="newMessage"
        @send-message="sendMessage"
        @load-more="fetchNextMessagesPage"
      />
    </Card>
  </div>
</template>

<style scoped>
.chat-page {
  display: flex;
  height: 100%;
  height: calc(100vh - 65px);
  gap: var(--spacing);
  padding: var(--spacing);
  width: 100%;
}

.conversation-sidebar {
  width: 320px;
  overflow-y: auto;
}

.conversation-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

@media (max-width: 768px) {
  .chat-page {
    flex-direction: column;
  }

  .conversation-sidebar {
    width: 100%;
    height: 50%;
  }

  .conversation-main {
    height: 50%;
  }
}
</style>

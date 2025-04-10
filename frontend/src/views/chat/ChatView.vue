<script setup lang="ts">
import { useChatMessages } from '@/lib/composables/useChat'
import ConversationList from './components/ConversationList.vue'
import ChatConversation from './components/ChatConversation.vue'
import { Card } from '@/components/ui/card'

const {
  conversations,
  messages,
  currentConversation,
  newMessage,
  isLoadingConversations,
  isLoadingMessages,
  isSendingMessage,
  setConversation,
  sendMessage,
} = useChatMessages()
</script>

<template>
  <div class="chat-page container">
    <Card class="conversation-sidebar">
      <ConversationList
        :conversations="conversations"
        :current-conversation="currentConversation"
        :is-loading="isLoadingConversations"
        @select-conversation="setConversation"
      />
    </Card>

    <Card class="conversation-main">
      <ChatConversation
        :messages="messages"
        :is-loading="isLoadingMessages"
        :is-sending="isSendingMessage"
        :current-conversation="currentConversation"
        :conversations="conversations"
        v-model:message="newMessage"
        @send-message="sendMessage"
      />
    </Card>
  </div>
</template>

<style scoped>
.chat-page {
  display: flex;
  height: 100%;
  min-height: calc(100vh - 64px); /* Adjust based on your app's header height */
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

/* Responsive adjustments */
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

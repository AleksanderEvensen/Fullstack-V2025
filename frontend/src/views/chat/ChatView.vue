<script setup lang="ts">
import { useChatMessages } from '@/lib/composables/useChat'
import ConversationList from './components/ConversationList.vue'
import ChatConversation from './components/ChatConversation.vue'

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
  <div class="chat-page">
    <div class="conversation-sidebar">
      <ConversationList
        :conversations="conversations"
        :current-conversation="currentConversation"
        :is-loading="isLoadingConversations"
        @select-conversation="setConversation"
      />
    </div>

    <div class="conversation-main">
      <ChatConversation
        :messages="messages"
        :is-loading="isLoadingMessages"
        :is-sending="isSendingMessage"
        :current-conversation="currentConversation"
        :conversations="conversations"
        v-model:message="newMessage"
        @send-message="sendMessage"
      />
    </div>
  </div>
</template>

<style scoped>
.chat-page {
  display: flex;
  height: 100%;
  min-height: calc(100vh - 64px); /* Adjust based on your app's header height */
}

.conversation-sidebar {
  width: 320px;
  border-right: 1px solid #e5e7eb;
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
    border-right: none;
    border-bottom: 1px solid #e5e7eb;
  }

  .conversation-main {
    height: 50%;
  }
}
</style>

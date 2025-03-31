<script setup lang="ts">
import { ref } from 'vue'
import Button from '@/components/ui/button/Button.vue'

interface Message {
    id: string
    user: string
    avatar: string
    lastMessage: string
    timestamp: string
    unread: number
    product?: {
        title: string
        price: number
        image: string
    }
}

const messages = ref<Message[]>([
    {
        id: '1',
        user: 'Alice Johnson',
        avatar: 'https://picsum.photos/id/64/50/50',
        lastMessage: 'Is this still available?',
        timestamp: '2:30 PM',
        unread: 2,
        product: {
            title: 'Sony WH-1000XM4 Headphones',
            price: 1500,
            image: 'https://picsum.photos/id/1/100/100'
        }
    },
    {
        id: '2',
        user: 'Bob Smith',
        avatar: 'https://picsum.photos/id/65/50/50',
        lastMessage: 'Would you accept 1200 kr?',
        timestamp: '1:45 PM',
        unread: 0,
        product: {
            title: 'iPhone 13 Pro',
            price: 8000,
            image: 'https://picsum.photos/id/2/100/100'
        }
    }
])

const selectedMessage = ref<Message | null>(null)
const newMessage = ref('')

const sendMessage = () => {
    if (!newMessage.value.trim() || !selectedMessage.value) return

    // TODO: Implement message sending
    console.log('Sending message:', newMessage.value)
    newMessage.value = ''
}
</script>

<template>
    <div class="messages">
        <div class="messages-sidebar">
            <h2>Messages</h2>
            <div class="messages-list">
                <div v-for="message in messages" :key="message.id" class="message-item"
                    :class="{ active: selectedMessage?.id === message.id }" @click="selectedMessage = message">
                    <img :src="message.avatar" :alt="message.user" class="avatar">
                    <div class="message-content">
                        <div class="message-header">
                            <span class="user">{{ message.user }}</span>
                            <span class="timestamp">{{ message.timestamp }}</span>
                        </div>
                        <p class="last-message">{{ message.lastMessage }}</p>
                        <div v-if="message.product" class="product-preview">
                            <img :src="message.product.image" :alt="message.product.title">
                            <div class="product-info">
                                <p class="product-title">{{ message.product.title }}</p>
                                <p class="product-price">kr {{ message.product.price }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="chat-view">
            <div v-if="selectedMessage" class="chat-header">
                <img :src="selectedMessage.avatar" :alt="selectedMessage.user" class="avatar">
                <div class="chat-info">
                    <h3>{{ selectedMessage.user }}</h3>
                    <p class="product-title">{{ selectedMessage.product?.title }}</p>
                </div>
            </div>

            <div v-else class="no-chat">
                <p>Select a conversation to start messaging</p>
            </div>

            <div v-if="selectedMessage" class="chat-messages">
                <!-- Chat messages will go here -->
                <div class="message-bubble received">
                    <p>{{ selectedMessage.lastMessage }}</p>
                </div>
            </div>

            <div v-if="selectedMessage" class="chat-input">
                <input v-model="newMessage" type="text" placeholder="Type a message..." @keyup.enter="sendMessage">
                <Button @click="sendMessage">Send</Button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.messages {
    display: grid;
    grid-template-columns: 300px 1fr;
    gap: 1rem;
    height: calc(100vh - 64px);
    background-color: var(--background);
}

.messages-sidebar {
    border-right: 1px solid var(--border);
    display: flex;
    flex-direction: column;
}

.messages-sidebar h2 {
    padding: 1rem;
    margin: 0;
    border-bottom: 1px solid var(--border);
}

.messages-list {
    flex: 1;
    overflow-y: auto;
}

.message-item {
    display: flex;
    padding: 1rem;
    gap: 1rem;
    cursor: pointer;
    border-bottom: 1px solid var(--border);
    transition: background-color 0.2s;
}

.message-item:hover {
    background-color: var(--accent);
}

.message-item.active {
    background-color: var(--accent);
}

.avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
}

.message-content {
    flex: 1;
    min-width: 0;

}

.message-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.25rem;
}

.user {
    font-weight: 600;
    color: var(--foreground);
}

.timestamp {
    font-size: 0.875rem;
    color: var(--muted-foreground);
}

.last-message {
    margin: 0;
    color: var(--muted-foreground);
    font-size: 0.875rem;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.product-preview {
    display: flex;
    gap: 0.5rem;
    margin-top: 0.5rem;
    padding: 0.5rem;
    background-color: var(--accent);
    border-radius: 0.25rem;
}

.product-preview img {
    width: 40px;
    height: 40px;
    border-radius: 0.25rem;
    object-fit: cover;
}

.product-info {
    flex: 1;
    min-width: 0;
}

.product-title {
    margin: 0;
    font-size: 0.875rem;
    color: var(--foreground);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.product-price {
    margin: 0;
    font-size: 0.875rem;
    color: var(--muted-foreground);
}

.unread-badge {
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;
    background-color: var(--primary);
    color: white;
    font-size: 0.75rem;
    padding: 0.125rem 0.375rem;
    border-radius: 1rem;
}

.chat-view {
    display: flex;
    flex-direction: column;
}

.chat-header {
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 1rem;
    border-bottom: 1px solid var(--border);
}

.chat-info h3 {
    margin: 0;
    color: var(--foreground);
}

.chat-messages {
    flex: 1;
    padding: 1rem;
    overflow-y: auto;
}

.message-bubble {
    max-width: 70%;
    padding: 0.75rem;
    border-radius: 1rem;
    margin-bottom: 1rem;
}

.message-bubble.received {
    background-color: var(--accent);
    color: var(--foreground);
}

.chat-input {
    display: flex;
    gap: 0.5rem;
    padding: 1rem;
    border-top: 1px solid var(--border);
}

.chat-input input {
    flex: 1;
    padding: 0.75rem;
    border: 1px solid var(--border);
    border-radius: 0.5rem;
    background-color: var(--background);
    color: var(--foreground);
}

.chat-input input:focus {
    outline: none;
    border-color: var(--primary);
}

.no-chat {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    color: var(--muted-foreground);
}

@media (max-width: 768px) {
    .messages {
        grid-template-columns: 1fr;
    }

    .messages-sidebar {
        display: none;
    }

    .messages-sidebar.active {
        display: block;
        position: fixed;
        top: 64px;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 50;
    }
}
</style>
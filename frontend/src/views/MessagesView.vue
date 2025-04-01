<script setup lang="ts">
import { ref } from 'vue'
import Button from '@/components/ui/button/Button.vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Input } from '@/components/ui/input';

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
        <Card class="messages-sidebar">
            <CardHeader>
                <CardTitle>Messages</CardTitle>
            </CardHeader>
            <CardContent class="messages-list">
                <div v-for="message in messages" :key="message.id" class="message-item"
                    :class="{ active: selectedMessage?.id === message.id }" @click="selectedMessage = message">
                    <Avatar class="message-avatar">
                        <AvatarImage :src="message.avatar" :alt="message.user" />
                        <AvatarFallback>{{ message.user[0] }}</AvatarFallback>
                    </Avatar>
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
            </CardContent>
        </Card>

        <Card class="chat-view">
            <CardHeader v-if="selectedMessage" class="chat-header">
                <div class="chat-header-content">
                    <Avatar class="chat-avatar">
                        <AvatarImage :src="selectedMessage.avatar" :alt="selectedMessage.user" />
                        <AvatarFallback>{{ selectedMessage.user[0] }}</AvatarFallback>
                    </Avatar>
                    <div class="chat-info">
                        <CardTitle>{{ selectedMessage.user }}</CardTitle>
                        <p class="product-title">{{ selectedMessage.product?.title }}</p>
                    </div>
                </div>
            </CardHeader>

            <CardContent v-if="selectedMessage" class="chat-messages">
                <div class="message-bubble received">
                    <p>{{ selectedMessage.lastMessage }}</p>
                </div>
            </CardContent>

            <CardContent v-else class="no-chat">
                <p>Select a conversation to start messaging</p>
            </CardContent>

            <CardContent v-if="selectedMessage" class="chat-input">
                <div class="input-container">
                    <Input v-model="newMessage" type="text" placeholder="Type a message..."
                        @keyup.enter="sendMessage" />
                    <Button @click="sendMessage">Send</Button>
                </div>
            </CardContent>
        </Card>
    </div>
</template>

<style scoped>
.messages {
    display: grid;
    grid-template-columns: 350px 1fr;
    gap: calc(var(--spacing) * 4);
    height: 100%;
    padding: calc(var(--spacing) * 4);
    background-color: var(--background);
}

.messages-sidebar {
    height: 100%;
    display: flex;
    flex-direction: column;
}

.messages-list {
    flex: 1;
    overflow-y: auto;
    padding: 0;
}

.message-item {
    display: flex;
    padding: calc(var(--spacing) * 4);
    gap: calc(var(--spacing) * 4);
    cursor: pointer;
    border-bottom: 1px solid var(--border);
    transition: all 0.2s ease;
}

.message-item:hover {
    background-color: var(--accent);
}

.message-item.active {
    background-color: var(--accent);
}

.message-avatar {
    width: 40px;
    height: 40px;
}

.message-content {
    flex: 1;
    min-width: 0;
}

.message-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: calc(var(--spacing) * 1);
}

.user {
    font-weight: var(--font-weight-semibold);
    color: var(--foreground);
}

.timestamp {
    font-size: var(--text-sm);
    color: var(--muted-foreground);
}

.last-message {
    margin: 0;
    color: var(--muted-foreground);
    font-size: var(--text-sm);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.product-preview {
    display: flex;
    gap: calc(var(--spacing) * 2);
    margin-top: calc(var(--spacing) * 2);
    padding: calc(var(--spacing) * 2);
    background-color: var(--accent);
    border-radius: var(--radius);
}

.product-preview img {
    width: 40px;
    height: 40px;
    border-radius: var(--radius);
    object-fit: cover;
}

.product-info {
    flex: 1;
    min-width: 0;
}

.product-title {
    margin: 0;
    font-size: var(--text-sm);
    color: var(--foreground);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.product-price {
    margin: 0;
    font-size: var(--text-sm);
    color: var(--muted-foreground);
}

.chat-view {
    height: 100%;
    display: flex;
    flex-direction: column;
}

.chat-header {
    padding: calc(var(--spacing) * 4);
    border-bottom: 1px solid var(--border);
}

.chat-header-content {
    display: flex;
    align-items: center;
    gap: calc(var(--spacing) * 4);
}

.chat-avatar {
    width: 48px;
    height: 48px;
}

.chat-info {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 1);
}

.chat-messages {
    flex: 1;
    padding: calc(var(--spacing) * 4);
    overflow-y: auto;
}

.message-bubble {
    max-width: 70%;
    padding: calc(var(--spacing) * 3);
    border-radius: var(--radius);
    margin-bottom: calc(var(--spacing) * 4);
}

.message-bubble.received {
    background-color: var(--accent);
    color: var(--foreground);
}

.chat-input {
    padding: calc(var(--spacing) * 4);
    border-top: 1px solid var(--border);
}

.input-container {
    display: flex;
    gap: calc(var(--spacing) * 2);
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
        padding: 0;
        gap: 0;
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

    .chat-view {
        border-radius: 0;
    }
}
</style>
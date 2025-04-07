package edu.ntnu.fullstack.amazoom.chat.exception

/**
 * Exception thrown when a message is not found
 */
class MessageNotFoundException(messageId: Long) :
    RuntimeException("Message with ID $messageId not found")

/**
 * Exception thrown when a user tries to access messages they don't have permission to access
 */
class MessageAccessDeniedException :
    RuntimeException("You don't have permission to access this message")

/**
 * Exception thrown when a user tries to send a message to a non-existent user
 */
class RecipientNotFoundException(recipientId: Long) :
    RuntimeException("Recipient with ID $recipientId not found")

/**
 * Generic exception for chat-related errors
 */
class ChatException(message: String) : RuntimeException(message)
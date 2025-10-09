package com.example.category.entity;

import java.time.LocalDateTime;

public class ChatMessage {
    private String content;
    private String sender;
    private String senderType; // "customer" or "agent"
    private LocalDateTime timestamp;
    private String sessionId;

    // Default constructor
    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with parameters
    public ChatMessage(String content, String sender, String senderType, String sessionId) {
        this.content = content;
        this.sender = sender;
        this.senderType = senderType;
        this.sessionId = sessionId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", senderType='" + senderType + '\'' +
                ", timestamp=" + timestamp +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}

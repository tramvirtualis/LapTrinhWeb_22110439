package com.example.category.service;

import com.example.category.entity.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {
    
    // Store active chat sessions
    private final Map<String, List<ChatMessage>> chatSessions = new ConcurrentHashMap<>();
    
    // Store active users in each session
    private final Map<String, Set<String>> sessionUsers = new ConcurrentHashMap<>();
    
    /**
     * Add a message to a chat session
     */
    public void addMessage(String sessionId, ChatMessage message) {
        chatSessions.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(message);
    }
    
    /**
     * Get all messages for a session
     */
    public List<ChatMessage> getMessages(String sessionId) {
        return chatSessions.getOrDefault(sessionId, new ArrayList<>());
    }
    
    /**
     * Add user to a session
     */
    public void addUserToSession(String sessionId, String username) {
        sessionUsers.computeIfAbsent(sessionId, k -> new HashSet<>()).add(username);
    }
    
    /**
     * Remove user from a session
     */
    public void removeUserFromSession(String sessionId, String username) {
        Set<String> users = sessionUsers.get(sessionId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                sessionUsers.remove(sessionId);
            }
        }
    }
    
    /**
     * Get active users in a session
     */
    public Set<String> getSessionUsers(String sessionId) {
        return sessionUsers.getOrDefault(sessionId, new HashSet<>());
    }
    
    /**
     * Get all active sessions
     */
    public Set<String> getActiveSessions() {
        return new HashSet<>(chatSessions.keySet());
    }
    
    /**
     * Create a new chat session
     */
    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        chatSessions.put(sessionId, new ArrayList<>());
        sessionUsers.put(sessionId, new HashSet<>());
        return sessionId;
    }
    
    /**
     * Check if session exists
     */
    public boolean sessionExists(String sessionId) {
        return chatSessions.containsKey(sessionId);
    }
}

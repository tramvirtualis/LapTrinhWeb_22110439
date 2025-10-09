package com.example.category.controller;

import com.example.category.entity.ChatMessage;
import com.example.category.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handle incoming chat messages
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        logger.info("📨 Received message from {} ({}): {}", 
            chatMessage.getSender(), chatMessage.getSenderType(), chatMessage.getContent());
        
        // Store the message in the service
        chatService.addMessage(chatMessage.getSessionId(), chatMessage);
        
        // Broadcast to ALL connected clients
        logger.info("📡 Broadcasting message to /topic/public for session: {}", chatMessage.getSessionId());
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
        logger.info("✅ Message broadcasted successfully");
    }

    /**
     * Handle user joining the chat
     */
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("👤 User {} ({}) joining session: {}", 
            chatMessage.getSender(), chatMessage.getSenderType(), chatMessage.getSessionId());
        
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("sessionId", chatMessage.getSessionId());
        
        // Add user to session
        chatService.addUserToSession(chatMessage.getSessionId(), chatMessage.getSender());
        
        // Broadcast join message to ALL connected clients
        logger.info("📡 Broadcasting join message to /topic/public");
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
        logger.info("✅ Join message broadcasted successfully");
    }

    /**
     * Handle user leaving the chat
     */
    @MessageMapping("/chat.removeUser")
    public void removeUser(@Payload ChatMessage chatMessage) {
        chatService.removeUserFromSession(chatMessage.getSessionId(), chatMessage.getSender());
        
        // Broadcast leave message to ALL connected clients
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

    /**
     * Serve the chat page for customers
     */
    @GetMapping("/chat")
    public String chatPage(Model model) {
        String sessionId = java.util.UUID.randomUUID().toString();
        model.addAttribute("sessionId", sessionId);
        return "chat/chat";
    }

    /**
     * Serve the chat page for support agents
     */
    @GetMapping("/admin/chat")
    public String adminChatPage(Model model) {
        model.addAttribute("isAdmin", true);
        return "chat/admin-chat";
    }

    /**
     * Join a specific chat session (for agents)
     */
    @GetMapping("/admin/chat/{sessionId}")
    public String joinChatSession(@PathVariable String sessionId, Model model) {
        if (chatService.sessionExists(sessionId)) {
            model.addAttribute("sessionId", sessionId);
            model.addAttribute("isAdmin", true);
            return "chat/admin-chat";
        } else {
            return "redirect:/admin/chat";
        }
    }

    /**
     * Chat test page for debugging
     */
    @GetMapping("/chat/test")
    public String chatTestPage() {
        return "chat/test";
    }
}

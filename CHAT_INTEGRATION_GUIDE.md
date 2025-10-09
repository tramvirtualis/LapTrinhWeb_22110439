# Customer Support Chat Integration Guide

This guide explains how to integrate the WebSocket-based customer support chat feature into your existing Spring Boot application.

## 🚀 Quick Start

### 1. Dependencies Added
The following dependency has been added to your `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 2. Files Created

#### Backend Components:
- `src/main/java/com/example/category/config/WebSocketConfig.java` - WebSocket configuration
- `src/main/java/com/example/category/entity/ChatMessage.java` - Message entity
- `src/main/java/com/example/category/service/ChatService.java` - Chat business logic
- `src/main/java/com/example/category/controller/ChatController.java` - WebSocket message handling

#### Frontend Components:
- `src/main/resources/templates/chat/chat.html` - Customer chat interface
- `src/main/resources/templates/chat/admin-chat.html` - Admin chat dashboard
- `src/main/resources/static/js/chat.js` - WebSocket client JavaScript
- `src/main/resources/static/css/chat.css` - Chat styling

## 🔧 Configuration

### WebSocket Endpoints
- **WebSocket URL**: `/chat`
- **Message Destination**: `/app/chat.sendMessage`
- **Subscription Topic**: `/topic/public`

### Access URLs
- **Customer Chat**: `http://localhost:8080/chat`
- **Admin Dashboard**: `http://localhost:8080/admin/chat`
- **Join Specific Session**: `http://localhost:8080/admin/chat/{sessionId}`

## 🎯 Key Features

### Real-time Communication
- ✅ WebSocket with STOMP protocol
- ✅ In-memory message broker
- ✅ Session-based chat rooms
- ✅ User presence tracking

### User Interface
- ✅ Responsive design with Bootstrap 5
- ✅ Customer and admin interfaces
- ✅ Real-time message display
- ✅ Connection status indicators
- ✅ Mobile-friendly design

### Security & Integration
- ✅ Excluded from authentication interceptor
- ✅ Session-based message routing
- ✅ Flexible user type handling (customer/agent)

## 🔌 Integration Points

### 1. Authentication Integration
To integrate with your existing authentication system, modify the `ChatController.java`:

```java
// In ChatController.java - modify the chatPage method
@GetMapping("/chat")
public String chatPage(HttpSession session, Model model) {
    // Get user from session
    User currentUser = (User) session.getAttribute("user");
    String sessionId = java.util.UUID.randomUUID().toString();
    
    model.addAttribute("sessionId", sessionId);
    model.addAttribute("username", currentUser.getUsername());
    return "chat/chat";
}
```

### 2. Database Integration
To persist chat messages, create a `ChatMessage` entity with JPA:

```java
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String sender;
    private String senderType;
    private LocalDateTime timestamp;
    private String sessionId;
    
    // Getters, setters, constructors...
}
```

### 3. User Management Integration
Update the JavaScript to use authenticated user data:

```javascript
// In chat.js - modify the constructor
constructor() {
    this.sessionId = document.getElementById('sessionId').value;
    this.isAdmin = document.getElementById('isAdmin').value === 'true';
    this.username = document.getElementById('username').value || 'Anonymous';
    this.senderType = this.isAdmin ? 'agent' : 'customer';
    // ... rest of constructor
}
```

## 🎨 Customization

### Styling
- Modify `src/main/resources/static/css/chat.css` for custom styling
- Colors, fonts, and layout can be easily customized
- Responsive breakpoints are included

### Message Types
Add new message types by extending the `ChatMessage` entity:

```java
public enum MessageType {
    TEXT, IMAGE, FILE, SYSTEM
}
```

### Notifications
Add browser notifications for new messages:

```javascript
// In chat.js - add to displayMessage method
if (Notification.permission === 'granted' && !document.hasFocus()) {
    new Notification('New message from ' + message.sender, {
        body: message.content,
        icon: '/images/chat-icon.png'
    });
}
```

## 🚀 Deployment Notes

### Production Considerations
1. **Message Persistence**: Implement database storage for chat messages
2. **Session Management**: Use Redis or database for session storage
3. **Load Balancing**: Configure sticky sessions for WebSocket connections
4. **Security**: Add authentication checks to WebSocket endpoints

### Environment Configuration
Add to `application.properties`:

```properties
# WebSocket configuration
spring.websocket.stomp.relay.host=localhost
spring.websocket.stomp.relay.port=61613
spring.websocket.stomp.relay.login=guest
spring.websocket.stomp.relay.passcode=guest
```

## 🧪 Testing

### Manual Testing
1. Open two browser windows
2. Navigate to `/chat` in one (customer)
3. Navigate to `/admin/chat` in another (agent)
4. Send messages between them

### Automated Testing
Create WebSocket integration tests:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatIntegrationTest {
    
    @Test
    void testWebSocketConnection() {
        // Test WebSocket connection and message sending
    }
}
```

## 📱 Mobile Support

The chat interface is fully responsive and works on:
- ✅ Desktop browsers
- ✅ Mobile devices
- ✅ Tablets
- ✅ Touch interfaces

## 🔧 Troubleshooting

### Common Issues
1. **WebSocket Connection Failed**: Check CORS settings in `WebSocketConfig.java`
2. **Messages Not Appearing**: Verify session ID matching
3. **Styling Issues**: Ensure CSS file is being loaded
4. **JavaScript Errors**: Check browser console for errors

### Debug Mode
Enable WebSocket debugging in `application.properties`:

```properties
logging.level.org.springframework.web.socket=DEBUG
logging.level.org.springframework.messaging=DEBUG
```

## 📚 Next Steps

1. **Add File Sharing**: Implement file upload in chat
2. **Add Emoji Support**: Include emoji picker
3. **Add Message History**: Load previous messages on connect
4. **Add Typing Indicators**: Show when users are typing
5. **Add Chat Rooms**: Support multiple chat rooms
6. **Add Push Notifications**: Mobile push notifications

---

**Ready to use!** Your customer support chat system is now integrated and ready for testing. 🎉

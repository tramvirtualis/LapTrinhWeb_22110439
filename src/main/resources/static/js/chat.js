// WebSocket Chat Client
class ChatClient {
    constructor() {
        this.stompClient = null;
        this.sessionId = document.getElementById('sessionId').value;
        this.isAdmin = document.getElementById('isAdmin').value === 'true';
        this.currentSessionId = this.sessionId;
        this.username = this.isAdmin ? 'Support Agent' : 'Customer';
        this.senderType = this.isAdmin ? 'agent' : 'customer';
        
        this.initializeElements();
        this.connect();
    }

    initializeElements() {
        this.messageInput = document.getElementById('messageInput');
        this.sendButton = document.getElementById('sendButton');
        this.chatMessages = document.getElementById('chatMessages');
        this.connectionStatus = document.getElementById('connectionStatus');
        this.activeSessions = document.getElementById('activeSessions');
    }

    connect() {
        const socket = new SockJS('/chat');
        this.stompClient = Stomp.over(socket);
        
        this.stompClient.connect({}, (frame) => {
            console.log('✅ WebSocket Connected: ' + frame);
            this.updateConnectionStatus('Connected', 'success');
            this.enableInput();
            
            // Subscribe to public messages
            this.stompClient.subscribe('/topic/public', (message) => {
                console.log('📡 Received message on /topic/public:', JSON.parse(message.body));
                this.displayMessage(JSON.parse(message.body));
            });

            // Join the chat
            this.joinChat();
            
            // If admin, load active sessions
            if (this.isAdmin) {
                this.loadActiveSessions();
            }
        }, (error) => {
            console.log('❌ WebSocket Connection error: ' + error);
            this.updateConnectionStatus('Disconnected', 'danger');
            this.disableInput();
        });
    }

    joinChat() {
        const chatMessage = {
            content: this.username + ' joined the chat',
            sender: this.username,
            senderType: this.senderType,
            sessionId: this.currentSessionId,
            timestamp: new Date().toISOString()
        };
        
        this.stompClient.send("/app/chat.addUser", {}, JSON.stringify(chatMessage));
    }

    sendMessage() {
        const messageContent = this.messageInput.value.trim();
        if (messageContent && this.stompClient) {
            const chatMessage = {
                content: messageContent,
                sender: this.username,
                senderType: this.senderType,
                sessionId: this.currentSessionId,
                timestamp: new Date().toISOString()
            };
            
            this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            this.messageInput.value = '';
        }
    }

    displayMessage(message) {
        console.log('📨 Received message:', message);
        console.log('🔍 Current session:', this.currentSessionId);
        console.log('👤 Is admin:', this.isAdmin);
        
        // Display messages based on user type and session
        let shouldDisplay = false;
        
        if (this.isAdmin) {
            // Admin can see messages from any customer session
            shouldDisplay = message.senderType === 'customer' || message.senderType === 'agent';
        } else {
            // Customer can see messages from their session or from agents
            shouldDisplay = message.sessionId === this.currentSessionId || message.senderType === 'agent';
        }
        
        if (!shouldDisplay) {
            console.log('❌ Message filtered out');
            return;
        }
        
        console.log('✅ Displaying message');

        const messageElement = document.createElement('div');
        messageElement.className = `message ${message.senderType === 'agent' ? 'agent-message' : 'customer-message'}`;
        
        const timestamp = new Date(message.timestamp).toLocaleTimeString();
        
        messageElement.innerHTML = `
            <div class="message-header">
                <strong>${message.sender}</strong>
                <small class="text-muted">${timestamp}</small>
            </div>
            <div class="message-content">${this.escapeHtml(message.content)}</div>
        `;
        
        this.chatMessages.appendChild(messageElement);
        this.scrollToBottom();
    }

    loadActiveSessions() {
        // This would typically be loaded from the server
        // For now, we'll simulate with the current session
        if (this.sessionId) {
            this.addSessionToList(this.sessionId);
        }
    }

    addSessionToList(sessionId) {
        const sessionElement = document.createElement('a');
        sessionElement.href = '#';
        sessionElement.className = 'list-group-item list-group-item-action';
        sessionElement.textContent = `Session: ${sessionId.substring(0, 8)}...`;
        sessionElement.onclick = (e) => {
            e.preventDefault();
            this.joinSession(sessionId);
        };
        
        if (this.activeSessions) {
            this.activeSessions.appendChild(sessionElement);
        }
    }

    joinSession(sessionId) {
        this.currentSessionId = sessionId;
        this.chatMessages.innerHTML = '';
        this.joinChat();
    }

    updateConnectionStatus(status, type) {
        if (this.connectionStatus) {
            this.connectionStatus.textContent = status;
            this.connectionStatus.className = `badge bg-${type}`;
        }
    }

    enableInput() {
        this.messageInput.disabled = false;
        this.sendButton.disabled = false;
        this.messageInput.focus();
    }

    disableInput() {
        this.messageInput.disabled = true;
        this.sendButton.disabled = true;
    }

    scrollToBottom() {
        this.chatMessages.scrollTop = this.chatMessages.scrollHeight;
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Event Listeners
document.addEventListener('DOMContentLoaded', function() {
    const chatClient = new ChatClient();
    
    // Send message on button click
    document.getElementById('sendButton').addEventListener('click', function() {
        chatClient.sendMessage();
    });
    
    // Send message on Enter key
    document.getElementById('messageInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            chatClient.sendMessage();
        }
    });
});

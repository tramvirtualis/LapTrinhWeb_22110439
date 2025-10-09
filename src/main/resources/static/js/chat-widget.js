// Chat Widget JavaScript
class ChatWidget {
    constructor() {
        this.stompClient = null;
        this.sessionId = this.generateSessionId();
        this.username = 'Customer';
        this.senderType = 'customer';
        this.isConnected = false;
        
        this.initializeElements();
        this.setupEventListeners();
        this.connect();
    }

    initializeElements() {
        this.chatButton = document.getElementById('chatButton');
        this.chatWidget = document.getElementById('chatWidget');
        this.closeButton = document.getElementById('closeChat');
        this.messageInput = document.getElementById('messageInput');
        this.sendButton = document.getElementById('sendButton');
        this.chatMessages = document.getElementById('chatMessages');
        this.connectionStatus = document.getElementById('widgetConnectionStatus');
    }

    setupEventListeners() {
        // Toggle chat widget
        this.chatButton.addEventListener('click', () => {
            this.toggleChat();
        });

        // Close chat widget
        this.closeButton.addEventListener('click', () => {
            this.hideChat();
        });

        // Send message
        this.sendButton.addEventListener('click', () => {
            this.sendMessage();
        });

        // Send message on Enter key
        this.messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.sendMessage();
            }
        });
    }

    generateSessionId() {
        return 'widget_' + Math.random().toString(36).substr(2, 9);
    }

    connect() {
        const socket = new SockJS('/chat');
        this.stompClient = Stomp.over(socket);
        
        this.stompClient.connect({}, (frame) => {
            console.log('✅ Chat Widget Connected: ' + frame);
            this.isConnected = true;
            this.updateConnectionStatus('Connected', 'success');
            
            // Subscribe to public messages
            this.stompClient.subscribe('/topic/public', (message) => {
                console.log('📡 Widget received message on /topic/public:', JSON.parse(message.body));
                this.displayMessage(JSON.parse(message.body));
            });

            // Join the chat
            this.joinChat();
        }, (error) => {
            console.log('❌ Chat Widget Connection error: ' + error);
            this.isConnected = false;
            this.updateConnectionStatus('Disconnected', 'danger');
        });
    }

    joinChat() {
        if (!this.isConnected) return;
        
        const chatMessage = {
            content: this.username + ' joined the chat',
            sender: this.username,
            senderType: this.senderType,
            sessionId: this.sessionId,
            timestamp: new Date().toISOString()
        };
        
        this.stompClient.send("/app/chat.addUser", {}, JSON.stringify(chatMessage));
    }

    toggleChat() {
        if (this.chatWidget.style.display === 'none') {
            this.showChat();
        } else {
            this.hideChat();
        }
    }

    showChat() {
        this.chatWidget.style.display = 'flex';
        this.messageInput.focus();
        
        // Add a welcome message if no messages exist
        if (this.chatMessages.children.length === 0) {
            this.addWelcomeMessage();
        }
    }

    hideChat() {
        this.chatWidget.style.display = 'none';
    }

    addWelcomeMessage() {
        const welcomeMessage = document.createElement('div');
        welcomeMessage.className = 'message agent-message';
        welcomeMessage.innerHTML = `
            <div class="message-header">
                <strong>Support Team</strong>
            </div>
            <div class="message-content">Hello! How can we help you today?</div>
        `;
        this.chatMessages.appendChild(welcomeMessage);
        this.scrollToBottom();
    }

    sendMessage() {
        const messageContent = this.messageInput.value.trim();
        if (messageContent && this.isConnected) {
            const chatMessage = {
                content: messageContent,
                sender: this.username,
                senderType: this.senderType,
                sessionId: this.sessionId,
                timestamp: new Date().toISOString()
            };
            
            this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            this.messageInput.value = '';
        }
    }

    displayMessage(message) {
        console.log('📨 Widget received message:', message);
        console.log('🔍 Widget session:', this.sessionId);
        
        // Display messages from the current session OR from agents
        let shouldDisplay = message.sessionId === this.sessionId || message.senderType === 'agent';
        
        if (!shouldDisplay) {
            console.log('❌ Widget message filtered out');
            return;
        }
        
        console.log('✅ Widget displaying message');

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

    scrollToBottom() {
        this.chatMessages.scrollTop = this.chatMessages.scrollHeight;
    }

    updateConnectionStatus(status, type) {
        if (this.connectionStatus) {
            this.connectionStatus.textContent = status;
            this.connectionStatus.className = `badge bg-${type} me-2`;
        }
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Initialize chat widget when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    new ChatWidget();
});

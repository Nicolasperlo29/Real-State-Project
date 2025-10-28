package org.example.messageservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Cliente se suscribe a /topic
        config.enableSimpleBroker("/topic");
        // Mensajes enviados desde el cliente a /app serán manejados por el servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Punto de conexión WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // permitir Angular
                .withSockJS(); // fallback si no hay soporte WS nativo
    }
}

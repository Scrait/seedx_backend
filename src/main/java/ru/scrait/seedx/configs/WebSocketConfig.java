package ru.scrait.seedx.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.scrait.seedx.controllers.KeyWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final KeyWebSocketHandler keyWebSocketHandler;

    public WebSocketConfig(KeyWebSocketHandler keyWebSocketHandler) {
        this.keyWebSocketHandler = keyWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(keyWebSocketHandler, "/ws/{userId}")
                .setAllowedOriginPatterns("*") // Здесь можно указать разрешенные домены
                .addInterceptors(auctionInterceptor());
    }

    @Bean
    public HandshakeInterceptor auctionInterceptor() {
        return new HandshakeInterceptor() {
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                // Get the URI segment corresponding to the auction id during handshake
                String path = request.getURI().getPath();
                String userId = path.substring(path.lastIndexOf('/') + 1);

                // This will be added to the websocket session
                attributes.put("userId", userId);
                return true;
            }

            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
                // Nothing to do after handshake
            }
        };
    }

}
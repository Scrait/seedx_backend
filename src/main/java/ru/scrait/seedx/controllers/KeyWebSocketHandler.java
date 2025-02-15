package ru.scrait.seedx.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.scrait.seedx.dtos.GetOrCreateWsResponse;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.repositories.KeyRepository;
import ru.scrait.seedx.utils.DateUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KeyWebSocketHandler extends TextWebSocketHandler {

    private final KeyRepository keyRepository;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> userSessions;

    public KeyWebSocketHandler(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
        this.objectMapper = new ObjectMapper();
        this.userSessions = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
            System.out.println("WebSocket connection established for userId: " + userId);
        } else {
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing userId"));
                System.err.println("WebSocket connection rejected: Missing userId");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        // Здесь можно обработать входящие сообщения, если потребуется
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
            System.out.println("WebSocket connection closed for userId: " + userId);
        }
    }

    public void sendMessageToUser(String userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                System.out.println("Message sent to userId: " + userId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Failed to send message: No active session for userId: " + userId);
        }
    }

    public void broadcastUpdatedCoins(Key key) {
        if (key != null) {
            try {
                GetOrCreateWsResponse response = new GetOrCreateWsResponse(
                        key.isSub(),
                        key.isSub() ? key.getCurrencies() : Set.of(Key.CryptoCurrency.TRON),
                        DateUtil.formatDate(key.getSubscriptionExpirationDate()),
                        key.getSpeed()
                );

                String message = objectMapper.writeValueAsString(response);
                sendMessageToUser(key.getId(), message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("No key found for userId: " + key.getId());
        }
    }

    public void broadcastUpdatedSpeed(Key key) {
        if (key != null) {
            try {
                GetOrCreateWsResponse response = new GetOrCreateWsResponse(
                        key.isSub(),
                        key.isSub() ? key.getCurrencies() : Set.of(Key.CryptoCurrency.TRON),
                        DateUtil.formatDate(key.getSubscriptionExpirationDate()),
                        key.getSpeed()
                );

                String message = objectMapper.writeValueAsString(response);
                sendMessageToUser(key.getId(), message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("No key found for userId: " + key.getId());
        }
    }



    public void broadcastUpdatedCoins(String userId) {
        Key key = keyRepository.findById(userId).orElse(null);
        broadcastUpdatedCoins(key);
    }

    private String getUserIdFromSession(WebSocketSession session) {
        Object userId = session.getAttributes().get("userId");
        return userId != null ? userId.toString() : null;
    }
}

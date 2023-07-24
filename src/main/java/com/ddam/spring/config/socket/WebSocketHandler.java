package com.ddam.spring.config.socket;

import com.ddam.spring.dto.request.ChatMessageRequest;
import com.ddam.spring.service.crew.CrewChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.ddam.spring.dto.request.ChatMessageRequest.MessageType.CHAT;
import static com.ddam.spring.dto.request.ChatMessageRequest.MessageType.ENTER;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final CrewChatService chatService;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("{}", payload);
        ChatMessageRequest request = objectMapper.readValue(payload, ChatMessageRequest.class);
        handlerActions(session, request);
    }

    private void handlerActions(WebSocketSession session, ChatMessageRequest request) {
        if(request.getType().equals(CHAT)) {
            chatService.save(request.getSenderId(), request.getMessage());
        }

        if (request.getType().equals(ENTER)) {
            sessions.add(session);
            request.setMessage(request.getSenderName() + "님이 입장했습니다.");
        }

        sendMessage(request);
    }

    private void sendMessage(ChatMessageRequest request) {
        sessions.parallelStream().forEach(
                session -> {
                    try {
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(request)));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
        );
    }
}

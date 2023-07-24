package com.ddam.spring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatMessageRequest{

    private MessageType type;

    private Long senderId;

    private String senderName;

    @Setter
    private String message;

    public enum MessageType{
        ENTER, CHAT
    }
}

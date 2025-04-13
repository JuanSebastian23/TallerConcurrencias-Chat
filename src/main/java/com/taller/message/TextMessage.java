package com.taller.message;

public class TextMessage implements Message {
    private final String content;
    private final String sender;

    public TextMessage(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getFormattedMessage() {
        return sender + ": " + content;
    }
} 
package com.taller.message;

public class AlertMessage implements Message {
    private final String content;
    private final String sender;

    public AlertMessage(String content, String sender) {
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
        return "¡ALERTA! " + sender + ": " + content;
    }
} 
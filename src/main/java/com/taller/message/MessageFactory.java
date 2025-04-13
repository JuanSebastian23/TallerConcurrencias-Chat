package com.taller.message;

public class MessageFactory {
    public static Message createMessage(String type, String content, String sender) {
        switch (type.toLowerCase()) {
            case "text":
                return new TextMessage(content, sender);
            case "alert":
                return new AlertMessage(content, sender);
            case "notification":
                return new NotificationMessage(content, sender);
            default:
                throw new IllegalArgumentException("Tipo de mensaje no válido: " + type);
        }
    }
} 
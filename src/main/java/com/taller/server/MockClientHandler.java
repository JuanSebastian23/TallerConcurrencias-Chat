package com.taller.server;

import java.util.function.Consumer;

public class MockClientHandler {
    private final String username;
    private final Consumer<String> messageHandler;

    public MockClientHandler(String username, Consumer<String> messageHandler) {
        this.username = username;
        this.messageHandler = messageHandler;
    }

    public void receiveMessage(String message) {
        if (messageHandler != null) {
            messageHandler.accept(message);
        }
    }

    public String getUsername() {
        return username;
    }
} 
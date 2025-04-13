package com.taller.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public class MockServer {
    private static MockServer instance;
    private final ConcurrentHashMap<String, MockClientHandler> clients;
    private final List<String> messageHistory;
    private boolean isRunning;

    private MockServer() {
        this.clients = new ConcurrentHashMap<>();
        this.messageHistory = new ArrayList<>();
        this.isRunning = false;
    }

    public static synchronized MockServer getInstance() {
        if (instance == null) {
            instance = new MockServer();
        }
        return instance;
    }

    public synchronized void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.println("Mock Server iniciado");
        }
    }

    public synchronized void stop() {
        if (isRunning) {
            isRunning = false;
            clients.clear();
            messageHistory.clear();
            System.out.println("Mock Server detenido");
        }
    }

    public synchronized boolean login(String username, MockClientHandler handler) {
        if (!isRunning) return false;
        if (username == null || username.trim().isEmpty()) return false;
        if (clients.containsKey(username)) return false;

        clients.put(username, handler);
        broadcastMessage("Usuario " + username + " se ha conectado", null);
        broadcastUserList();
        return true;
    }

    public synchronized void logout(String username) {
        if (clients.remove(username) != null) {
            broadcastMessage("Usuario " + username + " se ha desconectado", null);
            broadcastUserList();
        }
    }

    public synchronized void broadcastMessage(String message, String sender) {
        if (!isRunning) return;
        messageHistory.add(message);
        clients.forEach((username, handler) -> {
            if (!username.equals(sender)) {
                handler.receiveMessage(message);
            }
        });
    }

    private synchronized void broadcastUserList() {
        if (!isRunning) return;
        StringBuilder userList = new StringBuilder("/users ");
        clients.keySet().forEach(username -> userList.append(username).append(","));
        String users = userList.toString();
        clients.values().forEach(handler -> handler.receiveMessage(users));
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized int getConnectedClientsCount() {
        return clients.size();
    }

    public synchronized List<String> getMessageHistory() {
        return new ArrayList<>(messageHistory);
    }
} 
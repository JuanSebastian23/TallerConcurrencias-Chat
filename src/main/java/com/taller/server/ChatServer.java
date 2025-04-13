package com.taller.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 8080;
    public static Set<PrintWriter> writers = new HashSet<>();
    public static Map<String, String> activeUsers = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando servidor de chat...");
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Servidor iniciado en puerto " + PORT);
        
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static void broadcastUserList() {
        StringBuilder userList = new StringBuilder("/users ");
        activeUsers.keySet().forEach(name -> userList.append(name).append(","));
        String users = userList.toString();
        for (PrintWriter writer : writers) {
            writer.println(users);
        }
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (activeUsers) {
                        if (!name.isEmpty() && !activeUsers.containsKey(name)) {
                            activeUsers.put(name, socket.getRemoteSocketAddress().toString());
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED " + name);
                writers.add(out);
                broadcastUserList();

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    activeUsers.remove(name);
                    broadcastUserList();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static Thread getInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInstance'");
    }
} 
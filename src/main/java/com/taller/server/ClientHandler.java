package com.taller.server;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Proceso de login
            while (true) {
                out.println("SUBMITNAME");
                username = in.readLine();
                if (username == null) {
                    return;
                }
                synchronized (ChatServer.writers) {
                    if (!username.isEmpty()) {
                        ChatServer.writers.add(out);
                        break;
                    }
                }
            }

            out.println("NAMEACCEPTED " + username);

            // Bucle principal de mensajes
            while (true) {
                String input = in.readLine();
                if (input == null) {
                    return;
                }
                for (PrintWriter writer : ChatServer.writers) {
                    writer.println("MESSAGE " + username + ": " + input);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (out != null) {
                ChatServer.writers.remove(out);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
} 
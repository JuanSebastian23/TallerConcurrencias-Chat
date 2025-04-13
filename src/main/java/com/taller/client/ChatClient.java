package com.taller.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame {
    private BufferedReader in;
    private PrintWriter out;
    private JTextField messageField;
    private JTextArea chatArea;
    private JTextField usernameField;
    private JButton loginButton;
    private JButton sendButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private Socket socket;
    private String username;
    private boolean isLoggedIn = false;

    public ChatClient() {
        setTitle("Chat Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Panel de login
        JPanel loginPanel = new JPanel();
        usernameField = new JTextField(15);
        loginButton = new JButton("Login");
        loginPanel.add(new JLabel("Usuario:"));
        loginPanel.add(usernameField);
        loginPanel.add(loginButton);
        add(loginPanel, BorderLayout.NORTH);

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        add(chatScroll, BorderLayout.CENTER);

        // Lista de usuarios
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(150, 0));
        add(userScroll, BorderLayout.EAST);

        // Panel de mensajes
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setEnabled(false);
        sendButton = new JButton("Enviar");
        sendButton.setEnabled(false);
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        add(messagePanel, BorderLayout.SOUTH);

        // Eventos
        loginButton.addActionListener(e -> handleLogin());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        usernameField.addActionListener(e -> handleLogin());

        // Evento de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        username = usernameField.getText().trim();
        if (validateUsername(username)) {
            try {
                socket = new Socket("localhost", 8080);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Esperar el SUBMITNAME del servidor
                String line = in.readLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(username);
                    line = in.readLine();
                    if (line.startsWith("NAMEACCEPTED")) {
                        isLoggedIn = true;
                        loginButton.setEnabled(false);
                        usernameField.setEnabled(false);
                        messageField.setEnabled(true);
                        sendButton.setEnabled(true);
                        messageField.requestFocus();
                        
                        // Iniciar hilo para recibir mensajes
                        new Thread(this::receiveMessages).start();
                        
                        chatArea.append("Conectado al servidor.\n");
                    }
                }
            } catch (IOException e) {
                showError("Error al conectar con el servidor: " + e.getMessage());
            }
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("MESSAGE")) {
                    final String finalMessage = message.substring(8);
                    SwingUtilities.invokeLater(() -> chatArea.append(finalMessage + "\n"));
                } else if (message.startsWith("/users")) {
                    final String[] users = message.substring(7).split(",");
                    SwingUtilities.invokeLater(() -> {
                        userListModel.clear();
                        for (String user : users) {
                            if (!user.trim().isEmpty() && !user.equals(username)) {
                                userListModel.addElement(user.trim());
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                SwingUtilities.invokeLater(() -> showError("Error al recibir mensajes: " + e.getMessage()));
            }
        }
    }

    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            showError("Por favor ingrese un nombre de usuario");
            return false;
        }
        if (username.length() < 3) {
            showError("El nombre de usuario debe tener al menos 3 caracteres");
            return false;
        }
        if (username.length() > 15) {
            showError("El nombre de usuario no puede tener más de 15 caracteres");
            return false;
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            showError("El nombre de usuario solo puede contener letras y números");
            return false;
        }
        return true;
    }

    private void sendMessage() {
        if (!isLoggedIn || !messageField.isEnabled()) {
            return;
        }

        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            chatArea.append("Tú: " + message + "\n");
            messageField.setText("");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void disconnect() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }
} 
package com.taller.client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulatedChatClient extends JFrame {
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JTextField usernameField;
    private final JList<String> userList;
    private final DefaultListModel<String> userListModel;
    private String username;
    private boolean isLoggedIn;
    private JButton sendButton;
    private JButton loginButton;
    private final List<String> simulatedUsers;
    private final ScheduledExecutorService scheduler;
    private final Random random;

    public SimulatedChatClient() {
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

        // Inicializar variables
        simulatedUsers = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(1);
        random = new Random();

        // Eventos
        loginButton.addActionListener(e -> handleLogin());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        // Validación de entrada de usuario
        usernameField.addActionListener(e -> handleLogin());

        // Evento de cierre de ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                cleanupAndExit();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        username = usernameField.getText().trim();
        if (validateUsername(username)) {
            isLoggedIn = true;
            loginButton.setEnabled(false);
            usernameField.setEnabled(false);
            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            messageField.requestFocus();
            
            // Simular conexión exitosa
            chatArea.append("Conectado al servidor.\n");
            
            // Simular otros usuarios
            simulatedUsers.add("Usuario1");
            simulatedUsers.add("Usuario2");
            simulatedUsers.add("Usuario3");
            updateUserList();
            
            // Simular mensajes de otros usuarios
            startSimulatedMessages();
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
            chatArea.append("Tú: " + message + "\n");
            messageField.setText("");
        }
    }

    private void startSimulatedMessages() {
        scheduler.scheduleAtFixedRate(() -> {
            if (isLoggedIn && !simulatedUsers.isEmpty()) {
                String randomUser = simulatedUsers.get(random.nextInt(simulatedUsers.size()));
                String[] messages = {
                    "Hola a todos!",
                    "¿Cómo están?",
                    "Buen día!",
                    "¿Alguien quiere chatear?",
                    "Me encanta este chat!",
                    "¿Qué tal el día?",
                    "¡Saludos desde mi ubicación!"
                };
                String message = messages[random.nextInt(messages.length)];
                chatArea.append(randomUser + ": " + message + "\n");
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    private void updateUserList() {
        userListModel.clear();
        userListModel.addElement(username);
        for (String user : simulatedUsers) {
            userListModel.addElement(user);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void cleanupAndExit() {
        scheduler.shutdown();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimulatedChatClient::new);
    }
} 
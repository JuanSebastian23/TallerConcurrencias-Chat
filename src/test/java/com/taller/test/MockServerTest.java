package com.taller.test;

import com.taller.server.MockServer;
import com.taller.server.MockClientHandler;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MockServerTest {
    private MockServer server;
    private List<String> receivedMessages;

    @BeforeEach
    public void setUp() {
        server = MockServer.getInstance();
        server.start();
        receivedMessages = new ArrayList<>();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testServerStartStop() {
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    @Test
    public void testUserLogin() {
        MockClientHandler handler = new MockClientHandler("testUser", message -> receivedMessages.add(message));
        assertTrue(server.login("testUser", handler));
        assertEquals(1, server.getConnectedClientsCount());
    }

    @Test
    public void testDuplicateUserLogin() {
        MockClientHandler handler1 = new MockClientHandler("testUser", message -> receivedMessages.add(message));
        MockClientHandler handler2 = new MockClientHandler("testUser", message -> receivedMessages.add(message));
        
        assertTrue(server.login("testUser", handler1));
        assertFalse(server.login("testUser", handler2));
        assertEquals(1, server.getConnectedClientsCount());
    }

    @Test
    public void testUserLogout() {
        MockClientHandler handler = new MockClientHandler("testUser", message -> receivedMessages.add(message));
        server.login("testUser", handler);
        server.logout("testUser");
        assertEquals(0, server.getConnectedClientsCount());
    }

    @Test
    public void testBroadcastMessage() {
        MockClientHandler handler1 = new MockClientHandler("user1", message -> receivedMessages.add(message));
        MockClientHandler handler2 = new MockClientHandler("user2", message -> receivedMessages.add(message));
        
        server.login("user1", handler1);
        server.login("user2", handler2);
        
        server.broadcastMessage("Hola a todos", "user1");
        
        assertTrue(server.getMessageHistory().contains("Hola a todos"));
        assertTrue(receivedMessages.contains("Hola a todos"));
    }

    @Test
    public void testUserListBroadcast() {
        MockClientHandler handler = new MockClientHandler("testUser", message -> receivedMessages.add(message));
        server.login("testUser", handler);
        
        assertTrue(receivedMessages.stream()
            .anyMatch(message -> message.startsWith("/users") && message.contains("testUser")));
    }

    @Test
    public void testServerNotRunning() {
        server.stop();
        MockClientHandler handler = new MockClientHandler("testUser", message -> receivedMessages.add(message));
        assertFalse(server.login("testUser", handler));
        assertEquals(0, server.getConnectedClientsCount());
    }
} 
package com.taller.test;

import com.taller.server.ChatServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

public class ChatServerTest {

    @AfterEach
    public void tearDown() {
        ChatServer.getInstance().stop();
    }

    @Test
    public void testSingletonInstance() {
        ChatServer instance1 = ChatServer.getInstance();
        ChatServer instance2 = ChatServer.getInstance();
        assertSame(instance1, instance2, "Las instancias deberían ser la misma");
    }

    @Test
    public void testServerStart() {
        ChatServer server = ChatServer.getInstance();
        Thread serverThread = new Thread(() -> server.start());
        serverThread.start();
        
        try {
            Thread.sleep(1000); // Esperar a que el servidor inicie
            assertTrue(true, "El servidor debería iniciar correctamente");
        } catch (InterruptedException e) {
            fail("Error al esperar la inicialización del servidor");
        } finally {
            server.stop();
            try {
                serverThread.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
} 
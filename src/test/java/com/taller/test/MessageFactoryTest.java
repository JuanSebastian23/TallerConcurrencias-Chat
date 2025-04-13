package com.taller.test;

import com.taller.message.Message;
import com.taller.message.MessageFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageFactoryTest {
    @Test
    public void testCreateTextMessage() {
        Message message = MessageFactory.createMessage("text", "Hola", "Usuario1");
        assertEquals("Usuario1: Hola", message.getFormattedMessage());
    }

    @Test
    public void testCreateAlertMessage() {
        Message message = MessageFactory.createMessage("alert", "Peligro", "Sistema");
        assertEquals("¡ALERTA! Sistema: Peligro", message.getFormattedMessage());
    }

    @Test
    public void testCreateNotificationMessage() {
        Message message = MessageFactory.createMessage("notification", "Nuevo mensaje", "Admin");
        assertEquals("Notificación de Admin: Nuevo mensaje", message.getFormattedMessage());
    }

    @Test
    public void testInvalidMessageType() {
        assertThrows(IllegalArgumentException.class, () -> {
            MessageFactory.createMessage("invalid", "Mensaje", "Usuario");
        });
    }
} 
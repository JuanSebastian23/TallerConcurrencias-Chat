# Chat en Tiempo Real con Java

Este es un proyecto de chat en tiempo real implementado en Java, que permite la comunicación entre múltiples usuarios a través de una interfaz gráfica.

## Características

- Interfaz gráfica profesional usando Swing
- Soporte para múltiples usuarios simultáneos
- Lista de usuarios activos en tiempo real
- Validación de nombres de usuario
- Mensajes privados y públicos
- Sistema de login simple y efectivo

## Requisitos

- Java 8 o superior
- Maven para la compilación

## Estructura del Proyecto

```
proyecto/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── taller/
│   │               ├── client/
│   │               │   └── ChatClient.java       # Cliente con interfaz gráfica
│   │               └── server/
│   │                   ├── ChatServer.java       # Servidor principal
│   │                   └── ClientHandler.java    # Manejador de conexiones de cliente
│   └── test/
│       └── java/
│           └── com/
│               └── taller/
│                   ├── client/
│                   │   └── ChatClientTest.java   # Tests del cliente
│                   └── server/
│                       ├── ChatServerTest.java   # Tests del servidor
│                       └── ClientHandlerTest.java # Tests del manejador de clientes
├── pom.xml                # Configuración de Maven
├── start-all.bat         # Script para iniciar servidor y clientes
└── stop-all.bat          # Script para detener todo el sistema

Archivos Principales:
- ChatClient.java: Implementa la interfaz gráfica y la lógica del cliente
- ChatServer.java: Implementa el servidor y manejo de conexiones
- ClientHandler.java: Maneja las conexiones individuales de los clientes
- start-all.bat: Inicia el servidor y dos instancias del cliente
- stop-all.bat: Detiene todas las instancias en ejecución

Tests:
- ChatClientTest.java: Pruebas unitarias del cliente
- ChatServerTest.java: Pruebas unitarias del servidor
- ClientHandlerTest.java: Pruebas unitarias del manejador de clientes
```

## Cómo Ejecutar

1. Compilar el proyecto:
```bash
mvn clean compile
```

2. Iniciar el servidor y los clientes:
```bash
start-all.bat
```

3. Para detener todo:
```bash
stop-all.bat
```

## Uso

1. Al iniciar, se abrirán dos ventanas de cliente de chat
2. En cada cliente:
   - Ingresa un nombre de usuario (3-15 caracteres, solo letras y números)
   - Haz clic en "Login" o presiona Enter
   - Una vez conectado, puedes enviar mensajes
3. La lista de usuarios activos se muestra en el panel derecho
4. Los mensajes se muestran en el área central
5. Para enviar un mensaje, escribe en el campo inferior y presiona Enter o el botón "Enviar"

## Validaciones

- Nombres de usuario:
  - Mínimo 3 caracteres
  - Máximo 15 caracteres
  - Solo letras y números permitidos
  - No se permiten nombres duplicados

## Características de la Interfaz

- Panel superior: Login de usuario
- Panel central: Área de chat
- Panel derecho: Lista de usuarios conectados
- Panel inferior: Campo para enviar mensajes

## Notas Técnicas

- El servidor utiliza el puerto 8080 por defecto
- Implementa manejo de conexiones concurrentes
- Usa Java Swing para la interfaz gráfica
- Implementa patrón Observer para actualizaciones en tiempo real 
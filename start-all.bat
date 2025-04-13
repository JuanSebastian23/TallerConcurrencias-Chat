@echo off
echo Iniciando el servidor y clientes de chat...
echo.

echo Iniciando servidor en el puerto 8080...
start "Chat Server" cmd /c "java -cp target/classes com.taller.server.ChatServer"
timeout /t 2 /nobreak > nul

echo Iniciando cliente 1...
start "Chat Client 1" cmd /c "java -cp target/classes com.taller.client.ChatClient"
timeout /t 1 /nobreak > nul

echo Iniciando cliente 2...
start "Chat Client 2" cmd /c "java -cp target/classes com.taller.client.ChatClient"

echo.
echo Servidor y 2 clientes iniciados.
echo Presione cualquier tecla para salir...
pause > nul 
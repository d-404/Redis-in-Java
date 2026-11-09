package components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.lang.Thread;

@Component
public class TCPServer {
    private static final Logger log = LoggerFactory.getLogger(TCPServer.class);
    private static final int PORT = 6379;

    private final RespSerializer respSerializer;

    public TCPServer(RespSerializer respSerializer) {
        this.respSerializer = respSerializer;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(PORT));
            log.info("TCP server listening on port {}", PORT);
            int id = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                id++;
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                Client client = new Client(clientSocket, inputStream, outputStream, id);
                Thread.ofVirtual().start(() -> handleClient(client));
            }
        } catch (IOException e) {
            log.error("Server socket error: ", e);
        }
    }

    private void handleClient(Client client) {
        try (Socket socket = client.getSocket()) {
            byte[] buffer = new byte[socket.getReceiveBufferSize()];
            while (true) {
                int bytesRead = client.getInputStream().read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                List<String[]> result = respSerializer.deserialize(buffer);
                for (String[] strings : result) {
                    for (String string : strings) {
                        log.info("{} ", string);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error handling client {}: ", client.getId(), e);
        } finally {
            log.info("Client {} disconnected", client.getId());
        }
    }
}
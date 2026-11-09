package components;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
@Slf4j
public class TCPServer {
    private static RespSerializer respSerializer;

    public void startServer() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            int id = 0;
            while (true) {
                clientSocket = serverSocket.accept();
                id++;
                Socket finalClientSocket = clientSocket;
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                Client client = new Client(finalClientSocket, inputStream, outputStream, id);
                CompletableFuture.runAsync(() -> {
                    try {
                        handleClient(client);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            log.error("Some IO Exception Occurred : ", e);
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    log.error("Some IO exception occurred while closing connection : ", e);
                }
            }
        }
    }

    private static void handleClient(Client client) throws IOException {
        while (client.getSocket().isConnected()) {
            byte[] buffer = new byte[client.getSocket().getReceiveBufferSize()];
            int bytesRead = client.getInputStream().read(buffer);
            if (bytesRead > 0) {
                List<String[]> result = respSerializer.deserialize(buffer);
                for (String[] strings : result) {
                    for (String string : strings) {
                        log.info(string, " ");
                    }
                }
            }
        }
    }
}

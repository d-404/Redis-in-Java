package components;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TCPServer {
    private RespSerializer respSerializer;

    public void startServer() {
        respSerializer.printWorking();
    }
}

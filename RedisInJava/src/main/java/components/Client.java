package components;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Data
@Component
@AllArgsConstructor
public class Client {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int id;
}

package components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int id;

}

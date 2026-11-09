import components.TCPServer;
import config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TCPServer tcpServer = context.getBean(TCPServer.class);
        tcpServer.startServer();
    }
}

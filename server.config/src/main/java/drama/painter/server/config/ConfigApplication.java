package drama.painter.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author murphy
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {
    public static final String LOCAL_PATH;

    static {
        String path = ConfigApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.endsWith(".jar") ? path.substring(0, path.lastIndexOf("/")) : path;
        LOCAL_PATH = path.endsWith("/") || path.endsWith("\\") ? path.substring(0, path.length() - 1) : path;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}

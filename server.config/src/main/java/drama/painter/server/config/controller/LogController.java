package drama.painter.server.config.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author murphy
 */
@RestController
public class LogController {
    @GetMapping("/log/setGlobal")
    public String changeLevel(String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.toLevel(level));
        return "操作成功：" + level;
    }
}

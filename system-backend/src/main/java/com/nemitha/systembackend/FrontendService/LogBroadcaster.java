package com.nemitha.systembackend.FrontendService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class LogBroadcaster {

    private final LogStreamingController logStreamingController;

    public LogBroadcaster(LogStreamingController logStreamingController) {
        this.logStreamingController = logStreamingController;
    }

    @PostConstruct
    public void setupLogInterceptor() {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        rootLogger.addAppender(new ch.qos.logback.core.AppenderBase<>() {
            @Override
            protected void append(ch.qos.logback.classic.spi.ILoggingEvent eventObject) {
                String logMessage = eventObject.getFormattedMessage();
                logStreamingController.broadcastLog(logMessage); // Broadcast logs to clients
            }
        });
    }

}

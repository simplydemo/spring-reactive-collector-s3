package io.github.simplydemo.logback.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptorChain;

public class LogbackContextListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    private boolean started = false;

    public void start() {
        if (started) {
            return;
        }

        String logDir = System.getProperty("log.dir", "/tmp/logs");
        String logFile = System.getProperty("log.file", "collector");
        String region = System.getProperty("aws.region", "ap-northeast-2");
        String bucket = System.getProperty("aws.bucket", "data-collect-s3");
        String profile = System.getProperty("aws.profile");

        /*
         System.out.println(logDir);
         System.out.println(logFile);
         System.out.println(region);
         System.out.println(bucket);
         System.out.println(profile);
        */

        Context context = getContext();
        context.putProperty("LOG_DIR", logDir);
        context.putProperty("LOG_FILE", logFile);
        context.putProperty("AWS_REGION", region);
        context.putProperty("AWS_BUCKET", bucket);
        context.putProperty("AWS_PROFILE", profile);
        started = true;
    }

    public void stop() {
        started = false;
    }

    public boolean isStarted() {
        return started;
    }


    public boolean isResetResistant() {
        return true;
    }

    public void onStart(LoggerContext loggerContext) {

    }

    public void onReset(LoggerContext loggerContext) {

    }

    public void onStop(LoggerContext loggerContext) {

    }

    public void onLevelChange(Logger logger, Level level) {
    }

}

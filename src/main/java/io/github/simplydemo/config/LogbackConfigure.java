package io.github.simplydemo.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.FileSize;
import unit.github.simplydemo.logback.ext.rolling.SizeAndTimeBasedS3RollingPolicy;
import io.github.thenovaworks.logback.encoder.MessageOnlyEncoder;
import org.slf4j.LoggerFactory;

public class LogbackConfigure extends ContextAwareBase implements Configurator {

    /*
    <appender name="S3FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/tmp/logs/collector.log</file>
        <append>true</append>
        <rollingPolicy class="io.github.simplydemo.logback.ext.rolling.SizeAndTimeBasedS3RollingPolicy">
            <profile>opsdev-sts</profile>
            <region>ap-northeast-1</region>
            <bucket>otcmp-tbd-artifact-s3</bucket>
            <fileNamePattern>/tmp/logs/collector.%d{yyyyMMdd-HHmm}.%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <totalSizeCap>100MB</totalSizeCap>
            <maxHistory>3</maxHistory>
        </rollingPolicy>
        <encoder class="io.github.thenovaworks.logback.encoder.MessageOnlyEncoder"/>
    </appender>
     */

    public Logger create(String category,
                         String region,
                         String profile,
                         String bucket,
                         String file,
                         String fileNamePattern,
                         String maxFileSize,
                         String totalSizeCap,
                         int maxHistory) {

        System.out.println("LogbackConfigure.create -------------" + category);

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        MessageOnlyEncoder encoder = new MessageOnlyEncoder();
        encoder.setContext(lc);
        encoder.start();

        final RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<ILoggingEvent>();
        appender.setContext(lc);
        appender.setFile(file);
        appender.setAppend(true);
        appender.setEncoder(encoder);

        final SizeAndTimeBasedS3RollingPolicy policy = new SizeAndTimeBasedS3RollingPolicy();
        policy.setContext(lc);
        policy.setParent(appender);
        policy.setRegion(region);
        policy.setProfile(profile);
        policy.setBucket(bucket);
        policy.setFileNamePattern(fileNamePattern);
        policy.setMaxFileSize(FileSize.valueOf(maxFileSize));
        policy.setMaxHistory(maxHistory);
        policy.setTotalSizeCap(FileSize.valueOf(totalSizeCap));
        policy.start();

        appender.setRollingPolicy(policy);
        appender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(category);
        logger.addAppender(appender);
        logger.setLevel(Level.TRACE);
        logger.setAdditive(false); /* set to true if root should log too */
        return logger;
    }

    @Override
    public ExecutionStatus configure(final LoggerContext lc) {
        final MessageOnlyEncoder encoder = new MessageOnlyEncoder();
        encoder.setContext(lc);
        encoder.start();

        final RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<ILoggingEvent>();
        appender.setContext(lc);
        appender.setFile("/tmp/logs/collector.log");
        appender.setAppend(true);
        appender.setEncoder(encoder);

        final SizeAndTimeBasedS3RollingPolicy policy = new SizeAndTimeBasedS3RollingPolicy();
        policy.setContext(lc);
        policy.setParent(appender);
        policy.setRegion("ap-northeast-1");
        policy.setProfile("opsdev-sts");
        policy.setBucket("otcmp-tbd-artifact-s3");
        policy.setFileNamePattern("/tmp/logs/collector.%d{yyyyMMdd-HHmm}.%i.log");
        policy.setMaxFileSize(FileSize.valueOf("50MB"));
        policy.setTotalSizeCap(FileSize.valueOf("300MB"));
        policy.setMaxHistory(3);
        policy.start();

        appender.setRollingPolicy(policy);
        appender.start();


        Logger logger = lc.getLogger("DataCollector.default.ingest");
        logger.addAppender(appender);
        logger.setLevel(Level.TRACE);
        // logger.setAdditive(false); /* set to true if root should log too */
        logger.setAdditive(true); /* set to true if root should log too */

        return ExecutionStatus.NEUTRAL;
    }
}

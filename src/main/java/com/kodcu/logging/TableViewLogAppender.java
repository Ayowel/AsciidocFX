package com.kodcu.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.kodcu.service.ThreadService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by usta on 02.06.2015.
 */

public class TableViewLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static TableView<MyLog> logViewer;
    private static ObservableList<MyLog> logList;
    private static List<MyLog> buffer = Collections.synchronizedList(new LinkedList<MyLog>());
    private static AtomicBoolean scheduled = new AtomicBoolean(false);
    private static Label logShortMessage;
    private static ThreadService threadService;
    PatternLayoutEncoder encoder;
    private static Label logShowHider;

    public static void setLogViewer(TableView<MyLog> logViewer) {
        TableViewLogAppender.logViewer = logViewer;
    }

    public static void setLogList(ObservableList<MyLog> logList) {
        TableViewLogAppender.logList = logList;
    }

    public static void setStatusMessage(Label logShortMessage) {
        TableViewLogAppender.logShortMessage = logShortMessage;
    }

    public static void setShowHideLogs(Label logShowHider) {
        TableViewLogAppender.logShowHider = logShowHider;
    }

    public static Label getLogShowHider() {
        return logShowHider;
    }

    @Override
    protected void append(ILoggingEvent event) {

        if (Objects.isNull(logViewer))
            return;

        String message = event.getFormattedMessage();
        String level = event.getLevel().toString();

        if (event.getLevel() == Level.ERROR) {
            logShowHider.getStyleClass().add("red-label");
        }

        final String finalMessage = message;
        Platform.runLater(() -> {
            logShortMessage.setText(finalMessage);
        });

        IThrowableProxy tp = event.getThrowableProxy();
        if (Objects.nonNull(tp) && event.getLevel() == Level.ERROR) {
            String tpMessage = ThrowableProxyUtil.asString(tp);
            message += "\n" + tpMessage;
        }

        MyLog myLog = new MyLog(level, message);
        buffer.add(myLog);

        if (!scheduled.get()) {
            scheduled.set(true);
            threadService.schedule(() -> {
                Platform.runLater(() -> {
                    List<MyLog> clone = new LinkedList<>(buffer);
                    buffer.clear();
                    logList.addAll(clone);
                    scheduled.set(false);
                });
            }, 3, TimeUnit.SECONDS);
        }
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public static void setThreadService(ThreadService threadService) {
        TableViewLogAppender.threadService = threadService;
    }
}

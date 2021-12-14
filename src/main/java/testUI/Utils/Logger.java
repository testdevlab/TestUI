package testUI.Utils;

import io.netty.handler.logging.LogLevel;
import testUI.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingFormatArgumentException;

import static testUI.Configuration.testUILogLevel;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void putLogInfo(String log, Object... arg) {
        if (testUILogLevel != LogLevel.INFO && testUILogLevel != LogLevel.DEBUG)
            return;

        String sf1 = formatString(log, arg);
        log(LogLevel.INFO, sf1);
    }

    public static void putLogDebug(String log, Object ...arg) {
        if (testUILogLevel == LogLevel.DEBUG) {
            String sf1 = formatString(log, arg);
            log(LogLevel.DEBUG, sf1);
        }
    }

    public static void putLogError(String log, Object ...arg) {
        String sf1 = formatString(log, arg);
        log(LogLevel.ERROR, sf1);
    }

    public static void putSoftAssert(String log, Object ...arg) {
        String sf1 = formatString(log, arg);
        log(LogLevel.ERROR, sf1);
        Configuration.testUIErrors.add(sf1);
    }

    public static void putLogWarn(String log, Object ...arg) {
        String sf1 = formatString(log, arg);
        log(LogLevel.WARN, sf1);
    }

    private static void log(LogLevel logLevel, String message) {
        Date date = new Date();
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

        String color;
        switch (logLevel) {
            case ERROR:
                color = ANSI_RED;
                break;
            case WARN:
                color = ANSI_YELLOW;
                break;
            default:
                color = ANSI_PURPLE;
                break;
        }

        System.out.printf(
                color + "[%s] %s: %s %s\n",
                logLevel, jdf.format(date), message, ANSI_RESET
        );
    }

    private static String formatString(String log, Object... arg) {
        try {
            if (arg.length != 0) return String.format(log, arg);
            else return log;
        } catch (MissingFormatArgumentException e) {
            return log + "\n There was an error while formatting the previous message:\n" + e.getMessage();
        }
    }
}

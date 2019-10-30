package testUI.Utils;

import io.netty.handler.logging.LogLevel;
import testUI.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void putLogInfo(String log) {
        if (Configuration.testUILogLevel == LogLevel.INFO ||
                Configuration.testUILogLevel == LogLevel.DEBUG) {
            Date date = new Date();
            SimpleDateFormat jdf = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss.SSS");
            System.out.println(ANSI_GREEN + "[INFO] " + jdf.format(date) + ": " + log + ANSI_RESET);
        }
    }

    public static void putLogDebug(String log) {
        if (Configuration.testUILogLevel == LogLevel.DEBUG) {
            Date date = new Date();
            SimpleDateFormat jdf = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss.SSS");
            System.out.println(ANSI_GREEN + "[DEBUG] " + jdf.format(date) + ": " + log + ANSI_RESET);
        }
    }
}

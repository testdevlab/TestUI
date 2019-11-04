package testUI.Utils;

import io.qameta.allure.Allure;
import testUI.Configuration;

import java.util.ArrayList;
import java.util.List;

import static testUI.Utils.Logger.putLogInfo;

public class Performance {
    private static List<Long> time = new ArrayList<>();

    public static void setTime(long time) {
        Performance.time.add(time);
    }

    public static List<Long> getListOfCommandsTime() {
        return Performance.time;
    }

    public static void logAverageTime() {
        long total = 0;
        for (long t : time) {
            total += t;
        }
        if (time.size() != 0) {
            double average = (double) total / (double) time.size();
            putLogInfo("The command average time is: " + average + " ms");
            if (Configuration.useAllure) {
                Allure.parameter("Average Command Time", average + " ms");
            }
        }
    }
}

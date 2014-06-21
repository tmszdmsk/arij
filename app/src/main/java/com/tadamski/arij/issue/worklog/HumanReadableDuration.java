package com.tadamski.arij.issue.worklog;

/**
 * Created by t.adamski on 7/8/13.
 */
public class HumanReadableDuration {

    public static String create(Long timeSpentInSeconds) {
        long numberOfHours = timeSpentInSeconds / (60 * 60);
        long numberOfMinutes = (timeSpentInSeconds % (60 * 60)) / 60;
        String durationText = "";
        if (numberOfHours != 0) {
            durationText += numberOfHours + "h ";
        }
        durationText += numberOfMinutes + "m";
        return durationText;
    }
}

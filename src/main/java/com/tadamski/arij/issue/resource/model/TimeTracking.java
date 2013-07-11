package com.tadamski.arij.issue.resource.model;

/**
 * Created by t.adamski on 7/11/13.
 */
public class TimeTracking {
    private String originalEstimate = "0";
    private String remainingEstimate = "0";
    private String timeSpent = "0";
    private int originalEstimateSeconds;
    private int remainingEstimateSeconds;
    private int timeSpentSeconds;

    public String getOriginalEstimate() {
        return originalEstimate;
    }

    public String getRemainingEstimate() {
        return remainingEstimate;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public int getOriginalEstimateSeconds() {
        return originalEstimateSeconds;
    }

    public int getRemainingEstimateSeconds() {
        return remainingEstimateSeconds;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }
}

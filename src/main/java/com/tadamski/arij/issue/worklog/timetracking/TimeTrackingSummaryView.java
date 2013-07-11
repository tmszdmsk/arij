package com.tadamski.arij.issue.worklog.timetracking;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.resource.model.TimeTracking;

/**
 * Created by t.adamski on 7/11/13.
 */
@EViewGroup(R.layout.time_tracking_summary_view)
public class TimeTrackingSummaryView extends RelativeLayout {
    private TimeTracking timeTracking;

    @ViewById(R.id.progress)
    ProgressBar progressBar;
    @ViewById(R.id.time_logged)
    TextView timeLogged;
    @ViewById(R.id.time_remaining)
    TextView timeRemaining;

    public TimeTrackingSummaryView(Context context) {
        super(context);
    }

    public TimeTrackingSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTrackingSummaryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void updateView(TimeTracking timeTracking) {
        progressBar.setMax(timeTracking.getRemainingEstimateSeconds() + timeTracking.getTimeSpentSeconds());
        progressBar.setProgress(timeTracking.getTimeSpentSeconds());
        timeLogged.setText(timeTracking.getTimeSpent());
        timeRemaining.setText(timeTracking.getRemainingEstimate());
    }

    public TimeTracking getTimeTracking() {
        return timeTracking;
    }

    public void setTimeTracking(TimeTracking timeTracking) {
        this.timeTracking = timeTracking;
        updateView(this.timeTracking);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.worklog.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.login.LoginInfo;
import com.tadamski.arij.worklog.notification.NewWorklogNotificationBuilder;
import com.tadamski.arij.worklog.repository.NewWorklog;
import com.tadamski.arij.worklog.repository.WorkLogRepository;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author tmszdmsk
 */
public class NewWorklogFragment extends RoboFragment {


    @InjectView(R.id.start_date)
    TextView startDate;
    @InjectView(R.id.duration)
    TextView duration;
    @InjectView(R.id.worklog_comment)
    EditText comment;
    @InjectView(R.id.worklog_log_button)
    Button ok;
    @InjectView(R.id.worklog_cancel_button)
    Button cancel;
    @InjectView(R.id.plus15m)
    Button durationPlus15mButton;
    @InjectView(R.id.minus15m)
    Button durationMinus15mButton;
    @InjectView(R.id.start_plus15m)
    Button startPlus15mButton;
    @InjectView(R.id.start_minus15m)
    Button startMinus15mButton;
    @Inject
    WorkLogRepository workLogRepository;
    String issueKey;
    Date startDateDate;
    Long durationLong;
    LoginInfo loginInfo;

    public void prepare(Issue issue, Date startDate, Long duration, LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
        issueKey = issue.getSummary().getKey();
        startDateDate = startDate;
        durationLong = duration;
        this.startDate.setText(getHumanReadableStartDate(startDate));
        this.duration.setText(getHumanReadableDuration(durationLong));
    }

    private String getHumanReadableStartDate(Date startDate) {
        DateFormat dateFormat;
        Date now = new Date();
        if (now.getYear() == startDate.getYear() && now.getMonth() == startDate.getMonth() && now.getDay() == startDate.getDay()) {
            dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        } else {
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        }
        return dateFormat.format(startDate);
    }

    private String getHumanReadableDuration(long durationInSeconds) {
        long numberOfHours = durationInSeconds / (60 * 60);
        long numberOfMinutes = (durationInSeconds % (60 * 60)) / 60;
        String durationText = "";
        if (numberOfHours != 0) {
            durationText += numberOfHours + "h ";
        }
        durationText += numberOfMinutes + "m";
        return durationText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.worklog_new_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.setEnabled(false);
                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {
                            workLogRepository.addNewWorklogItem(issueKey, new NewWorklog(comment.getText().toString(), startDateDate, durationLong), loginInfo);
                        } catch (Exception ex) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(getActivity(), "worklog updated", Toast.LENGTH_SHORT).show();
                            NewWorklogNotificationBuilder.cancelNotification(getActivity(), issueKey);
                            getActivity().finish();
                        } else {
                            ok.setEnabled(true);
                            Toast.makeText(getActivity(), "error sending worklog", Toast.LENGTH_SHORT).show();
                        }

                    }
                }.execute();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWorklogNotificationBuilder.cancelNotification(getActivity(), issueKey);
                getActivity().finish();
            }
        });
        durationPlus15mButton.setOnClickListener(new DurationModifier(15L * 60));
        durationMinus15mButton.setOnClickListener(new DurationModifier(-15L * 60));
        startPlus15mButton.setOnClickListener(new StartModifier(15L * 60));
        startMinus15mButton.setOnClickListener(new StartModifier(-15L * 60));
    }

    private class DurationModifier implements View.OnClickListener {

        private Long amount;

        public DurationModifier(Long amountInSeconds) {
            this.amount = amountInSeconds;
        }

        @Override
        public void onClick(View v) {
            durationLong = Math.max(durationLong + amount, 60);
            duration.setText(getHumanReadableDuration(durationLong));
        }
    }

    private class StartModifier implements View.OnClickListener {

        private Long amountInSeconds;

        public StartModifier(Long amountInSeconds) {
            this.amountInSeconds = amountInSeconds;
        }

        @Override
        public void onClick(View v) {
            startDateDate = new Date(startDateDate.getTime() + amountInSeconds * 1000);
            startDate.setText(getHumanReadableStartDate(startDateDate));
        }
    }
}

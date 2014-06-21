/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue.worklog.newlog.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.worklog.newlog.notification.NewWorklogNotification;
import com.tadamski.arij.issue.worklog.resource.Worklog;
import com.tadamski.arij.issue.worklog.resource.WorklogService;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author tmszdmsk
 */
@EFragment(R.layout.worklog_new_fragment)
public class NewWorklogFragment extends Fragment {


    @ViewById(R.id.start_date)
    TextView startDate;
    @ViewById(R.id.duration)
    TextView duration;
    @ViewById(R.id.worklog_comment)
    EditText comment;
    @ViewById(R.id.worklog_log_button)
    Button ok;
    @ViewById(R.id.worklog_cancel_button)
    Button cancel;
    @ViewById(R.id.plus15m)
    Button durationPlus15mButton;
    @ViewById(R.id.minus15m)
    Button durationMinus15mButton;
    @ViewById(R.id.start_plus15m)
    Button startPlus15mButton;
    @ViewById(R.id.start_minus15m)
    Button startMinus15mButton;
    @InstanceState
    String issueKey;
    @InstanceState
    Date startDateDate;
    @InstanceState
    Long durationLong;
    @InstanceState
    LoginInfo loginInfo;
    @Bean
    WorklogService worklogService;

    @AfterViews
    public void attachListeners() {
        durationPlus15mButton.setOnClickListener(new DurationModifier(15L * 60));
        durationMinus15mButton.setOnClickListener(new DurationModifier(-15L * 60));
        startPlus15mButton.setOnClickListener(new StartModifier(15L * 60));
        startMinus15mButton.setOnClickListener(new StartModifier(-15L * 60));
    }

    public void prepare(String issueKey, Date startDate, Long duration, LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
        this.issueKey = issueKey;
        this.startDateDate = startDate;
        this.durationLong = duration;
        initDateAndDuration();

    }

    @AfterViews
    void initDateAndDuration() {
        if (startDateDate != null) {
            this.startDate.setText(getHumanReadableStartDate(startDateDate));
        }
        if (durationLong != null) {
            this.duration.setText(getHumanReadableDuration(durationLong));
        }
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

    @Click(R.id.worklog_log_button)
    void logWorkClick() {
        ok.setEnabled(false);
        logWork(loginInfo, issueKey, comment.getText().toString(), startDateDate, durationLong);
    }

    @Background
    void logWork(LoginInfo loginInfo, String issueKey, String comment, Date startDate, long duration) {
        try {
            worklogService.addWorklog(loginInfo, issueKey, new Worklog(comment, startDate, duration));
            onWorkLogged(true);
        } catch (RuntimeException ex) {
            onWorkLogged(false);
        }
    }

    @UiThread
    void onWorkLogged(boolean successful) {
        if (successful) {
            Toast.makeText(getActivity(), "worklog updated", Toast.LENGTH_SHORT).show();
            NewWorklogNotification.cancel(getActivity(), issueKey);
            if (getActivity() instanceof WorkLoggedListener) {
                ((WorkLoggedListener) getActivity()).onWorkLogged();
            }
        } else {
            Toast.makeText(getActivity(), "error sending worklog", Toast.LENGTH_SHORT).show();
            ok.setEnabled(true);
        }
    }

    @Click(R.id.worklog_cancel_button)
    void discardWorklog() {
        NewWorklogNotification.cancel(getActivity(), issueKey);
        if (getActivity() instanceof WorkLoggedListener) {
            ((WorkLoggedListener) getActivity()).onWorkDiscarded();
        }
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

    public interface WorkLoggedListener {
        void onWorkLogged();

        void onWorkDiscarded();
    }
}

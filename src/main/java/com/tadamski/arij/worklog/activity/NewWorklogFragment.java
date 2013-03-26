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

    @InjectView(R.id.issue_key_and_title)
    TextView issueKeyAndTitle;
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
    @Inject
    WorkLogRepository workLogRepository;

    String issueKey;
    Date startDateDate;
    Long durationLong;

    public void prepare(Issue issue, Date startDate, Long duration) {
        issueKey = issue.getSummary().getKey();
        startDateDate = startDate;
        durationLong = duration;
        issueKeyAndTitle.setText(issue.getSummary().getKey() + ": " + issue.getSummary().getSummary());
        this.startDate.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(startDate));
        this.duration.setText(duration + " seconds");
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
                            workLogRepository.addNewWorklogItem(issueKey, new NewWorklog(comment.getText().toString(), startDateDate, durationLong));
                        } catch (Exception ex) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        Toast.makeText(getActivity(), "worklog updated", Toast.LENGTH_SHORT).show();
                        NewWorklogNotificationBuilder.cancelNotification(getActivity(), issueKey);
                        getActivity().finish();
                    }
                }.execute();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWorklogNotificationBuilder.cancelNotification(getActivity(),issueKey);
                getActivity().finish();
            }
        });
    }
}

package com.tadamski.arij.worklog.activity;

import android.os.Bundle;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.login.LoginInfo;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectFragment;

import java.util.Date;

/**
 * @author tmszdmsk
 */
public class NewWorklogActivity extends RoboFragmentActivity {
    public static final String INTENT_ISSUE = "issue";
    public static final String INTENT_START_DATE = "startDate";
    public static final String INTENT_LOGIN_INFO = "loginInfo";
    private static final String TAG = NewWorklogActivity.class.getName();
    @InjectFragment(R.id.worklog_fragment)
    NewWorklogFragment fragment;
    @InjectExtra(INTENT_ISSUE)
    private Issue issue;
    @InjectExtra(INTENT_START_DATE)
    private Date startDate;
    @InjectExtra(INTENT_LOGIN_INFO)
    private LoginInfo loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worklog_new_activity);
        Long seconds = Math.max((new Date().getTime() - startDate.getTime()) / 1000, 60);
        setTitle(issue.getSummary().getKey());
        fragment.prepare(issue, startDate, seconds, loginInfo);
    }
}

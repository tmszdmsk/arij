package com.tadamski.arij.worklog.activity;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.login.LoginInfo;
import java.util.Date;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectFragment;

import javax.inject.Inject;
import roboguice.activity.RoboFragmentActivity;

/**
 *
 * @author tmszdmsk
 */
public class NewWorklogActivity extends RoboFragmentActivity {
    public static final String INTENT_ISSUE = "issue";
    public static final String INTENT_START_DATE = "startDate";
    public static final String INTENT_LOGIN_INFO = "loginInfo";
    

    private static final String TAG = NewWorklogActivity.class.getName();


    @InjectExtra(INTENT_ISSUE)
    private Issue issue;
    @InjectExtra(INTENT_START_DATE)
    private Date startDate;
    @InjectExtra(INTENT_LOGIN_INFO)
    private LoginInfo loginInfo;

    @InjectFragment(R.id.worklog_fragment)
    NewWorklogFragment fragment;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worklog_new_activity);
        Long seconds = Math.max((new Date().getTime()-startDate.getTime())/1000,60);
        fragment.prepare(issue,startDate,seconds);
    }
}

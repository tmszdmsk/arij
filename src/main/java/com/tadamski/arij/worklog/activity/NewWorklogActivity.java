package com.tadamski.arij.worklog.activity;

import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.login.LoginInfo;
import roboguice.activity.RoboFragmentActivity;

import java.util.Date;

/**
 * @author tmszdmsk
 */
@EActivity(R.layout.worklog_new_activity)
public class NewWorklogActivity extends RoboFragmentActivity {
    public static final String INTENT_ISSUE = "issue";
    public static final String INTENT_START_DATE = "startDate";
    public static final String INTENT_LOGIN_INFO = "loginInfo";
    private static final String TAG = NewWorklogActivity.class.getName();
    @FragmentById(R.id.worklog_fragment)
    NewWorklogFragment fragment;
    @Extra(INTENT_ISSUE)
    Issue issue;
    @Extra(INTENT_START_DATE)
    Date startDate;
    @Extra(INTENT_LOGIN_INFO)
    LoginInfo loginInfo;

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStop(this);
    }

    @AfterViews
    protected void prepareFragment() {
        Long seconds = Math.max((new Date().getTime() - startDate.getTime()) / 1000, 60);
        setTitle(issue.getSummary().getKey());
        fragment.prepare(issue, startDate, seconds, loginInfo);
    }
}

package com.tadamski.arij.issue.worklog.activity;

import android.support.v4.app.FragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.Issue;

import java.util.Date;

/**
 * @author tmszdmsk
 */
@EActivity(R.layout.worklog_new_activity)
public class NewWorklogActivity extends FragmentActivity {

    private static final String TAG = NewWorklogActivity.class.getName();
    @FragmentById(R.id.worklog_fragment)
    NewWorklogFragment fragment;
    @Extra
    Issue issue;
    @Extra
    Date startDate;
    @Extra
    LoginInfo loginInfo;
    @NonConfigurationInstance
    boolean loaded = false;

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
        if (!loaded) {
            setTitle(issue.getSummary().getKey());
            fragment.prepare(issue, startDate, seconds, loginInfo);
            loaded = true;
        }
    }
}

package com.tadamski.arij.issue.worklog.newlog.activity;

import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;

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
    String issueKey;
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
            setTitle(issueKey);
            fragment.prepare(issueKey, startDate, seconds, loginInfo);
            loaded = true;
        }
    }
}

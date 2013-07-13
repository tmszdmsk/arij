package com.tadamski.arij.issue.worklog.list;

import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OnActivityResult;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.worklog.newlog.activity.NewWorklogActivity;
import com.tadamski.arij.issue.worklog.resource.WorklogList;
import com.tadamski.arij.util.analytics.Tracker;

/**
 * Created by tmszdmsk on 07.07.13.
 */
@EActivity(R.layout.worklogs)
public class WorklogsActivity extends SherlockFragmentActivity {

    public static final int REQUEST_WORKLOG = 35345;
    public static final int RESULT_UPDATED = 23;
    @Extra
    String issueKey;
    @Extra
    LoginInfo loginInfo;
    @Extra
    WorklogList worklogList;
    @FragmentById(R.id.worklogs_fragment)
    WorklogsFragment worklogsFragment;
    @NonConfigurationInstance
    boolean loaded = false;

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tracker.activityStop(this);
    }

    @OnActivityResult(NewWorklogActivity.REQUEST_CODE_LOG)
    void onWorkLogged(int resultCode) {
        if (resultCode == NewWorklogActivity.RESULT_OK) {
            worklogsFragment.loadWorklogs(loginInfo, issueKey, null); //null for workloglist because we want them to be reloaded
            setResult(RESULT_UPDATED);
        }
    }

    @AfterViews
    void loadWorklogs() {
        getSupportActionBar().setTitle(issueKey);
        getSupportActionBar().setSubtitle(getString(R.string.worklog));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!loaded) {
            worklogsFragment.loadWorklogs(loginInfo, issueKey, worklogList);
            loaded = true;
        }
    }

    @OptionsItem(android.R.id.home)
    void homeSelected() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

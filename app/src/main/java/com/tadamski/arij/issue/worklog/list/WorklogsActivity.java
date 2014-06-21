package com.tadamski.arij.issue.worklog.list;

import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
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

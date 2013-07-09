package com.tadamski.arij.issue.worklog.list;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
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
import com.tadamski.arij.issue.worklog.newlog.activity.NewWorklogActivity_;
import com.tadamski.arij.issue.worklog.resource.WorklogList;

/**
 * Created by tmszdmsk on 07.07.13.
 */
@EActivity(R.layout.worklogs)
public class WorklogsActivity extends SherlockFragmentActivity {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.worklog_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.menu_item_log_work)
    void onLogWorkClicked() {
        NewWorklogActivity_.intent(this).issueKey(issueKey).loginInfo(loginInfo).startForResult(NewWorklogActivity.REQUEST_CODE_LOG);
    }

    @OnActivityResult(NewWorklogActivity.REQUEST_CODE_LOG)
    void onWorkLogged(int resultCode) {
        if (resultCode == NewWorklogActivity.RESULT_OK)
            worklogsFragment.loadWorklogs(loginInfo, issueKey, null); //null for workloglist because we want them to be reloaded
    }

    @AfterViews
    void loadWorklogs() {
        if (!loaded) {
            getSupportActionBar().setTitle(issueKey);
            getSupportActionBar().setSubtitle(getString(R.string.worklog));
            worklogsFragment.loadWorklogs(loginInfo, issueKey, worklogList);
            loaded = true;
        }
    }
}

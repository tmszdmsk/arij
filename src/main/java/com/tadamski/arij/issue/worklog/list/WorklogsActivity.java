package com.tadamski.arij.issue.worklog.list;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;

/**
 * Created by tmszdmsk on 07.07.13.
 */
@EActivity(R.layout.worklogs)
public class WorklogsActivity extends SherlockFragmentActivity {

    @Extra
    String issueKey;
    @Extra
    LoginInfo loginInfo;
    @FragmentById(R.id.worklogs_fragment)
    WorklogsFragment worklogsFragment;
    @NonConfigurationInstance
    boolean loaded = false;


    @AfterViews
    void loadComments() {
        if (!loaded) {
            getSupportActionBar().setTitle(issueKey);
            worklogsFragment.loadWorklogs(loginInfo, issueKey);
            loaded = true;
        }
    }
}

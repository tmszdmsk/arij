package com.tadamski.arij.issue.activity.list;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.dao.IssueDAO;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends SherlockFragmentActivity {

    private final String TAG = IssueListActivity.class.getName();
    @FragmentById(R.id.fragment)
    IssueListFragment fragment;
    @NonConfigurationInstance
    boolean loaded;
    @Bean
    IssueDAO issueDao;
    @Extra
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
    void initFragment() {
        if (!loaded) {
            fragment.executeJql("assignee=currentUser()", loginInfo);
            loaded = true;
        }
    }

}

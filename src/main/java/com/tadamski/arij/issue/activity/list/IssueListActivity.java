package com.tadamski.arij.issue.activity.list;

import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.CredentialsService;
import com.tadamski.arij.issue.dao.IssueDAO;
import roboguice.activity.RoboFragmentActivity;

import javax.inject.Inject;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends RoboFragmentActivity {

    private final String TAG = IssueListActivity.class.getName();
    @FragmentById(R.id.fragment)
    IssueListFragment fragment;
    @NonConfigurationInstance
    boolean loaded;
    @Inject
    private IssueDAO issueDao;
    @Inject
    private CredentialsService service;

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
            fragment.executeJql("assignee=currentUser()", service.getActive());
            loaded = true;
        }
    }

}

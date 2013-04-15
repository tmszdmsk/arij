package com.tadamski.arij.issue.activity.list;

import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.CredentialsService;
import com.tadamski.arij.issue.dao.IssueDAO;
import roboguice.activity.RoboFragmentActivity;

import javax.inject.Inject;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends RoboFragmentActivity {

    private final String TAG = IssueListActivity.class.getName();
    @Inject
    private IssueDAO issueDao;
    @Inject
    private CredentialsService service;
    @FragmentById(R.id.fragment)
    IssueListFragment fragment;

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
        fragment.executeJql("assignee=currentUser()", service.getActive());
    }

}

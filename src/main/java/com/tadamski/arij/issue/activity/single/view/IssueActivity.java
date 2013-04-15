package com.tadamski.arij.issue.activity.single.view;

import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.dao.Issue;
import roboguice.activity.RoboFragmentActivity;

@EActivity(R.layout.issue)
public class IssueActivity extends RoboFragmentActivity implements IssueFragment.IssueLoadedListener {

    private static final String TAG = IssueActivity.class.getName();
    @Extra
    String issueKey;
    @Extra
    LoginInfo account;
    @FragmentById(R.id.issue_fragment)
    IssueFragment issueFragment;

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
    void init() {
        getActionBar().setTitle(issueKey);
        issueFragment.loadIssue(issueKey, account);
    }

    @Override
    public void issueLoaded(Issue issue) {
        getActionBar().setSubtitle(issue.getSummary().getSummary());
    }
}

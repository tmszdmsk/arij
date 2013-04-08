package com.tadamski.arij.issue.activity;

import android.os.Bundle;
import com.google.analytics.tracking.android.EasyTracker;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.dao.Issue;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectFragment;

public class IssueActivity extends RoboFragmentActivity implements IssueFragment.IssueLoadedListener {

    public static final String INTENT_EXTRA_ISSUE_KEY = "issueKey";
    private static final String TAG = IssueActivity.class.getName();
    @InjectExtra(INTENT_EXTRA_ISSUE_KEY)
    private String issueKey;
    @InjectFragment(R.id.issue_fragment)
    private IssueFragment issueFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue);
        getActionBar().setTitle(issueKey);
        issueFragment.loadIssue(issueKey);
    }

    @Override
    public void issueLoaded(Issue issue) {
    }
}

package com.tadamski.arij.activity.issue;

import android.os.Bundle;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectFragment;

public class IssueActivity extends RoboFragmentActivity implements IssueFragment.IssueLoadedListener{

    private static final String TAG = IssueActivity.class.getName();
    public static final String INTENT_EXTRA_ISSUE_KEY = "issueKey";
    @InjectExtra(INTENT_EXTRA_ISSUE_KEY)
    private String issueKey;
    @InjectFragment(R.id.issue_fragment)
    private IssueFragment issueFragment;

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

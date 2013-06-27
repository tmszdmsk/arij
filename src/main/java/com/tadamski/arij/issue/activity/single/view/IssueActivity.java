package com.tadamski.arij.issue.activity.single.view;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.comments.CommentsFragment;
import com.tadamski.arij.issue.dao.Issue;

@EActivity(R.layout.issue)
public class IssueActivity extends SherlockFragmentActivity implements IssueFragment.IssueLoadedListener {

    private static final String TAG = IssueActivity.class.getName();
    @Extra
    String issueKey;
    @Extra
    LoginInfo account;
    @FragmentById(R.id.issue_fragment)
    IssueFragment issueFragment;
    @FragmentById(R.id.comments_fragment)
    CommentsFragment commentsFragment;
    @NonConfigurationInstance
    boolean loaded = false;

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
        getSupportActionBar().setTitle(issueKey);
        if (!loaded) {
            issueFragment.loadIssue(issueKey, account);
            commentsFragment.loadComments(account, issueKey);
            loaded = true;
        }
    }

    @Override
    public void issueLoaded(Issue issue) {
        getSupportActionBar().setSubtitle(issue.getSummary().getSummary());
    }
}

package com.tadamski.arij.issue.single.activity.single.view;

import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OnActivityResult;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.activity.CommentsActivity;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.worklog.list.WorklogsActivity;
import com.tadamski.arij.util.analytics.Tracker;

@EActivity(R.layout.issue)
public class IssueActivity extends SherlockFragmentActivity implements IssueFragment.IssueFragmentListener {

    private static final String TAG = IssueActivity.class.getName();
    @Extra
    String issueKey;
    @Extra
    LoginInfo loginInfo;
    @FragmentById(R.id.issue_fragment)
    IssueFragment issueFragment;
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

    @AfterViews
    void init() {
        getSupportActionBar().setTitle(issueKey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!loaded) {
            issueFragment.loadIssue(issueKey, loginInfo);
            loaded = true;
        }
    }

    @OptionsItem(android.R.id.home)
    void homeSelected() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnActivityResult(CommentsActivity.REQUEST_SHOW_COMMENTS)
    void onCommentsUpdated(int result) {
        if (result == CommentsActivity.RESULT_UPDATE) {
            issueFragment.loadIssue(issueKey, loginInfo);
        }
    }

    @OnActivityResult(WorklogsActivity.REQUEST_WORKLOG)
    void onWorklogUpdated(int result) {
        if (result == WorklogsActivity.RESULT_UPDATED) {
            issueFragment.loadIssue(issueKey, loginInfo);
        }
    }

    @Override
    public void issueLoaded(Issue issue) {
        getSupportActionBar().setSubtitle(issue.getSummary());
    }
}

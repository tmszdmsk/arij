package com.tadamski.arij.issue.single.activity.single.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.WindowManager;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.activity.CommentsActivity;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.worklog.list.WorklogsActivity;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;

@EActivity(R.layout.issue)
public class IssueActivity extends FragmentActivity implements IssueFragment.IssueFragmentListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD) > 0) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
    }

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
        getActionBar().setTitle(issueKey);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        getActionBar().setSubtitle(issue.getSummary());
    }
}

package com.tadamski.arij.issue.single.activity.single.view;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.activity.CommentsActivity_;
import com.tadamski.arij.issue.resource.Issue;
import com.tadamski.arij.issue.worklog.list.WorklogsActivity_;

@EActivity(R.layout.issue)
public class IssueActivity extends SherlockFragmentActivity implements IssueFragment.IssueLoadedListener {

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
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.issue_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.menu_item_comments)
    void onCommentsClicked() {
        CommentsActivity_.intent(this).issueKey(issueKey).loginInfo(loginInfo).start();
    }

    @OptionsItem(R.id.menu_item_worklog)
    void onWorklogClicked() {
        WorklogsActivity_.intent(this).issueKey(issueKey).loginInfo(loginInfo).start();
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
            issueFragment.loadIssue(issueKey, loginInfo);
            loaded = true;
        }
    }

    @Override
    public void issueLoaded(Issue issue) {
        getSupportActionBar().setSubtitle(issue.getSummary().getSummary());
    }
}

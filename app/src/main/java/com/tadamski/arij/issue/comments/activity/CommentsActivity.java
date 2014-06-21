package com.tadamski.arij.issue.comments.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.resource.CommentsList;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;

/**
 * Created by tmszdmsk on 07.07.13.
 */
@EActivity(R.layout.comments)
public class CommentsActivity extends FragmentActivity implements CommentsFragment.CommentsFragmentListener {

    public static final int REQUEST_SHOW_COMMENTS = 1235;
    public static final int RESULT_UPDATE = 12;
    @Extra
    String issueKey;
    @Extra
    LoginInfo loginInfo;
    @Extra
    CommentsList commentsList;
    @FragmentById(R.id.comments_fragment)
    CommentsFragment commentsFragment;
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
    void loadComments() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(issueKey);
        getActionBar().setSubtitle(getString(R.string.comments));
        if (!loaded) {
            commentsFragment.loadComments(loginInfo, issueKey, commentsList);
            loaded = true;
        }
    }

    @Override
    public void onNewCommentSent() {
        setResult(RESULT_UPDATE);
    }

    @OptionsItem(android.R.id.home)
    void homeSelected() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

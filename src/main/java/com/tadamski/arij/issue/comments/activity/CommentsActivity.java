package com.tadamski.arij.issue.comments.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.resource.CommentsList;

/**
 * Created by tmszdmsk on 07.07.13.
 */
@EActivity(R.layout.comments)
public class CommentsActivity extends SherlockFragmentActivity implements CommentsFragment.CommentsFragmentListener {

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


    @AfterViews
    void loadComments() {
        getSupportActionBar().setTitle(issueKey);
        getSupportActionBar().setSubtitle(getString(R.string.comments));
        if (!loaded) {
            commentsFragment.loadComments(loginInfo, issueKey, commentsList);
            loaded = true;
        }
    }

    @Override
    public void onNewCommentSent() {
        setResult(RESULT_UPDATE);
    }
}

package com.tadamski.arij.issue.comments.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;

/**
 * Created by tmszdmsk on 07.07.13.
 */
@EActivity(R.layout.comments)
public class CommentsActivity extends SherlockFragmentActivity {

    @Extra
    String issueKey;
    @Extra
    LoginInfo loginInfo;
    @FragmentById(R.id.comments_fragment)
    CommentsFragment commentsFragment;

    @AfterViews
    void loadComments() {
        getSupportActionBar().setTitle(issueKey);
        commentsFragment.loadComments(loginInfo, issueKey);
    }
}

package com.tadamski.arij.issue.comments.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.*;
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
    @NonConfigurationInstance
    boolean loaded = false;


    @AfterViews
    void loadComments() {
        if (!loaded) {
            getSupportActionBar().setTitle(issueKey);
            commentsFragment.loadComments(loginInfo, issueKey);
            loaded = true;
        }
    }
}

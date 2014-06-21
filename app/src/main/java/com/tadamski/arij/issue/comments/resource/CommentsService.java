package com.tadamski.arij.issue.comments.resource;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:14
 * To change this template use File | Settings | File Templates.
 */
@EBean
public class CommentsService {

    public List<Comment> getComments(LoginInfo loginInfo, String issueKey) {
        CommentsResource commentsResource = getResource(loginInfo);
        return commentsResource.getComments(issueKey).getComments();
    }

    public Comment addComment(LoginInfo loginInfo, String issueKey, Comment comment) {
        CommentsResource commentsResource = getResource(loginInfo);
        return commentsResource.addComment(issueKey, comment);
    }

    CommentsResource getResource(LoginInfo loginInfo) {
        return RestAdapterProvider.get(CommentsResource.class, loginInfo);
    }
}

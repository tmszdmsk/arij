package com.tadamski.arij.comments;

import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.comments.resource.Comment;
import com.tadamski.arij.comments.resource.CommentsResource;
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

    List<Comment> getComments(LoginInfo loginInfo, String issueKey) {
        CommentsResource commentsResource = getResource(loginInfo);
        return commentsResource.getComments(issueKey).getComments();
    }

    Comment addComment(LoginInfo loginInfo, String issueKey, Comment comment) {
        CommentsResource commentsResource = getResource(loginInfo);
        return commentsResource.addComment(issueKey, comment);
    }

    CommentsResource getResource(LoginInfo loginInfo) {
        return RestAdapterProvider.get(CommentsResource.class, loginInfo);
    }
}

package com.tadamski.arij.comments;

import android.util.Base64;
import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.comments.resource.Comment;
import com.tadamski.arij.comments.resource.CommentsResource;
import retrofit.RestAdapter;

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
        return commentsResource.getComments(issueKey, authorizationHeader(loginInfo)).getComments();
    }

    Comment addComment(LoginInfo loginInfo, String issueKey, Comment comment) {
        CommentsResource commentsResource = getResource(loginInfo);
        return commentsResource.addComment(issueKey, comment, authorizationHeader(loginInfo));
    }

    CommentsResource getResource(LoginInfo loginInfo) {
        RestAdapter restAdapter = new RestAdapter.Builder().setServer(loginInfo.getBaseURL()).setDebug(true).build();
        CommentsResource commentsResource = restAdapter.create(CommentsResource.class);
        return commentsResource;
    }

    private String authorizationHeader(LoginInfo credentials) {
        byte[] toEncode = (credentials.getUsername() + ":" + new String(credentials.getPassword())).getBytes();
        return "Basic " + Base64.encodeToString(toEncode, Base64.NO_WRAP);
    }
}

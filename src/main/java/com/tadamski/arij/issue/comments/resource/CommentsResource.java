package com.tadamski.arij.issue.comments.resource;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:03
 * To change this template use File | Settings | File Templates.
 */

public interface CommentsResource {

    @GET("/rest/api/2/issue/{issueKey}/comment")
    CommentsList getComments(@Path("issueKey") String issueKey);

    @POST("/rest/api/2/issue/{issueKey}/comment")
    Comment addComment(@Path("issueKey") String issueKey, @Body Comment comment);

}

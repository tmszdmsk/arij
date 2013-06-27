package com.tadamski.arij.comments.resource;

import retrofit.http.*;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:03
 * To change this template use File | Settings | File Templates.
 */

public interface CommentsResource {

    @GET("/rest/api/2/issue/{issueKey}/comment")
    CommentsList getComments(@Path("issueKey") String issueKey, @Header("Authorization") String credentials);

    @POST("/rest/api/2/issue/{issueKey}/comment")
    Comment addComment(@Path("issueKey") String issueKey, @Body Comment comment, @Header("Authorization") String credentials);

}

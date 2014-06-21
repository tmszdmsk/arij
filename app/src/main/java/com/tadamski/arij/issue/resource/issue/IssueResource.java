package com.tadamski.arij.issue.resource.issue;

import com.tadamski.arij.issue.resource.model.Issue;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public interface IssueResource {

    @GET("/rest/api/2/issue/{issueKey}")
    public Issue getIssue(@Path("issueKey") String issueKey);

    @PUT("/rest/api/2/issue/{issueKey}")
    public Issue updateIssue(@Path("issueKey") String issueKey, @Body IssueUpdate issue);
}

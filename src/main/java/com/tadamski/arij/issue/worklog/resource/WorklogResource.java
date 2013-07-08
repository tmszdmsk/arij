package com.tadamski.arij.issue.worklog.resource;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by t.adamski on 7/8/13.
 */
public interface WorklogResource {

    @GET("/rest/api/2/issue/{issueKey}/worklog")
    WorklogList getWorklogs(@Path("issueKey") String issueKey);

    @POST("/rest/api/2/issue/{issueKey}/worklog")
    Worklog addWorklog(@Path("issueKey") String issueKey, @Body Worklog worklog);

}

package com.tadamski.arij.issue.worklog.repository;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by tmszdmsk on 02.07.13.
 */
public interface WorklogResource {

    @POST("/rest/api/latest/issue/{issueKey}/worklog")
    public Response addWorklog(@Body NewWorklog worklog, @Path("issueKey") String issueKey);

}

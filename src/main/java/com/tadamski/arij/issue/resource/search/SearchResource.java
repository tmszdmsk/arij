package com.tadamski.arij.issue.resource.search;

import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public interface SearchResource {

    @POST("/rest/api/2/search")
    public IssuesResultList searchForIssues(@Body SearchParams searchParams);
}

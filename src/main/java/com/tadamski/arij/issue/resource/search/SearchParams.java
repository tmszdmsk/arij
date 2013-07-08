package com.tadamski.arij.issue.resource.search;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class SearchParams {
    private String jql;
    private long startAt;
    private long maxResults;

    public SearchParams(String jql, long startAt, long maxResults) {
        this.jql = jql;
        this.startAt = startAt;
        this.maxResults = maxResults;
    }
}

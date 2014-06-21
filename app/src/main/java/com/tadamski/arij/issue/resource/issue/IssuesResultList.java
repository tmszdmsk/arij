package com.tadamski.arij.issue.resource.issue;


import com.tadamski.arij.issue.resource.model.Issue;

import java.util.List;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class IssuesResultList {
    private long total;
    private long startAt;
    private List<Issue> issues;

    public IssuesResultList(long total, long startAt, List<Issue> issues) {
        this.total = total;
        this.startAt = startAt;
        this.issues = issues;
    }

    public long getTotal() {
        return total;
    }

    public long getStartAt() {
        return startAt;
    }

    public List<Issue> getIssues() {
        return issues;
    }
}

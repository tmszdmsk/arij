package com.tadamski.arij.issue.worklog.resource;

import com.tadamski.arij.issue.resource.Issue;

import java.util.Date;

/**
 * Created by t.adamski on 7/8/13.
 */
public class Worklog {

    private String comment;
    private Date started;
    private Long timeSpentSeconds;
    private Issue.User author;
    private Date created;
    private String timeSpent;
    private String id;

    public Worklog(String comment, Date started, Long timeSpentSeconds) {
        this.comment = comment;
        this.started = started;
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public String getComment() {
        return comment;
    }

    public Date getStarted() {
        return started;
    }

    public Long getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public Issue.User getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public String getId() {
        return id;
    }
}

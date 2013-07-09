package com.tadamski.arij.issue.worklog.resource;

import com.tadamski.arij.issue.resource.model.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by t.adamski on 7/8/13.
 */
public class Worklog implements Serializable {

    private String comment;
    private Date started;
    private Long timeSpentSeconds;
    private User author;
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

    public User getAuthor() {
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

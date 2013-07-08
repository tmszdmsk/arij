package com.tadamski.arij.issue.resource.model;

import java.util.Date;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class Issue {
    private String self;
    private Long id;
    private String key;
    private Fields fields;

    public String getSelf() {
        return self;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return fields.summary;
    }

    public String getDescription() {
        return fields.description;
    }

    public Priority getPriority() {
        return fields.priority;
    }

    public Status getStatus() {
        return fields.status;
    }

    public Type getIssueType() {
        return fields.issuetype;
    }

    public User getReporter() {
        return fields.reporter;
    }

    public User getAssignee() {
        return fields.assignee;
    }

    public Date getCreated() {
        return fields.created;
    }

    public Date getUpdated() {
        return fields.updated;
    }

    public Project getProject() {
        return fields.project;
    }

    public Resolution getResolution() {
        return fields.resolution;
    }

    public Date getResolutionDate() {
        return fields.resolutiondate;
    }

    private class Fields {
        String summary;
        String description;
        Priority priority;
        Status status;
        Type issuetype;
        User reporter;
        User assignee;
        Date created;
        Date updated;
        Project project;
        Resolution resolution;
        Date resolutiondate;
    }

}

package com.tadamski.arij.issue.resource.model;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class Type {
    private String self;
    private Long id;
    private String description;
    private String iconUrl;
    private String name;
    private boolean subtask;

    public String getSelf() {
        return self;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getName() {
        return name;
    }

    public boolean isSubtask() {
        return subtask;
    }
}

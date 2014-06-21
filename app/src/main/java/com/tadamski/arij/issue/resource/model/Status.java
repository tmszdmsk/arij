package com.tadamski.arij.issue.resource.model;

import java.io.Serializable;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class Status implements Serializable {
    private String self;
    private Long id;
    private String name;
    private String iconUrl;
    private String description;

    public String getSelf() {
        return self;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getDescription() {
        return description;
    }
}

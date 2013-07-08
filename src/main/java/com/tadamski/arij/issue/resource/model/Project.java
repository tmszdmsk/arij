package com.tadamski.arij.issue.resource.model;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class Project {
    private String self;
    private Long id;
    private String key;
    private String name;
    //avatarUrls-> 16x16, 24x24, 32x32, 48x48;


    public String getSelf() {
        return self;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}

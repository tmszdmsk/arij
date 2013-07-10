package com.tadamski.arij.issue.resource.model;

import java.io.Serializable;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class User implements Serializable {
    private String self;
    private String name;
    private String emailAddress;
    private String displayName;
    private boolean active;
    //avatarUrls-> 16x16, 24x24, 32x32, 48x48;


    public User(String name) {
        this.name = name;
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return active;
    }
}

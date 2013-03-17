/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.activity.issue.properties.model;

/**
 *
 * @author tmszdmsk
 */
public class IssueProperty {

    private String id;
    private String name;
    private String value;
    private String iconUrl;

    public IssueProperty(String id, String name, String value, String iconUrl) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}

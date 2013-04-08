/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue.activity.properties.model;

import java.util.Collections;
import java.util.List;

/**
 * @author tmszdmsk
 */
public class IssuePropertyGroup {
    private List<IssueProperty> properties;
    private String name;

    public IssuePropertyGroup(List<IssueProperty> properties, String name) {
        this.properties = properties;
        this.name = name;
    }

    public List<IssueProperty> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public String getName() {
        return name;
    }

}

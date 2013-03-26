/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 *
 * @author tmszdmsk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project implements Serializable {

    @JsonProperty("key")
    private String key;
    @JsonProperty("name")
    private String name;

    @JsonCreator
    public Project(@JsonProperty("key") String key, @JsonProperty("name") String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Project{" + "key=" + key + ", name=" + name + '}';
    }
}

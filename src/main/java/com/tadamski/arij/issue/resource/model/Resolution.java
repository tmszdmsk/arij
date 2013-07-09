package com.tadamski.arij.issue.resource.model;

import java.io.Serializable;

/**
 * Created by tmszdmsk on 08.07.13.
 */
public class Resolution implements Serializable {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

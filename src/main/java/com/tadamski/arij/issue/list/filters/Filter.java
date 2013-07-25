package com.tadamski.arij.issue.list.filters;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 26.06.13
 * Time: 19:12
 */
public class Filter implements Serializable{

    public String id;
    public String name;
    public String jql;

    public Filter(String id, String name, String jql) {
        this.id = id;
        this.name = name;
        this.jql = jql;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (!id.equals(filter.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

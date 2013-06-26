package com.tadamski.arij.issue.activity.list.filters;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 26.06.13
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class Filter {

    public String name;
    public String jql;

    public Filter(String name, String jql) {
        this.name = name;
        this.jql = jql;
    }
}

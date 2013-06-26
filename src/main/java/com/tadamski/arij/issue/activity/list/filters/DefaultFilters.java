package com.tadamski.arij.issue.activity.list.filters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 26.06.13
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class DefaultFilters {

    public List<Filter> getFilterList() {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("Assigned to me", "assignee = currentUser() ORDER BY updatedDate DESC"));
        filters.add(new Filter("Reported by me", "reporter = currentUser() ORDER BY createdDate DESC"));
        filters.add(new Filter("All issues", "ORDER BY createdDate DESC"));
        return filters;
    }
}

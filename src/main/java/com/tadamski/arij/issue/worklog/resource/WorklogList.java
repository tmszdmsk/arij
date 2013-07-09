package com.tadamski.arij.issue.worklog.resource;

import java.io.Serializable;
import java.util.List;

/**
 * Created by t.adamski on 7/8/13.
 */
public class WorklogList implements Serializable {
    List<Worklog> worklogs;

    public List<Worklog> getWorklogs() {
        return worklogs;
    }
}

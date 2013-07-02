package com.tadamski.arij.worklog.repository;

import java.util.Date;

/**
 *
 * @author tmszdmsk
 */
public class NewWorklog {
    private String comment;
    private Date started;
    private Long timeSpentSeconds;

    public NewWorklog(String comment, Date started, Long timeSpentSeconds) {
        this.comment = comment;
        this.started = started;
        this.timeSpentSeconds = timeSpentSeconds;
    }
    
}

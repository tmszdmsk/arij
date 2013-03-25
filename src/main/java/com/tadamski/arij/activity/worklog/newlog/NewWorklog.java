package com.tadamski.arij.activity.worklog.newlog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author tmszdmsk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewWorklog {
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("started")
    private Date startDate;
    @JsonProperty("timeSpentSeconds")
    private Long timeSpentSeconds;

    public NewWorklog(String comment, Date startDate, Long timeSpentSeconds) {
        this.comment = comment;
        this.startDate = startDate;
        this.timeSpentSeconds = timeSpentSeconds;
    }
    
}

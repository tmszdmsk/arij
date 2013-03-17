package com.tadamski.arij.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.Date;

/**
 *
 * @author tmszdmsk
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    @JsonUnwrapped
    private Summary summary;

    public Summary getSummary() {
        return summary;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {

        @JsonProperty("id")
        private Long id;
        @JsonProperty("key")
        private String key;
        @JsonProperty("fields")
        private Fields fields;

        public String getDescription() {
            return fields.getDescription();
        }

        public String getKey() {
            return key;
        }

        public String getSummary() {
            return fields.getSummary();
        }

        public Date getCreated() {
            return fields.getCreated();
        }

        public Date getUpdated() {
            return fields.getUpdated();
        }

        public Date getResolutionDate() {
            return fields.getResolutionDate();
        }
        
        public Long getId() {
            return id;
        }

        public Type getType() {
            return fields.getType();
        }

        public Project getProject() {
            return fields.getProject();
        }

        public Priority getPriority() {
            return fields.getPriority();
        }

        public Status getStatus() {
            return fields.getStatus();
        }

        public Resolution getResolution() {
            return fields.getResolution();
        }

        public User getAssignee() {
            return fields.getAssignee();
        }

        public User getReporter() {
            return fields.getReporter();
        }

        @Override
        public String toString() {
            return "Summary{" + "id=" + id + ", key=" + key + ", fields=" + fields + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {

        @JsonProperty
        private long id;
        @JsonProperty
        private String name;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Type{" + "id=" + id + ", name=" + name + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Priority {

        @JsonProperty
        private long id;
        @JsonProperty
        private String name;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Type{" + "id=" + id + ", name=" + name + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {

        @JsonProperty
        private long id;
        @JsonProperty
        private String name;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Status{" + "id=" + id + ", name=" + name + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Resolution {

        @JsonProperty
        private long id;
        @JsonProperty
        private String name;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Resolution{" + "id=" + id + ", name=" + name + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {

        @JsonProperty
        private long id;
        @JsonProperty
        private String name;
        @JsonProperty
        private String displayName;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return "User{" + "id=" + id + ", name=" + name + ", displayName=" + displayName + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fields {

        @JsonProperty
        private String description;
        @JsonProperty
        private String summary;
        @JsonProperty("created")
        private Date created;
        @JsonProperty("updated")
        private Date updated;
        @JsonProperty("resolutiondate")
        private Date resolutionDate;
        @JsonProperty("issuetype")
        private Type type;
        @JsonProperty("project")
        private Project project;
        @JsonProperty("priority")
        private Priority priority;
        @JsonProperty("status")
        private Status status;
        @JsonProperty("resolution")
        private Resolution resolution;
        @JsonProperty("assignee")
        private User assignee;
        @JsonProperty("reporter")
        private User reporter;

        public String getDescription() {
            return description;
        }

        public String getSummary() {
            return summary;
        }

        public Date getCreated() {
            return created;
        }

        public Date getUpdated() {
            return updated;
        }

        public Date getResolutionDate() {
            return resolutionDate;
        }

        public Type getType() {
            return type;
        }

        public Project getProject() {
            return project;
        }

        public Priority getPriority() {
            return priority;
        }

        public Status getStatus() {
            return status;
        }

        public Resolution getResolution() {
            return resolution;
        }

        public User getAssignee() {
            return assignee;
        }

        public User getReporter() {
            return reporter;
        }

        @Override
        public String toString() {
            return "Fields{" + "description=" + description + ", summary=" + summary + '}';
        }
    }
}

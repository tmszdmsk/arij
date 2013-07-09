package com.tadamski.arij.issue.comments.resource;

import com.tadamski.arij.issue.resource.model.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class Comment implements Serializable {
    private String body;
    private User author;
    private Date created;

    public Comment(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreatedDate() {
        return created;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "body='" + body + '\'' +
                ", author=" + author +
                ", created=" + created +
                '}';
    }
}

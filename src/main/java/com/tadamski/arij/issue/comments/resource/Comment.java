package com.tadamski.arij.issue.comments.resource;

import com.tadamski.arij.issue.resource.Issue;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class Comment {
    private String body;
    private Issue.User author;

    public Comment(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Issue.User getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return body;
    }
}

package com.tadamski.arij.issue.comments.resource;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 22:13
 * To change this template use File | Settings | File Templates.
 */
public class CommentsList implements Serializable {
    List<Comment> comments;
    long startAt;
    long maxResults;
    long total;

    public List<Comment> getComments() {
        return comments;
    }

    public long getStartAt() {
        return startAt;
    }

    public long getMaxResults() {
        return maxResults;
    }

    public long getTotal() {
        return total;
    }
}

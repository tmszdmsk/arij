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

    public List<Comment> getComments() {
        return comments;
    }
}

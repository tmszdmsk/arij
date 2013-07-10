package com.tadamski.arij.issue.resource.model.updates;

import com.tadamski.arij.issue.resource.model.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by t.adamski on 7/10/13.
 */
public class ChangeAssigneeUpdate implements IssueUpdateParams {
    private List<SetOperation> assignee;

    public ChangeAssigneeUpdate(User assignee) {
        this.assignee = Collections.singletonList(new SetOperation(assignee));
    }

    private static class SetOperation {
        Object set;

        private SetOperation(Object set) {
            this.set = set;
        }
    }
}

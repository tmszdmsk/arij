package com.tadamski.arij.issue.dao;

import com.google.common.base.Preconditions;
import com.tadamski.arij.util.Jack;
import com.tadamski.arij.util.rest.CommandResult;
import com.tadamski.arij.util.rest.RESTRunner;
import com.tadamski.arij.util.rest.command.GETCommand;
import com.tadamski.arij.util.rest.command.POSTCommand;
import com.tadamski.arij.util.rest.exceptions.CommunicationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.ContextSingleton;

import javax.inject.Inject;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tmszdmsk
 */
@ContextSingleton
public class IssueDAO {

    private static final String SEARCH_PATH = "rest/api/latest/search";
    private static final String ISSUE_PATH_PATTERN = "rest/api/latest/issue/{0}";
    @Inject
    private RESTRunner restRunner;

    public List<Issue.Summary> getTasksAssignedToLoggedInUser() {
        try {
            JSONObject query = new JSONObject().put("jql", "assignee=currentUser()");
            POSTCommand cmd = new POSTCommand(query.toString(), SEARCH_PATH);
            CommandResult result = restRunner.run(cmd);
            JSONObject resultObject = new JSONObject(result.getResult());
            final JSONArray issuesArray = resultObject.getJSONArray("issues");
            List<Issue.Summary> issueList = new LinkedList<Issue.Summary>();
            for (int i = 0; i < issuesArray.length(); i++) {
                JSONObject issue = issuesArray.getJSONObject(i);
                Issue.Summary readValue = Jack.son().readValue(issue.toString(), Issue.Summary.class);
                issueList.add(readValue);
            }
            return issueList;
        } catch (IOException ex) {
            throw new CommunicationException(ex);
        } catch (JSONException ex) {
            throw new CommunicationException(ex);
        }
    }

    public Issue getIssueWithKey(String key) {
        try {
            Preconditions.checkNotNull(key);
            String url = MessageFormat.format(ISSUE_PATH_PATTERN, key.toString());
            GETCommand cmd = new GETCommand(url);
            CommandResult result = restRunner.run(cmd);
            return Jack.son().readValue(result.getResult(), Issue.class);
        } catch (IOException ex) {
            throw new CommunicationException(ex);
        }
    }
}

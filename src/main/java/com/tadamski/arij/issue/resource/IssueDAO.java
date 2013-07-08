package com.tadamski.arij.issue.resource;

import com.google.common.base.Preconditions;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.Jack;
import com.tadamski.arij.util.rest.CommandResult;
import com.tadamski.arij.util.rest.RESTRunner;
import com.tadamski.arij.util.rest.command.GETCommand;
import com.tadamski.arij.util.rest.command.POSTCommand;
import com.tadamski.arij.util.rest.exceptions.CommunicationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tmszdmsk
 */
@EBean
public class IssueDAO {

    private static final String SEARCH_PATH = "rest/api/latest/search";
    private static final String ISSUE_PATH_PATTERN = "rest/api/latest/issue/{0}";
    @Bean
    RESTRunner restRunner;

    public ResultList executeJql(String jql, Long startAt, Long maxResults, LoginInfo loginInfo) {
        try {
            JSONObject query = new JSONObject()
                    .put("jql", jql)
                    .put("startAt", startAt)
                    .put("maxResults", maxResults);
            POSTCommand cmd = new POSTCommand(query.toString(), SEARCH_PATH);
            CommandResult result = restRunner.run(cmd, loginInfo);
            JSONObject resultObject = new JSONObject(result.getResult());
            final JSONArray issuesArray = resultObject.getJSONArray("issues");
            final long totalResult = resultObject.getLong("total");
            final long startAtResult = resultObject.getLong("startAt");
            List<Issue.Summary> issueList = new LinkedList<Issue.Summary>();
            for (int i = 0; i < issuesArray.length(); i++) {
                JSONObject issue = issuesArray.getJSONObject(i);
                Issue.Summary readValue = Jack.son().readValue(issue.toString(), Issue.Summary.class);
                issueList.add(readValue);
            }
            return new ResultList(totalResult, startAtResult, issueList);
        } catch (IOException ex) {
            throw new CommunicationException(ex);
        } catch (JSONException ex) {
            throw new CommunicationException(ex);
        }
    }

    public Issue getIssueWithKey(String key, LoginInfo account) {
        try {
            Preconditions.checkNotNull(key);
            String url = MessageFormat.format(ISSUE_PATH_PATTERN, key.toString());
            GETCommand cmd = new GETCommand(url);
            CommandResult result = restRunner.run(cmd, account);
            return Jack.son().readValue(result.getResult(), Issue.class);
        } catch (IOException ex) {
            throw new CommunicationException(ex);
        }
    }

    public static class ResultList {
        public long total;
        public long startAt;
        public List<Issue.Summary> issues;

        public ResultList(long total, long startAt, List<Issue.Summary> issues) {
            this.total = total;
            this.startAt = startAt;
            this.issues = issues;
        }
    }
}

package com.tadamski.arij.issue.list.drawer;

import android.net.Uri;
import android.util.Log;

import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.retrofit.AuthorizationHeaderGenerator;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tmszdmsk on 31.07.13.
 */
public class QuickSearch {
    public static final String TAG = "#QUICK-SEARCH#";
    private AuthorizationHeaderGenerator authorizationHeaderGenerator = new AuthorizationHeaderGenerator();

    public Action perform(String query, LoginInfo loginInfo) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(loginInfo.getBaseURL() + "/secure/QuickSearch.jspa").openConnection();
            String content = "searchString=" + URLEncoder.encode(query, "UTF-8");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", authorizationHeaderGenerator.getValue(loginInfo));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.writeBytes(content);
            out.flush();
            out.close();
            String location = connection.getHeaderField("Location");
            return chooseAction(location, loginInfo);
        } catch (Exception ex) {
            Log.e(TAG, "quick search failed, returning default jql", ex);
            return new DoJQL("text ~ \"" + escapeQuery(query) + "\"");
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private Action chooseAction(String location, LoginInfo loginInfo) {
        Pattern singleIssuePattern = Pattern.compile(loginInfo.getBaseURL() + "/?browse/((.+)-[0-9]+)");
        Matcher singleIssueMatcher = singleIssuePattern.matcher(location);
        if (singleIssueMatcher.find()) {
            String issueKey = singleIssueMatcher.group(1);
            return new OpenIssue(issueKey);
        }
        Uri locationUri = Uri.parse(location);
        if (locationUri.getQueryParameterNames().contains("jql")) {
            return new DoJQL(locationUri.getQueryParameter("jql"));
        }
        throw new IllegalArgumentException("Cannot handle that location redirection: " + location);
    }

    private String escapeQuery(String query) {
        return query.replaceAll("\"", "\\\\\\\\\\\\\"");
    }

    public interface Action {
    }

    public class DoJQL implements Action {
        public String jql;

        public DoJQL(String jql) {
            this.jql = jql;
        }
    }

    public class OpenIssue implements Action {
        public String issueKey;

        public OpenIssue(String issueKey) {
            this.issueKey = issueKey;
        }
    }

}

package com.tadamski.arij.issue.list.drawer;

import android.net.Uri;

import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.retrofit.AuthorizationHeaderGenerator;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by tmszdmsk on 31.07.13.
 */
public class QuerySearch {
    private AuthorizationHeaderGenerator authorizationHeaderGenerator = new AuthorizationHeaderGenerator();

    public String getJql(String query, LoginInfo loginInfo) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(loginInfo.getBaseURL() + "/secure/QuickSearch.jspa").openConnection();
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
            Uri uri = Uri.parse(location);
            String jql = uri.getQueryParameter("jql");
            return jql;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

package com.tadamski.arij.login;

import android.net.Uri;
import com.google.common.base.Preconditions;
import java.net.URL;

/**
 *
 * @author tmszdmsk
 */
public class LoginInfo {

    private final String username;
    private final String password;
    private final String baseUrl;

    public LoginInfo(String username, String password, String baseUrl) {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);
        Preconditions.checkNotNull(baseUrl);
        this.username = username;
        this.password = password;
        this.baseUrl = baseUrl;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseURL() {
        return baseUrl;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginInfo{" + "username=" + username + ", baseUrl=" + baseUrl + '}';
    }
}

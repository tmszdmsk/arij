package com.tadamski.arij.account.service;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author tmszdmsk
 */

public class LoginInfo implements Serializable {

    @Expose
    private String username;
    @Expose
    private String password;
    private String baseUrl;

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

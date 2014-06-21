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
    private boolean secureHttps;

    public LoginInfo(String username, String password, String baseUrl, boolean secureHttps) {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);
        Preconditions.checkNotNull(baseUrl);
        this.username = username;
        this.password = password;
        this.baseUrl = baseUrl;
        this.secureHttps = secureHttps;
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

    public boolean isSecureHttps() {
        return secureHttps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginInfo loginInfo = (LoginInfo) o;

        if (!baseUrl.equals(loginInfo.baseUrl)) return false;
        if (!username.equals(loginInfo.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + baseUrl.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "username='" + username + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", secureHttps=" + secureHttps +
                '}';
    }
}

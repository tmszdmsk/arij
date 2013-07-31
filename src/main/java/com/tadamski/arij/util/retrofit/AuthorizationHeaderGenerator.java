package com.tadamski.arij.util.retrofit;

import android.util.Base64;

import com.tadamski.arij.account.service.LoginInfo;

/**
 * Created by tmszdmsk on 31.07.13.
 */
public class AuthorizationHeaderGenerator {
    public String getValue(LoginInfo credentials) {
        byte[] toEncode = (credentials.getUsername() + ":" + credentials.getPassword()).getBytes();
        return "Basic " + Base64.encodeToString(toEncode, Base64.NO_WRAP);
    }
}

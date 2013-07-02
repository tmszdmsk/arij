package com.tadamski.arij.util.retrofit;

import android.util.Base64;
import com.tadamski.arij.account.service.LoginInfo;
import retrofit.RequestInterceptor;

/**
 * Created by tmszdmsk on 02.07.13.
 */
public class AuthorizationInterceptor implements RequestInterceptor {

    private final LoginInfo loginInfo;

    public AuthorizationInterceptor(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    @Override
    public void intercept(RequestFacade requestFacade) {
        requestFacade.addHeader("Authorization", authorizationHeaderValue(loginInfo));
    }

    private String authorizationHeaderValue(LoginInfo credentials) {
        byte[] toEncode = (credentials.getUsername() + ":" + new String(credentials.getPassword())).getBytes();
        return "Basic " + Base64.encodeToString(toEncode, Base64.NO_WRAP);
    }
}

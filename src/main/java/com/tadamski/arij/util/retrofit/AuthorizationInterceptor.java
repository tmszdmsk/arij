package com.tadamski.arij.util.retrofit;

import com.tadamski.arij.account.service.LoginInfo;

import retrofit.RequestInterceptor;

/**
 * Created by tmszdmsk on 02.07.13.
 */
public class AuthorizationInterceptor implements RequestInterceptor {

    private final LoginInfo loginInfo;
    private AuthorizationHeaderGenerator authorizationHeaderGenerator = new AuthorizationHeaderGenerator();

    public AuthorizationInterceptor(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    @Override
    public void intercept(RequestFacade requestFacade) {
        requestFacade.addHeader("Authorization", authorizationHeaderGenerator.getValue(loginInfo));
    }


}

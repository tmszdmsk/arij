package com.tadamski.arij.account.service;

import com.googlecode.androidannotations.annotations.EBean;
import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 03.06.13
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
@EBean
public class LoginService {

    public Response checkCredentials(LoginInfo loginInfo) {
        RestAdapter restAdapter = new RestAdapter.Builder().setServer(loginInfo.getBaseURL()).build();
        LoginResource loginResource = restAdapter.create(LoginResource.class);
        Response response = loginResource.checkCredentials(loginInfo);
        return response;
    }
}

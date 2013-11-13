package com.tadamski.arij.account.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 03.06.13
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
@EBean
public class LoginService {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public Response checkCredentials(LoginInfo loginInfo) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(loginInfo.getBaseURL())
                .setConverter(new GsonConverter(gson))
                .setClient(new RestAdapterProvider.UrlConnectionClient(loginInfo.isSecureHttps()))
                .build();
        LoginResource loginResource = restAdapter.create(LoginResource.class);
        Response response = loginResource.checkCredentials(loginInfo);
        return response;
    }
}

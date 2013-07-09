package com.tadamski.arij.util.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tadamski.arij.BuildConfig;
import com.tadamski.arij.account.service.LoginInfo;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by tmszdmsk on 02.07.13.
 */
public class RestAdapterProvider {

    public static <T> T get(Class<T> clazz, LoginInfo loginInfo) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create();
        return new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setDebug(BuildConfig.DEBUG)
                .setServer(loginInfo.getBaseURL())
                .setRequestInterceptor(new AuthorizationInterceptor(loginInfo))
                .setErrorHandler(new JiraErrorHandler())
                .build()
                .create(clazz);
    }

}

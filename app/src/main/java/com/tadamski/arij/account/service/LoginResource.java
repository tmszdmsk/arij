package com.tadamski.arij.account.service;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 03.06.13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
public interface LoginResource {

    @POST("/rest/auth/1/session")
    Response checkCredentials(@Body LoginInfo loginInfo);
}

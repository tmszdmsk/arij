package com.tadamski.arij.util.retrofit;

import com.tadamski.arij.util.rest.exceptions.HttpException;
import com.tadamski.arij.util.rest.exceptions.NetworkException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by t.adamski on 7/9/13.
 */
public class JiraErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError retrofitError) {
        if (retrofitError.isNetworkError()) {
            throw new NetworkException(retrofitError);
        } else if (retrofitError.getResponse() != null) {
            //unfortunately Jira API is very inconsitnently using http error codes to communicate problems. What given error code means
            //has to be determined in use case(rest resource endpoint) context
            throw new HttpException(retrofitError.getResponse().getStatus(), retrofitError.getResponse().getReason(), retrofitError);
        } else {
            throw retrofitError;
        }
    }
}

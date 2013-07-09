package com.tadamski.arij.util.retrofit;

import com.tadamski.arij.util.rest.exceptions.ForbiddenException;
import com.tadamski.arij.util.rest.exceptions.NetworkException;
import com.tadamski.arij.util.rest.exceptions.NotFoundException;
import com.tadamski.arij.util.rest.exceptions.ServerErrorException;
import com.tadamski.arij.util.rest.exceptions.UnauthorizedException;

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
        } else {
            switch (retrofitError.getResponse().getStatus()) {
                case 401:
                    throw new UnauthorizedException(retrofitError);
                case 403:
                    throw new ForbiddenException(retrofitError);
                case 404:
                    throw new NotFoundException(retrofitError);
                case 500:
                    throw new ServerErrorException(retrofitError);
            }
        }
        throw retrofitError;
    }
}

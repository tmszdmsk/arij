package com.tadamski.arij.util.rest.exceptions;

/**
 * Created by t.adamski on 7/10/13.
 */
public class HttpException extends RuntimeException {

    public HttpException(int httpStatusCode, String msg, Throwable ex) {
        super("[" + httpStatusCode + "] " + msg, ex);
    }

}

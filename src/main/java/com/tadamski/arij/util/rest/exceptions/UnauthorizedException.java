package com.tadamski.arij.util.rest.exceptions;

/**
 * 401 HTTP response code
 *
 * @author t.adamski
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String responseMessage) {
        super(responseMessage);
    }

    public UnauthorizedException(Throwable throwable) {
        super(throwable);
    }
}

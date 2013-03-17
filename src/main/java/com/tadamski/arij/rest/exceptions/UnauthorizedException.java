package com.tadamski.arij.rest.exceptions;

/**
 * 401 HTTP response code
 * @author t.adamski
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String responseMessage) {
        super(responseMessage);
    }
}

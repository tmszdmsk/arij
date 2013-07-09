package com.tadamski.arij.util.rest.exceptions;

/**
 * 403 HTTP response code
 *
 * @author t.adamski
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String responseMessage) {
        super(responseMessage);
    }

    public ForbiddenException(Throwable throwable) {
        super(throwable);
    }
}

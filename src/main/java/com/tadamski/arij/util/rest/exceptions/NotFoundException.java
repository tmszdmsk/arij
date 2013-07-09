package com.tadamski.arij.util.rest.exceptions;

/**
 * 404 HTTP response code
 *
 * @author t.adamski
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String responseMessage) {
        super(responseMessage);
    }

    public NotFoundException(Throwable ex) {
        super(ex);
    }
}

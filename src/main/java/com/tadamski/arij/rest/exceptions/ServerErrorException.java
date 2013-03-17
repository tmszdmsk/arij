package com.tadamski.arij.rest.exceptions;

/**
 * 500 HTTP response code
 * @author t.adamski
 */
public class ServerErrorException extends RuntimeException {

    public ServerErrorException(String responseMessage) {
        super(responseMessage);
    }
}

package com.tadamski.arij.util.rest.exceptions;

/**
 * 500 HTTP response code
 *
 * @author t.adamski
 */
public class ServerErrorException extends RuntimeException {

    public ServerErrorException(String responseMessage) {
        super(responseMessage);
    }
}

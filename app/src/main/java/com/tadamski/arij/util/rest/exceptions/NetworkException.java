package com.tadamski.arij.util.rest.exceptions;

/**
 * @author t.adamski
 */
public class NetworkException extends RuntimeException {

    public NetworkException(Exception ex) {
        super(ex);
    }

    public NetworkException(String msg) {
        super(msg);
    }

}

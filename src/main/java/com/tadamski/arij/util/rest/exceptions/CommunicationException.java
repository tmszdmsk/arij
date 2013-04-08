package com.tadamski.arij.util.rest.exceptions;

/**
 * @author t.adamski
 */
public class CommunicationException extends RuntimeException {

    public CommunicationException(Exception ex) {
        super(ex);
    }

    public CommunicationException(String msg) {
        super(msg);
    }

}

package com.tadamski.arij.rest.exceptions;

import java.io.IOException;

/**
 *
 * @author t.adamski
 */
public class CommunicationException extends RuntimeException {

    public CommunicationException(Exception ex) {
        super(ex);
    }
    
    public CommunicationException(String msg){
        super(msg);
    }

}

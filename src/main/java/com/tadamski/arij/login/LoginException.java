package com.tadamski.arij.login;

import com.tadamski.arij.rest.exceptions.UnauthorizedException;

/**
 *
 * @author t.adamski
 */
public class LoginException extends Exception {

    public LoginException(Exception ex) {
        super(ex);
    }
}

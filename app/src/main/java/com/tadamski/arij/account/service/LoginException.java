package com.tadamski.arij.account.service;

/**
 * @author t.adamski
 */
public class LoginException extends Exception {

    public LoginException(Exception ex) {
        super(ex);
    }
}

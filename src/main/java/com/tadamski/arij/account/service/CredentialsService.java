/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.account.service;

import javax.inject.Singleton;

/**
 * @author tmszdmsk
 */
@Singleton
public class CredentialsService {

    private LoginInfo credentials;

    public void setActive(LoginInfo credentials) {
        this.credentials = credentials;
    }

    public LoginInfo getActive() {
        return credentials;
    }

    public boolean seemsAuthorized() {
        return credentials != null;
    }
}

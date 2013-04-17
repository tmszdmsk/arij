/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.account.service;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

/**
 * @author tmszdmsk
 */
@EBean(scope = Scope.Singleton)
public class CredentialsService {

    private LoginInfo credentials;

    public LoginInfo getActive() {
        return credentials;
    }

    public void setActive(LoginInfo credentials) {
        this.credentials = credentials;
    }

    public boolean seemsAuthorized() {
        return credentials != null;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.util.rest.command;

import com.google.common.base.Preconditions;

/**
 * @author tmszdmsk
 */
public class POSTCommand extends RESTCommand {

    private final String body;

    public POSTCommand(String body, String targetUrl) {
        super(targetUrl);
        Preconditions.checkNotNull(body);
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}

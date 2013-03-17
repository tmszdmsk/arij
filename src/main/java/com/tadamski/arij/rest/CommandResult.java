/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.rest;

/**
 *
 * @author tmszdmsk
 */
public class CommandResult {
    private final String result;

    public CommandResult(String json) {
        this.result = json;
    }

    public String getResult() {
        return result;
    }
    
}

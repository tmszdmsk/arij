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
    private int code;

    public CommandResult(String json, int code) {
        this.result = json;
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }
}

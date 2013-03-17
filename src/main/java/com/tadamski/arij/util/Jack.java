/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author tmszdmsk
 */
public class Jack {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ObjectMapper son() {
        return OBJECT_MAPPER;
    }
}

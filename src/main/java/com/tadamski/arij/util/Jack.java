/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author tmszdmsk
 */
public class Jack {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    static{
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static ObjectMapper son() {
        return OBJECT_MAPPER;
    }
}

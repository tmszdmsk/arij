/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.auth;

import com.tadamski.arij.rest.RESTRunner;
import java.io.IOException;
import java.net.URLEncoder;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author tmszdmsk
 */
public class RESTRunnerTest {

    public RESTRunnerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLocalAuth() throws IOException {
        RESTRunner restRunner = new RESTRunner();
//        GETResult runCommand = restRunner.runCommand(new GETCommand("http://localhost:8080/rest/api/2/search"));
//        System.out.println(runCommand);
    }
}

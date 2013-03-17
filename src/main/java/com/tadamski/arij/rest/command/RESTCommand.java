package com.tadamski.arij.rest.command;

import static com.google.common.base.Preconditions.*;

/**
 *
 * @author tmszdmsk
 */
public class RESTCommand {

    private final String targetUrl;

    public RESTCommand(String targetUrl) {
        checkNotNull(targetUrl, "targetUrl cannot be null");
        this.targetUrl = targetUrl;
    }

    public String getPath() {
        return targetUrl;
    }
}

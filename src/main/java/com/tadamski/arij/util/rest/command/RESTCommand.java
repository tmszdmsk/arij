package com.tadamski.arij.util.rest.command;

import static com.google.common.base.Preconditions.checkNotNull;

/**
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

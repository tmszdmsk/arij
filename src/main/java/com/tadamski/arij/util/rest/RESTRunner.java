package com.tadamski.arij.util.rest;

import android.util.Base64;
import android.util.Log;
import com.tadamski.arij.account.service.CredentialsService;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.rest.command.GETCommand;
import com.tadamski.arij.util.rest.command.POSTCommand;
import com.tadamski.arij.util.rest.command.RESTCommand;
import com.tadamski.arij.util.rest.exceptions.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * @author tmszdmsk
 */
@Singleton
public class RESTRunner {

    private static final String TAG = RESTRunner.class.getName();
    @Inject
    private CredentialsService credentialsService;

    public CommandResult run(RESTCommand command) {
        return run(command, credentialsService.getActive());
    }

    public CommandResult run(RESTCommand command, LoginInfo loginInfo) {
        if (command instanceof POSTCommand) {
            return runPost((POSTCommand) command, loginInfo);
        } else if (command instanceof GETCommand) {
            return runGet((GETCommand) command, loginInfo);
        }
        throw new UnsupportedOperationException("unsupported RESTCommand of type" + command.getClass());
    }

    private CommandResult runPost(POSTCommand postCommand, LoginInfo loginInfo) {
        try {
            URL url = getURL(loginInfo.getBaseURL(), postCommand.getPath());
            Log.d(TAG, MessageFormat.format("Sending {0} to {1} with login {2}", postCommand.getBody(), url, loginInfo.getUsername()));
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + encodeCredentials(loginInfo));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            doOutput(connection, postCommand.getBody());
            handleErrors(connection);
            return new CommandResult(getOutputFromConnection(connection), connection.getResponseCode());
        } catch (IOException ex) {
            throw new CommunicationException(ex);
        }
    }

    private CommandResult runGet(GETCommand getCommand, LoginInfo loginInfo) {
        try {
            URL url = getURL(loginInfo.getBaseURL(), getCommand.getPath());
            Log.i(TAG, MessageFormat.format("Sending GET to {0} with login {1}", url, loginInfo.getUsername()));
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + encodeCredentials(loginInfo));
            connection.setDoInput(true);
            connection.setDoOutput(false);
            handleErrors(connection);
            return new CommandResult(getOutputFromConnection(connection), connection.getResponseCode());
        } catch (IOException ex) {
            throw new CommunicationException(ex);
        }
    }

    private void doOutput(final HttpURLConnection connection, String body) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.append(body);
        writer.flush();
        writer.close();
    }

    private String encodeCredentials(LoginInfo credentials) {
        byte[] toEncode = (credentials.getUsername() + ":" + new String(credentials.getPassword())).getBytes();
        return Base64.encodeToString(toEncode, Base64.NO_WRAP);
    }

    private String getOutputFromConnection(final HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String result = sb.toString();
        Log.i(TAG, "Got response: " + result);
        return result;
    }

    private URL getURL(String base, String subPath) {
        try {
            URL baseUrl = new URL(base);
            String file;
            if (!baseUrl.getFile().endsWith("/")) {
                file = baseUrl.getFile().concat("/").concat(subPath);
            } else {
                file = baseUrl.getFile().concat(subPath);
            }
            return new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), file);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleErrors(HttpURLConnection connection) throws IOException {
        int code = connection.getResponseCode();
        switch (code) {
            case 401:
                throw new UnauthorizedException(connection.getResponseMessage());
            case 403:
                throw new ForbiddenException(connection.getResponseMessage());
            case 404:
                throw new NotFoundException(connection.getResponseMessage());
            case 500:
                throw new ServerErrorException(connection.getResponseMessage());
        }

    }
}

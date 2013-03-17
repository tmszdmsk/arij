package com.tadamski.arij.login;

import com.google.inject.Inject;
import com.tadamski.arij.rest.RESTRunner;
import com.tadamski.arij.rest.command.POSTCommand;
import com.tadamski.arij.rest.exceptions.CommunicationException;
import com.tadamski.arij.rest.exceptions.ForbiddenException;
import com.tadamski.arij.rest.exceptions.UnauthorizedException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author t.adamski
 */
public class LoginService {

    private static final String POST_PATH = "rest/auth/1/session";
    @Inject
    private RESTRunner restRunner;

    public void checkCredentials(LoginInfo loginInfo) throws LoginException, CommunicationException {
        try {
            JSONObject jsonObject = new JSONObject().put("username", loginInfo.getUsername()).put("password", new String(loginInfo.getPassword()));
            POSTCommand post = new POSTCommand(jsonObject.toString(), POST_PATH);
            restRunner.run(post, loginInfo);
        } catch (JSONException ex) {
            throw new CommunicationException(ex);
        } catch (UnauthorizedException ex) {
            throw new LoginException(ex);
        } catch (ForbiddenException ex) {
            throw new LoginException(ex);
        }

    }
}

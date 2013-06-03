package com.tadamski.arij.account.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.widget.Button;
import android.widget.EditText;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.common.base.Strings;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.R;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginException;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.account.service.LoginService;
import com.tadamski.arij.util.rest.exceptions.CommunicationException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author t.adamski
 */
@EActivity(R.layout.login)
public class AddNewAccountActivity extends SherlockAccountAuthenticatorActivity {

    @ViewById(R.id.url_edit_text)
    EditText urlEditText;
    @ViewById(R.id.login_edit_text)
    EditText loginEditText;
    @ViewById(R.id.password_edit_text)
    EditText passwordEditText;
    @ViewById(R.id.login_button)
    Button loginButton;
    @StringRes(R.string.invalid_url_format)
    String invalidUrlFormat;
    @StringRes(R.string.invalid_login_empty)
    String invalidLoginEmpty;
    @StringRes(R.string.invalid_login_credentials)
    String invalidLoginCredentials;
    @Bean
    LoginService loginService;
    @SystemService
    AccountManager accountManager;

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStop(this);
    }

    @AfterViews
    protected void initUrlEditText() {
        urlEditText.setText(getString(R.string.add_account_url_default_hostname));
        Selection.setSelection(urlEditText.getText(), 0);

    }

    @Click(R.id.login_button)
    void login() {
        String url = urlEditText.getText().toString();
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (validate()) {
            checkCredentials(url, login, password);
        }
    }

    @Background
    void checkCredentials(String url, String login, String password) {
        try {
            LoginInfo credentials = null;
            try {
                LoginInfo possibleCredentials = new LoginInfo(login, password, "https://" + url);
                loginService.checkCredentials(possibleCredentials);
                credentials = possibleCredentials;
            } catch (CommunicationException ex) {
                try {
                    LoginInfo possibleCredentials = new LoginInfo(login, password, "http://" + url);
                    loginService.checkCredentials(possibleCredentials);
                    credentials = possibleCredentials;
                } catch (CommunicationException ex2) {
                    ifCommunicationException(ex2);
                }
            }
            if (credentials != null)
                ifCredentialsConfirmed(credentials);
        } catch (LoginException e) {
            ifCredentialsInvalid();
        }
    }

    @UiThread
    void ifCredentialsConfirmed(LoginInfo credentials) {
        createAccountAndFinish(credentials);
    }

    @UiThread
    void ifCredentialsInvalid() {
        loginEditText.setError(invalidLoginCredentials);
    }

    @UiThread
    void ifCommunicationException(Exception ex) {
        new AlertDialog.Builder(AddNewAccountActivity.this).setMessage(ex.getLocalizedMessage()).create().show();
    }

    private boolean validate() {
        boolean seemsValid = true;
        //url
        String url = urlEditText.getText().toString();
        try {
            new URL("http://" + url);
            urlEditText.setError(null);
        } catch (MalformedURLException ex) {
            urlEditText.setError(invalidUrlFormat);
            seemsValid = false;
        }
        //login
        String login = loginEditText.getText().toString();
        if (Strings.isNullOrEmpty(login)) {
            loginEditText.setError(invalidLoginEmpty);
            seemsValid = false;
        } else {
            loginEditText.setError(null);
        }
        return seemsValid;
    }

    private void createAccountAndFinish(LoginInfo credentials) {
        Account account = new Account(credentials.getUsername(), Authenticator.ACCOUNT_TYPE);
        Bundle bundle = new Bundle();
        bundle.putString(Authenticator.INSTANCE_URL_KEY, credentials.getBaseURL().toString());
        accountManager.addAccountExplicitly(account, String.valueOf(credentials.getPassword()), bundle);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, credentials.getUsername());
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Authenticator.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}

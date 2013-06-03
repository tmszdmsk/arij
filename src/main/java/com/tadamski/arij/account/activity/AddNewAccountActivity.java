package com.tadamski.arij.account.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.common.base.Strings;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.R;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.account.service.LoginService;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author t.adamski
 */
@EActivity(R.layout.login)
public class AddNewAccountActivity extends SherlockAccountAuthenticatorActivity {

    @ViewById(R.id.url_edit_text)
    EditText urlEditText;
    @ViewById(R.id.add_account_default_postfix_button)
    Button defaultPostfixButton;
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

    @Click(R.id.add_account_default_postfix_button)
    void changeDefaultHostnamePostfix() {
        int initialLength = urlEditText.getText().length();
        String postFix = defaultPostfixButton.getText().toString();
        defaultPostfixButton.setVisibility(View.GONE);
        urlEditText.append(postFix);
        Selection.setSelection(urlEditText.getText(), initialLength);
    }

    @Click(R.id.login_button)
    void login() {
        String url = getBaseUrl();
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (validate()) {
            checkCredentials(url, login, password);
        }
    }

    String getBaseUrl() {
        if (defaultPostfixButton.getVisibility() == View.VISIBLE) {
            return urlEditText.getText().toString() + defaultPostfixButton.getText().toString();
        } else {
            return urlEditText.getText().toString();
        }
    }

    private CheckResult checkServer(LoginInfo loginInfo) {
        try {
            Response response = loginService.checkCredentials(loginInfo);
            return new CheckResult(response.getStatus(), response.toString());
        } catch (RetrofitError retrofitError) {
            if (retrofitError.isNetworkError()) {
                return new CheckResult(0, retrofitError.getCause().toString());
            } else {
                return new CheckResult(retrofitError.getResponse().getStatus(), retrofitError.toString());
            }
        }
    }

    @Background
    void checkCredentials(String url, String login, String password) {
        LoginInfo possibleCredentials = new LoginInfo(login, password, "https://" + url);
        CheckResult result = checkServer(possibleCredentials);
        if (result.code == 200) {
            ifCredentialsConfirmed(possibleCredentials);
        } else if (result.code == 401) {
            ifCredentialsInvalid();
        } else {
            possibleCredentials = new LoginInfo(login, password, "http://" + url);
            result = checkServer(possibleCredentials);
            if (result.code == 200) {
                ifCredentialsConfirmed(possibleCredentials);
            } else if (result.code == 401) {
                ifCredentialsInvalid();
            } else {
                ifCommunicationException(new RuntimeException(result.code + result.reason));
            }
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
        String url = getBaseUrl();
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

    private static class CheckResult {
        int code;
        String reason;

        private CheckResult(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }
    }


}

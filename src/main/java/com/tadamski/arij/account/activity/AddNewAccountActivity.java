package com.tadamski.arij.account.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.common.base.Strings;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.R;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.account.service.LoginService;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit.RetrofitError;
import retrofit.client.Response;

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
    void initListeners() {
        OnDoneImeAction onDoneImeAction = new OnDoneImeAction();
        urlEditText.setOnEditorActionListener(onDoneImeAction);
        loginEditText.setOnEditorActionListener(onDoneImeAction);
        passwordEditText.setOnEditorActionListener(onDoneImeAction);
    }

    @Click(R.id.login_button)
    void login() {
        EasyTracker.getTracker().sendEvent("AddNewAccountActivity", "login_button_pushed", null, null);
        String url = urlEditText.getText().toString();
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (validate()) {
            checkCredentials(url, login, password);
        } else {
            EasyTracker.getTracker().sendEvent("AddNewAccountActivity", "validation_not_passed", null, null);
        }
    }

    @UiThread
    void setLoginButtonState(boolean enabled) {
        loginButton.setEnabled(enabled);
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
        try {
            setLoginButtonState(false);
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
                    ifCommunicationException(result.code + result.reason);
                }
            }
        } finally {
            setLoginButtonState(true);
        }
    }

    @UiThread
    void ifCredentialsConfirmed(LoginInfo credentials) {
        EasyTracker.getTracker().sendEvent("AddNewAccountActivity", "login_success", null, null);
        createAccountAndFinish(credentials);
    }

    @UiThread
    void ifCredentialsInvalid() {
        EasyTracker.getTracker().sendEvent("AddNewAccountActivity", "login_failed_invalid_credentials", null, null);
        loginEditText.setError(invalidLoginCredentials);
    }

    @UiThread
    void ifCommunicationException(String msg) {
        EasyTracker.getTracker().sendEvent("AddNewAccountActivity", "login_failed_exception", msg, null);
        new AlertDialog.Builder(AddNewAccountActivity.this).setMessage(msg).create().show();
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

    private static class CheckResult {
        int code;
        String reason;

        private CheckResult(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }
    }

    private class OnDoneImeAction implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
            return false;
        }

    }


}

package com.tadamski.arij.activity.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.tadamski.arij.R;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.login.LoginException;
import com.tadamski.arij.login.LoginInfo;
import com.tadamski.arij.login.LoginService;
import com.tadamski.arij.rest.exceptions.CommunicationException;
import com.tadamski.arij.util.Callback;
import com.tadamski.arij.util.Result;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;
import roboguice.activity.RoboAccountAuthenticatorActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 *
 * @author t.adamski
 */
public class LoginActivity extends RoboAccountAuthenticatorActivity {

    @InjectView(R.id.url_edit_text)
    private EditText urlEditText;
    @InjectView(R.id.login_edit_text)
    private EditText loginEditText;
    @InjectView(R.id.password_edit_text)
    private EditText passwordEditText;
    @InjectView(R.id.login_button)
    private Button loginButton;
    @InjectResource(R.string.invald_url_format)
    private String invalidUrlFormat;
    @InjectResource(R.string.invalid_login_empty)
    private String invalidLoginEmpty;
    @InjectResource(R.string.invalid_login_credentials)
    private String invalidLoginCredentials;
    @Inject
    private LoginService loginService;
    @Inject
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login);
        createLoginButtonListener();
    }

    private boolean validate() {
        boolean seemsValid = true;
        //url
        String url = urlEditText.getText().toString();
        try {
            new URL(url);
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

    private LoginInfo getCredentials() {
        String url = urlEditText.getText().toString();
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        return new LoginInfo(login, password, url);
    }

    private void createLoginButtonListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    final LoginInfo credentials = getCredentials();
                    new LoginActivity.LoginAsyncTask(new Callback<Result<Callback.Void>>() {
                        @Override
                        public void call(Result<Callback.Void> result) {
                            if (result.success()) {
                                createAccountAndFinish(credentials);
                            } else {
                                if (result.getException() instanceof LoginException) {
                                    loginEditText.setError(invalidLoginCredentials);
                                } else if (result.getException() instanceof CommunicationException) {
                                    new AlertDialog.Builder(LoginActivity.this).setMessage(result.getException().getLocalizedMessage()).create().show();
                                }
                            }
                        }
                    }).execute(credentials);
                }
            }
        });
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

    private class LoginAsyncTask extends AsyncTask<LoginInfo, Void, Boolean> {

        private Throwable ex;
        private final Callback<Result<Callback.Void>> callback;

        public LoginAsyncTask(Callback<Result<Callback.Void>> callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(LoginInfo... params) {
            try {
                Preconditions.checkArgument(params.length == 1);
                loginService.checkCredentials(params[0]);
                return true;
            } catch (LoginException ex) {
                this.ex = ex;
            } catch (CommunicationException ex) {
                this.ex = ex;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (ex != null) {
                callback.call(new Result<Callback.Void>(ex));
            } else {
                callback.call(new Result<Callback.Void>(Callback.VOID));
            }
        }
    }
}

package com.tadamski.arij.account.authenticator;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tadamski.arij.R;
import com.tadamski.arij.account.activity.AddNewAccountActivity_;

/**
 * @author t.adamski
 */
public class Authenticator extends AbstractAccountAuthenticator {

    public static final String ACCOUNT_TYPE = "com.tadamski.arij.account";
    public static final String INSTANCE_URL_KEY = "instanceUrl";

    private final Context context;

    public Authenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(context, AddNewAccountActivity_.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return context.getString(R.string.app_name);
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

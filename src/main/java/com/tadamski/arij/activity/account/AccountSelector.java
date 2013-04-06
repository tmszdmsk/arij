package com.tadamski.arij.activity.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.tadamski.arij.R;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.activity.issuelist.IssueListActivity;
import com.tadamski.arij.login.CredentialsService;
import com.tadamski.arij.login.LoginInfo;
import com.tadamski.arij.util.Callback;
import roboguice.activity.RoboListActivity;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tmszdmsk
 */
public class AccountSelector extends RoboListActivity implements OnAccountsUpdateListener {

    @Inject
    private AccountManager accountManager;
    @Inject
    private CredentialsService credentialsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager.addOnAccountsUpdatedListener(this, null, true);
        reloadAccounts();
        if (getListAdapter().isEmpty()) {
            new OpenAddNewAccountScreen().call(null);
        }
    }

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

    @Override
    protected void onDestroy() {
        accountManager.removeOnAccountsUpdatedListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, getString(R.string.add_account));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new OpenAddNewAccountScreen().call(null);
        return super.onOptionsItemSelected(item);
    }

    private List<LoginInfo> getAvailableAccount() {
        List<LoginInfo> result = new LinkedList<LoginInfo>();
        Account[] accountsByType = accountManager.getAccountsByType(Authenticator.ACCOUNT_TYPE);
        for (Account account : accountsByType) {
            String instanceUrl = accountManager.getUserData(account, Authenticator.INSTANCE_URL_KEY);
            String password = accountManager.getPassword(account);
            result.add(new LoginInfo(account.name, password, instanceUrl));
        }
        return result;
    }

    private void reloadAccounts() {
        List<LoginInfo> availableAccounts = getAvailableAccount();
        AccountListAdapter accountListAdapter = new AccountListAdapter(this, availableAccounts, new OpenTaskListForSelectedAccount());
        setListAdapter(accountListAdapter);
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        reloadAccounts();
    }

    private class OpenTaskListForSelectedAccount implements Callback<LoginInfo> {

        @Override
        public void call(LoginInfo result) {
            credentialsService.setActive(result);
            Intent intent = new Intent(AccountSelector.this, IssueListActivity.class);
            startActivity(intent);
        }
    }

    private class OpenAddNewAccountScreen implements Callback<Callback.Void> {

        @Override
        public void call(Void result) {
            accountManager.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, AccountSelector.this, null, null);
        }
    }
}

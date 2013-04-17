package com.tadamski.arij.account.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.SystemService;
import com.tadamski.arij.R;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.CredentialsService;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.activity.list.IssueListActivity_;

import java.util.LinkedList;
import java.util.List;

/**
 * @author tmszdmsk
 */
@EActivity
public class AccountSelectorActivity extends SherlockListActivity implements OnAccountsUpdateListener {

    @SystemService
    AccountManager accountManager;
    @Bean
    CredentialsService credentialsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager.addOnAccountsUpdatedListener(this, null, true);
        reloadAccounts();
        if (getListAdapter().isEmpty()) {
            openAddNewAccountScreen();
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
        getSupportMenuInflater().inflate(R.menu.account_selector_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_account) {
            openAddNewAccountScreen();
            return true;
        }
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
        AccountListAdapter accountListAdapter = new AccountListAdapter(this, availableAccounts);
        setListAdapter(accountListAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        credentialsService.setActive((LoginInfo) getListAdapter().getItem(position));
        Intent intent = new Intent(AccountSelectorActivity.this, IssueListActivity_.class);
        startActivity(intent);
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        reloadAccounts();
    }

    private void openAddNewAccountScreen() {
        accountManager.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, AccountSelectorActivity.this, null, null);
    }

}

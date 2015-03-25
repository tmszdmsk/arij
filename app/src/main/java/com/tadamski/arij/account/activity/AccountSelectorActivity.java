package com.tadamski.arij.account.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.tadamski.arij.R;
import com.tadamski.arij.account.AccountsService;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.IssueListActivity_;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.SystemService;

import java.util.List;

/**
 * @author tmszdmsk
 */
@EActivity
public class AccountSelectorActivity extends ListActivity implements OnAccountsUpdateListener {

    @SystemService
    AccountManager accountManager;
    @Bean
    AccountsService accountsService;
    ActionMode activeActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager.addOnAccountsUpdatedListener(this, null, true);
        reloadAccounts();
        if(savedInstanceState==null&&getIntent().getDataString()!=null){
            deeplink(getIntent().getDataString());
        }
        if (getListAdapter().isEmpty()) {
            openAddNewAccountScreen();
        }
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }
    private boolean deeplink(String url){
        int ofs =url==null?-1:url.indexOf("/browse/");
        if (ofs>0) {//looks like a link to an issue, find out what account it belongs to
            ListAdapter adapter = getListAdapter();
            String issue = url.substring(ofs+8);
            for (int i = 0; i < adapter.getCount(); i++) {
                LoginInfo loginInfo = (LoginInfo) adapter.getItem(i);
                if(url.contains(loginInfo.getBaseURL())){
                    IssueActivity_.intent(this)
                                  .issueKey(issue)
                                  .loginInfo(loginInfo)
                                  .start();
                    return true;
                }
            }
            new AlertDialog.Builder(this)
                    .setTitle("Account not found")
                    .setMessage("Please create an account for "+url.substring(0,url.indexOf("/browse")) )
                    .create()
                    .show();
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Tracker.activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tracker.activityStop(this);
    }

    @Override
    protected void onDestroy() {
        accountManager.removeOnAccountsUpdatedListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_selector_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_account) {
            EasyTracker.getTracker().sendEvent("AccountSelectorActivity", "account_add_manual", null, null);
            openAddNewAccountScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadAccounts() {
        List<LoginInfo> availableAccounts = accountsService.getAvailableAccounts();
        AccountListAdapter accountListAdapter = new AccountListAdapter(this, availableAccounts);
        setListAdapter(accountListAdapter);
    }

    @ItemLongClick(android.R.id.list)
    void onAccountLongClick(final int position) {
        getListView().setItemChecked(position, true);
        startActionMode(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                activeActionMode = mode;
                mode.getMenuInflater().inflate(R.menu.account_selector_menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_remove_account:
                        LoginInfo loginInfo = (LoginInfo) getListAdapter().getItem(getListView().getCheckedItemPosition());
                        removeAccount(loginInfo);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                activeActionMode = null;
                getListView().setItemChecked(getListView().getCheckedItemPosition(), false);
            }
        });
    }

    private void removeAccount(LoginInfo loginInfo) {
        EasyTracker.getTracker().sendEvent("AccountSelectorActivity", "account_removed", null, null);
        accountManager.removeAccount(new Account(loginInfo.getUsername(), Authenticator.ACCOUNT_TYPE), null, null);
    }

    @ItemClick(android.R.id.list)
    void onAccountClick(int position) {
        LoginInfo loginInfo = (LoginInfo) getListAdapter().getItem(position);
        if (activeActionMode == null) {
            getListView().setItemChecked(position, false);
            EasyTracker.getTracker().sendEvent("AccountSelectorActivity", "account_open", null, Long.valueOf(position));
            IssueListActivity_.intent(this).loginInfo(loginInfo).start();
        } else {
            getListView().setItemChecked(getListView().getCheckedItemPosition(), false);
            getListView().setItemChecked(position, true);
        }
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        reloadAccounts();
    }

    private void openAddNewAccountScreen() {
        accountManager.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, AccountSelectorActivity.this, null, null);
    }
}

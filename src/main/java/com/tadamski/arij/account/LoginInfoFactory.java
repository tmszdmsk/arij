package com.tadamski.arij.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.SystemService;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginInfo;

/**
 * Created by tmszdmsk on 12.07.13.
 */
@EBean
public class LoginInfoFactory {

    @SystemService
    AccountManager accountManager;

    public LoginInfo getLoginInfoFromAccountManager(String accountName) {
        Account[] allAccounts = accountManager.getAccountsByType(Authenticator.ACCOUNT_TYPE);
        for (Account account : allAccounts) {
            if (account.name.equals(accountName)) {
                String password = accountManager.getPassword(account);
                String login = account.name;
                String instanceUrl = accountManager.getUserData(account, Authenticator.INSTANCE_URL_KEY);
                return new LoginInfo(login, password, instanceUrl);
            }
        }
        return null;
    }
}

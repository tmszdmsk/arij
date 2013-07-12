package com.tadamski.arij.account;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.SystemService;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tmszdmsk on 12.07.13.
 */
@EBean
public class AccountsService {

    @SystemService
    AccountManager accountManager;

    public List<LoginInfo> getAvailableAccounts() { List<LoginInfo> result = new LinkedList<LoginInfo>();
        Account[] accountsByType = accountManager.getAccountsByType(Authenticator.ACCOUNT_TYPE);
        for (Account account : accountsByType) {
            String instanceUrl = accountManager.getUserData(account, Authenticator.INSTANCE_URL_KEY);
            String password = accountManager.getPassword(account);
            result.add(new LoginInfo(account.name, password, instanceUrl));
        }
        return result;
    }
}

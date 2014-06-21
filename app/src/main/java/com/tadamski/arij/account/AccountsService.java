package com.tadamski.arij.account;

import android.accounts.Account;
import android.accounts.AccountManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
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
            String isSecureHttps = accountManager.getUserData(account, Authenticator.SECURE_HTTPS_KEY);
            Boolean secureHttps = Boolean.valueOf(isSecureHttps == null ? "true" : isSecureHttps);
            String password = accountManager.getPassword(account);
            result.add(new LoginInfo(account.name, password, instanceUrl, secureHttps));
        }
        return result;
    }

    public LoginInfo getAccount(String accountName) {
        for(LoginInfo loginInfo : getAvailableAccounts()){
            if(loginInfo.getUsername().equals(accountName)){
                return loginInfo;
            }
        }
        return null;
    }
}

package com.tadamski.arij.widget.options;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tadamski.arij.R;
import com.tadamski.arij.account.AccountsService;
import com.tadamski.arij.account.activity.AccountListAdapter;
import com.tadamski.arij.account.authenticator.Authenticator;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.util.analytics.Tracker;
import com.tadamski.arij.widget.HomeScreenWidgetProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by tmszdmsk on 12.07.13.
 */
@EActivity(R.layout.homescreen_widget_options_activity)
public class HomescreenWidgetOptionsActivity extends Activity implements OnAccountsUpdateListener {

    @ViewById(R.id.accounts)
    Spinner accountsSpinner;
    @ViewById(R.id.filters)
    Spinner filtersSpinner;
    @Extra(AppWidgetManager.EXTRA_APPWIDGET_ID)
    int appWidgetId;
    @Bean
    AccountsService accountsService;
    @Bean
    DefaultFilters defaultFilters;
    @SystemService
    AccountManager accountManager;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager.addOnAccountsUpdatedListener(this, null, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accountManager.removeOnAccountsUpdatedListener(this);
    }

    @AfterViews
    void initAccountsList() {
        List<LoginInfo> availableAccounts = accountsService.getAvailableAccounts();
        AccountListAdapter accountListAdapter = new AccountListAdapter(this, availableAccounts);
        accountsSpinner.setAdapter(accountListAdapter);

        List<Filter> filterList = defaultFilters.getFilterList();
        ArrayAdapter<FilterWrapper> adapter = new ArrayAdapter<FilterWrapper>(this, android.R.layout.simple_list_item_1);
        filtersSpinner.setAdapter(adapter);
        for (Filter filter : filterList) {
            adapter.add(new FilterWrapper(filter));
        }

        if (availableAccounts.size() == 0)
            accountManager.addAccount(Authenticator.ACCOUNT_TYPE, null, null, null, this, null, null);
    }

    @Click(R.id.add_homescreen_widget_button)
    void addHomescreenWidget() {
        Intent data = new Intent();
        data.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        LoginInfo selectedAccount = ((LoginInfo) accountsSpinner.getSelectedItem());
        Filter selectedFilter = ((FilterWrapper) filtersSpinner.getSelectedItem()).filter;
        WidgetOptions options = new WidgetOptions(this, appWidgetId);
        options.set(selectedFilter.id, selectedAccount.getUsername());
        setResult(RESULT_OK, data);
        Intent refreshIntent = new Intent(this, HomeScreenWidgetProvider.class);
        refreshIntent.setAction(HomeScreenWidgetProvider.ACTION_REFRESH);
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        refreshIntent.setData(Uri.parse(refreshIntent.toUri(Intent.URI_INTENT_SCHEME)));
        sendBroadcast(refreshIntent);
        Tracker.sendEvent("HomescreenWidget", "Widget added", selectedFilter.id, 0L);
        finish();
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        initAccountsList();
    }

    private static class FilterWrapper {
        private Filter filter;

        FilterWrapper(Filter filter) {
            this.filter = filter;
        }

        @Override
        public String toString() {
            return filter.name;
        }
    }

}

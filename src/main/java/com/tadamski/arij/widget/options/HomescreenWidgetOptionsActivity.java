package com.tadamski.arij.widget.options;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.AccountsService;
import com.tadamski.arij.account.activity.AccountListAdapter;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.widget.HomeScreenWidgetProvider;

import java.util.List;

/**
 * Created by tmszdmsk on 12.07.13.
 */
@EActivity(R.layout.homescreen_widget_options_activity)
public class HomescreenWidgetOptionsActivity extends SherlockActivity {

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

    @AfterViews
    void initAccountsList() {
        List<LoginInfo> availableAccounts = accountsService.getAvailableAccounts();
        AccountListAdapter accountListAdapter = new AccountListAdapter(this, availableAccounts);
        accountsSpinner.setAdapter(accountListAdapter);
    }

    @AfterViews
    void initFiltersList() {
        List<Filter> filterList = defaultFilters.getFilterList();
        ArrayAdapter<FilterWrapper> adapter = new ArrayAdapter<FilterWrapper>(this, android.R.layout.simple_list_item_1);
        filtersSpinner.setAdapter(adapter);
        for(Filter filter : filterList){
            adapter.add(new FilterWrapper(filter));
        }
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
        finish();
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

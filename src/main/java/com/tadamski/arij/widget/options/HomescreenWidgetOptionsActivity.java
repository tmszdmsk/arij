package com.tadamski.arij.widget.options;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.AccountsService;
import com.tadamski.arij.account.activity.AccountListAdapter;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.list.filters.FiltersListAdapter;

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
    void initAccountsList(){
        List<LoginInfo> availableAccounts = accountsService.getAvailableAccounts();
        accountsSpinner.setAdapter(new AccountListAdapter(this, availableAccounts));
    }

    @AfterViews
    void initFiltersList(){
        List<Filter> filterList = defaultFilters.getFilterList();
        filtersSpinner.setAdapter(new FiltersListAdapter(this,filterList));
    }

    @Click(R.id.add_homescreen_widget_button)
    void addHomescreenWidget(){
        Intent data = new Intent();
        data.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        LoginInfo selectedAccount = (LoginInfo) accountsSpinner.getSelectedItem();
        Filter selectedFilter = (Filter) filtersSpinner.getSelectedItem();
        data.putExtra(WidgetOptions.ACCOUNT_NAME, selectedAccount.getUsername());
        data.putExtra(WidgetOptions.FILTER_NAME, selectedFilter.name);
        data.putExtra(WidgetOptions.FILTER_JQL, selectedFilter.jql);
        setResult(RESULT_OK, data);
        finish();
    }


}

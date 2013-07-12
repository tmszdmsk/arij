package com.tadamski.arij.widget.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by tmszdmsk on 12.07.13.
 */
public class WidgetOptions {

    public static final String FILTER_NAME = "filterName";
    public static final String FILTER_JQL = "filterJql";
    public static final String ACCOUNT_NAME = "account_name";
    private final SharedPreferences prefs;

    public WidgetOptions(Context ctx, int appWidgetId) {
        prefs = ctx.getSharedPreferences("HomescreenWidgetOptions_widgetId_" + appWidgetId, Context.MODE_PRIVATE);
    }

    public String getFilterName() {
        return prefs.getString(FILTER_NAME, null);
    }

    public String getFilterJql() {
        return prefs.getString(FILTER_JQL, null);
    }

    public String getAccountName() {
        return prefs.getString(ACCOUNT_NAME, null);
    }

    public void set(String filtername, String jql, String accountName) {
        prefs.edit().putString(FILTER_NAME, filtername).putString(FILTER_JQL, jql).putString(ACCOUNT_NAME, accountName).apply();
    }

    public void set(Bundle newOptions) {
        this.set(newOptions.getString(FILTER_NAME), newOptions.getString(FILTER_JQL), newOptions.getString(ACCOUNT_NAME));
    }
}

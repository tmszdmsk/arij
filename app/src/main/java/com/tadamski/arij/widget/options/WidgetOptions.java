package com.tadamski.arij.widget.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by tmszdmsk on 12.07.13.
 */
public class WidgetOptions {

    public static final String FILTER_ID = "filterName";
    public static final String ACCOUNT_NAME = "account_name";
    private final SharedPreferences prefs;

    public WidgetOptions(Context ctx, int appWidgetId) {
        prefs = ctx.getSharedPreferences("HomescreenWidgetOptions_widgetId_" + appWidgetId, Context.MODE_PRIVATE);
    }

    public String getFilterId() {
        return prefs.getString(FILTER_ID, null);
    }

    public String getAccountName() {
        return prefs.getString(ACCOUNT_NAME, null);
    }

    public void set(String filterId, String accountName) {
        prefs.edit().putString(FILTER_ID, filterId).putString(ACCOUNT_NAME, accountName).apply();
    }

    public void set(Bundle newOptions) {
        this.set(newOptions.getString(FILTER_ID), newOptions.getString(ACCOUNT_NAME));
    }

    public boolean exists() {
        return getFilterId() != null && getAccountName() != null;
    }
}

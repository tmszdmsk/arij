package com.tadamski.arij.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by t.adamski on 7/12/13.
 */
public class HomeScreenWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            startRefreshService(ctx, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context ctx, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        startRefreshService(ctx, appWidgetId);
    }

    void startRefreshService(Context ctx, int appWidgetId) {
        Intent intent = RefreshHomescreenWidgetService_.intent(ctx).get().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        ctx.startService(intent);
    }

}
package com.tadamski.arij.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.actionbarsherlock.R;

/**
 * Created by t.adamski on 7/12/13.
 */
public class HomeScreenWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            startRefreshService(ctx, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context ctx, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        startRefreshService(ctx, appWidgetManager, appWidgetId);
    }

    void startRefreshService(Context ctx, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.homescreen_widget);
        Intent intent = new Intent(ctx, RefreshHomescreenWidgetService_.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(R.id.list, intent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list);
    }
}
package com.tadamski.arij.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.tadamski.arij.widget.options.WidgetOptions;

/**
 * Created by t.adamski on 7/12/13.
 */
public class HomeScreenWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_LIST_CLICK = "com.tadamski.arij.homescreenwidget.list.click";
    public static final String LIST_ITEM_EXTRA_ISSUE_KEY = "issueKey";

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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_LIST_CLICK)) {
            String extra = intent.getStringExtra(LIST_ITEM_EXTRA_ISSUE_KEY);
            Toast.makeText(context, "item clicked "+extra, Toast.LENGTH_SHORT).show();
        } else
            super.onReceive(context, intent);
    }

    void startRefreshService(Context ctx, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.homescreen_widget);
        remoteViews.setTextViewText(R.id.filter_name, new WidgetOptions(ctx, appWidgetId).getFilterName());

        Intent onListClickIntent = new Intent(ctx, HomeScreenWidgetProvider.class);
        onListClickIntent.setAction(HomeScreenWidgetProvider.ACTION_LIST_CLICK);
        onListClickIntent.setData(Uri.parse(onListClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        onListClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent onListItemPendingIntent = PendingIntent.getBroadcast(ctx, 0, onListClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.list, onListItemPendingIntent);

        Intent intent = new Intent(ctx, RefreshHomescreenWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.list, intent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}
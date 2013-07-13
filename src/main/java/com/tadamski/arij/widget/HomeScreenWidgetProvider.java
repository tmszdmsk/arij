package com.tadamski.arij.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.actionbarsherlock.R;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;
import com.tadamski.arij.widget.options.WidgetOptions;

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
        remoteViews.setTextViewText(R.id.filter_name, new WidgetOptions(ctx, appWidgetId).getFilterName());
        Intent intent = new Intent(ctx, RefreshHomescreenWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.list, intent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}
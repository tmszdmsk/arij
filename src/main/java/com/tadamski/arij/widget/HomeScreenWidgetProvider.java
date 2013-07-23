package com.tadamski.arij.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.actionbarsherlock.R;
import com.tadamski.arij.account.LoginInfoFactory;
import com.tadamski.arij.account.LoginInfoFactory_;
import com.tadamski.arij.account.activity.AccountSelectorActivity_;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.IssueListActivity_;
import com.tadamski.arij.issue.list.filters.DefaultFilters_;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;
import com.tadamski.arij.util.analytics.Tracker;
import com.tadamski.arij.widget.options.WidgetOptions;

/**
 * Created by t.adamski on 7/12/13.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class HomeScreenWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_LIST_CLICK = "com.tadamski.arij.homescreenwidget.list.click";
    public static final String ACTION_REFRESH = "com.tadamski.arij.homescreenwidget.list.refresh";
    public static final String LIST_ITEM_EXTRA_ISSUE_KEY = "issueKey";

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Tracker.sendEvent("HomescreenWidget", "automatic refresh", null, appWidgetIds.length + 0L);
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
            String issueKey = intent.getStringExtra(LIST_ITEM_EXTRA_ISSUE_KEY);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            WidgetOptions options = new WidgetOptions(context, appWidgetId);
            Filter filter = DefaultFilters_.getInstance_(context).getFilter(options.getFilterId());
            LoginInfo loginInfo = getLoginInfo(context, options.getAccountName());
            TaskStackBuilder.create(context)
                    .addNextIntent(AccountSelectorActivity_.intent(context).get())
                    .addNextIntent(IssueListActivity_.intent(context).loginInfo(loginInfo).selectedFilter(filter).get())
                    .addNextIntent(IssueActivity_.intent(context).loginInfo(loginInfo).issueKey(issueKey).
                            flags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD).get())
                    .startActivities();
            Tracker.sendEvent("HomescreenWidget", "item opened", null, null);
        } else if (intent.getAction().equals(ACTION_REFRESH)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            startRefreshService(context, AppWidgetManager.getInstance(context), appWidgetId);
            Tracker.sendEvent("HomescreenWidget", "manual refresh", null, null);
        } else
            super.onReceive(context, intent);
    }

    LoginInfo getLoginInfo(Context ctx, String accountName) {
        LoginInfoFactory loginInfoFactory = LoginInfoFactory_.getInstance_(ctx);
        return loginInfoFactory.getLoginInfoFromAccountManager(accountName);
    }

    void startRefreshService(Context ctx, AppWidgetManager appWidgetManager, int appWidgetId) {
        WidgetOptions options = new WidgetOptions(ctx, appWidgetId);
        if (options.exists()) {
            RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.homescreen_widget);
            String filterId = options.getFilterId();
            Filter filter = DefaultFilters_.getInstance_(ctx).getFilter(filterId);
            remoteViews.setTextViewText(R.id.filter_name, filter.name);
            remoteViews.setPendingIntentTemplate(R.id.list, createListItemClickPendingIntent(ctx, appWidgetId));
            remoteViews.setOnClickPendingIntent(R.id.homescreen_widget_top_bar, createOpenFilterPendingIntent(ctx, appWidgetId, options, filter));
            remoteViews.setOnClickPendingIntent(R.id.refresh, createRefreshPendingIntent(ctx, appWidgetId));
            remoteViews.setRemoteAdapter(R.id.list, createRemoteAdapterIntent(ctx, appWidgetId));
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private PendingIntent createListItemClickPendingIntent(Context ctx, int appWidgetId) {
        Intent onListClickIntent = new Intent(ctx, HomeScreenWidgetProvider.class);
        onListClickIntent.setAction(HomeScreenWidgetProvider.ACTION_LIST_CLICK);
        onListClickIntent.setData(Uri.parse(onListClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        onListClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(ctx, appWidgetId, onListClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent createOpenFilterPendingIntent(Context ctx, int appWidgetId, WidgetOptions options, Filter filter) {
        Intent accountsIntent = AccountSelectorActivity_.intent(ctx).get();
        accountsIntent.setData(Uri.parse(accountsIntent.toUri(Intent.URI_INTENT_SCHEME)));
        Intent issueListIntent = IssueListActivity_.intent(ctx).loginInfo(getLoginInfo(ctx, options.getAccountName())).selectedFilter(filter)
                .flags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD).get();
        issueListIntent.setData(Uri.parse(issueListIntent.toUri(Intent.URI_INTENT_SCHEME)));
        return TaskStackBuilder.create(ctx)
                .addNextIntent(accountsIntent)
                .addNextIntent(issueListIntent)
                .getPendingIntent(appWidgetId, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent createRemoteAdapterIntent(Context ctx, int appWidgetId) {
        Intent intent = new Intent(ctx, RefreshHomescreenWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        return intent;
    }

    private PendingIntent createRefreshPendingIntent(Context ctx, int appWidgetId) {
        Intent refreshIntent = new Intent(ctx, HomeScreenWidgetProvider.class);
        refreshIntent.setAction(ACTION_REFRESH);
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        refreshIntent.setData(Uri.parse(refreshIntent.toUri(Intent.URI_INTENT_SCHEME)));
        return PendingIntent.getBroadcast(ctx, appWidgetId, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
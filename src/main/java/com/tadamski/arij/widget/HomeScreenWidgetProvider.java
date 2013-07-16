package com.tadamski.arij.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.tadamski.arij.account.LoginInfoFactory;
import com.tadamski.arij.account.LoginInfoFactory_;
import com.tadamski.arij.account.activity.AccountSelectorActivity_;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.IssueListActivity_;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;
import com.tadamski.arij.widget.options.WidgetOptions;

/**
 * Created by t.adamski on 7/12/13.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
            String issueKey = intent.getStringExtra(LIST_ITEM_EXTRA_ISSUE_KEY);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            WidgetOptions options = new WidgetOptions(context, appWidgetId);
            LoginInfo loginInfo = getLoginInfo(context, options.getAccountName());
            TaskStackBuilder.create(context)
                    .addNextIntent(AccountSelectorActivity_.intent(context).get())
                    .addNextIntent(IssueListActivity_.intent(context).loginInfo(loginInfo).get())
                    .addNextIntent(IssueActivity_.intent(context).loginInfo(loginInfo).issueKey(issueKey).get())
                    .startActivities();
        } else
            super.onReceive(context, intent);
    }

    LoginInfo getLoginInfo(Context ctx, String accountName) {
        LoginInfoFactory loginInfoFactory = LoginInfoFactory_.getInstance_(ctx);
        return loginInfoFactory.getLoginInfoFromAccountManager(accountName);
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
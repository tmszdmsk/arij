package com.tadamski.arij.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.actionbarsherlock.R;
import com.tadamski.arij.account.LoginInfoFactory;
import com.tadamski.arij.account.LoginInfoFactory_;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.IssueService_;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.widget.options.WidgetOptions;

/**
 * Created by t.adamski on 7/12/13.
 */
public class HomeScreenWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IssueService issueService = IssueService_.getInstance_(context);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.homescreen_widget);
            Intent intent = new Intent(context, HomescreenWidgetRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //remoteViews.setRemoteAdapter(appWidgetId, intent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context ctx, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        WidgetOptions options = new WidgetOptions(ctx, appWidgetId);
        options.set(newOptions);
        IssuesResultList issues = getIssues(ctx, appWidgetId);
        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.homescreen_widget);
        remoteViews.setTextViewText(R.id.text, issues.getTotal()+"");
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    IssuesResultList getIssues(Context ctx, int appWidgetId) {
        IssueService issueService = IssueService_.getInstance_(ctx);
        WidgetOptions options = new WidgetOptions(ctx, appWidgetId);
        LoginInfo loginInfo = getLoginInfo(ctx, options.getAccountName());
        String filterName = options.getFilterName();
        String filterJql = options.getFilterJql();
        IssuesResultList result = issueService.search(loginInfo, new SearchParams(filterJql, 0, 20));
        return result;
    }

    LoginInfo getLoginInfo(Context ctx, String accountName) {
        LoginInfoFactory loginInfoFactory = LoginInfoFactory_.getInstance_(ctx);
        return loginInfoFactory.getLoginInfoFromAccountManager(accountName);
    }
}

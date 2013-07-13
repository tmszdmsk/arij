package com.tadamski.arij.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.actionbarsherlock.R;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.tadamski.arij.account.LoginInfoFactory;
import com.tadamski.arij.account.LoginInfoFactory_;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.widget.options.WidgetOptions;

/**
 * Created by tmszdmsk on 13.07.13.
 */
@EService
public class RefreshHomescreenWidgetService extends IntentService {

    @Bean
    IssueService issueService;

    public RefreshHomescreenWidgetService() {
        super(RefreshHomescreenWidgetService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -AppWidgetManager.INVALID_APPWIDGET_ID);
        IssuesResultList issues = getIssues(this, appWidgetId);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.homescreen_widget);
        remoteViews.setTextViewText(R.id.text, issues.getTotal() + "");
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    IssuesResultList getIssues(Context ctx, int appWidgetId) {
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

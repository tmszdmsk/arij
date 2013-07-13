package com.tadamski.arij.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.actionbarsherlock.R;
import com.googlecode.androidannotations.annotations.EService;
import com.tadamski.arij.account.LoginInfoFactory;
import com.tadamski.arij.account.LoginInfoFactory_;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueService_;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.widget.options.WidgetOptions;

import java.util.Collections;

/**
 * Created by tmszdmsk on 13.07.13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RefreshHomescreenWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new HomescreenWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private static class HomescreenWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private  Context ctx;
        private  int appWidgetId;
        IssuesResultList issues;

        public HomescreenWidgetRemoteViewsFactory(Context ctx, Intent intent) {
            this.ctx = ctx;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            issues = new IssuesResultList(0, 0, Collections.<Issue>emptyList());
        }

        @Override
        public void onDataSetChanged() {
            issues = getIssues(ctx, appWidgetId);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return issues.getIssues().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.homescreen_widget_elem);
            remoteViews.setTextViewText(R.id.issue_summary, issues.getIssues().get(position).getSummary());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        IssuesResultList getIssues(Context ctx, int appWidgetId) {
            IssueService_ issueService = IssueService_.getInstance_(ctx);
            WidgetOptions options = new WidgetOptions(ctx, appWidgetId);
            LoginInfo loginInfo = getLoginInfo(ctx, options.getAccountName());
            String filterName = options.getFilterName();
            String filterJql = options.getFilterJql();
            IssuesResultList result = issueService.search(loginInfo, new SearchParams(filterJql, 0, 20));
            return result;
        }

        String getFilterName(Context ctx, int appWidgetId) {
             return new WidgetOptions(ctx, appWidgetId).getFilterName();
        }

        LoginInfo getLoginInfo(Context ctx, String accountName) {
            LoginInfoFactory loginInfoFactory = LoginInfoFactory_.getInstance_(ctx);
            return loginInfoFactory.getLoginInfoFromAccountManager(accountName);
        }
    }
}

package com.tadamski.arij.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.net.ConnectivityManagerCompat;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.actionbarsherlock.R;
import com.tadamski.arij.account.LoginInfoFactory;
import com.tadamski.arij.account.LoginInfoFactory_;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.DefaultFilters_;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.resource.IssueService_;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.widget.options.WidgetOptions;

import java.util.Collections;

/**
 * Created by tmszdmsk on 13.07.13.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class RefreshHomescreenWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new HomescreenWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private static class HomescreenWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        IssuesResultList issues;
        private Context ctx;
        private int appWidgetId;

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
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
                    issues = getIssues(ctx, appWidgetId);
            }
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
            Issue issue = issues.getIssues().get(position);
            remoteViews.setTextViewText(R.id.issue_summary, issue.getSummary());
            remoteViews.setTextViewText(R.id.issue_key, issue.getKey());
            remoteViews.setTextViewText(R.id.issue_type, issue.getIssueType().getName());
            remoteViews.setTextViewText(R.id.issue_priority, issue.getPriority().getName());
            remoteViews.setTextViewText(R.id.issue_assignee, issue.getAssignee().getDisplayName());
            Intent intent = new Intent();
            intent.putExtra(HomeScreenWidgetProvider.LIST_ITEM_EXTRA_ISSUE_KEY, issue.getKey());
            remoteViews.setOnClickFillInIntent(R.id.item, intent);
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
            LoginInfo loginInfo = getLoginInfo(ctx, options);
            Filter filter = getFilter(ctx, options);
            IssuesResultList result = issueService.search(loginInfo, new SearchParams(filter.jql, 0, 20));
            return result;
        }

        LoginInfo getLoginInfo(Context ctx, WidgetOptions options) {
            LoginInfoFactory loginInfoFactory = LoginInfoFactory_.getInstance_(ctx);
            return loginInfoFactory.getLoginInfoFromAccountManager(options.getAccountName());
        }

        Filter getFilter(Context ctx, WidgetOptions options) {
            String filterId = options.getFilterId();
            return DefaultFilters_.getInstance_(ctx).getFilter(filterId);
        }

    }
}

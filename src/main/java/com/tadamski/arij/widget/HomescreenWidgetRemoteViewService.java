package com.tadamski.arij.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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
 * Created by tmszdmsk on 12.07.13.
 */
@EService
public class HomescreenWidgetRemoteViewService extends RemoteViewsService {

    @Bean
    IssueService issueService;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetOptions options = new WidgetOptions(this, intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1));
        LoginInfo loginInfo = getLoginInfo(this, options.getAccountName());
        String filterName = options.getFilterName();
        String filterJql = options.getFilterJql();
        IssuesResultList result = issueService.search(loginInfo, new SearchParams(filterJql, 0, 20));
        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                return null;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }

    LoginInfo getLoginInfo(Context ctx, String accountName) {
        LoginInfoFactory loginInfoFactory = LoginInfoFactory_.getInstance_(ctx);
        return loginInfoFactory.getLoginInfoFromAccountManager(accountName);
    }

}

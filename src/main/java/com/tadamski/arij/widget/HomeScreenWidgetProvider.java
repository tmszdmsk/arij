package com.tadamski.arij.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.IssueService_;

/**
 * Created by t.adamski on 7/12/13.
 */
public class HomeScreenWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IssueService issueService = IssueService_.getInstance_(context);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

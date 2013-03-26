/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.worklog.notification;

import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.login.LoginInfo;
import com.tadamski.arij.worklog.activity.NewWorklogActivity;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author tmszdmsk
 */
public class NewWorklogNotificationBuilder {

    private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);

    public static Notification createNotification(Context ctx, Issue issue, Date startDate, LoginInfo loginInfo) {
        Intent intent = new Intent(ctx, NewWorklogActivity.class);
        intent.putExtra(NewWorklogActivity.INTENT_ISSUE, issue);
        intent.putExtra(NewWorklogActivity.INTENT_START_DATE, startDate);
        intent.putExtra(NewWorklogActivity.INTENT_LOGIN_INFO, loginInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification notification =
                new Notification.Builder(ctx).
                setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_menu_agenda)).
                setSmallIcon(R.drawable.ic_menu_agenda).
                setOngoing(true).
                setContentTitle(issue.getSummary().getKey() + ": " + issue.getSummary().getSummary()).
                setAutoCancel(false).
                setContentText("Started at: " + TIME_FORMAT.format(startDate)).
                setContentIntent(PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)).
                getNotification();
        return notification;
    }
}

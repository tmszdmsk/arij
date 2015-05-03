/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue.worklog.newlog.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.worklog.WorkingManager;
import com.tadamski.arij.issue.worklog.newlog.activity.NewWorklogActivity_;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author tmszdmsk
 */
public class NewWorklogNotification {

    private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static int NOTIFICATION_ID = 12366234;
    private static int PENDING_REQUETS_ID = 0;
    private static WorkingManager workingManager;

    public static Intent createIntentForWorklog(Context ctx, Issue issue, Date startDate, LoginInfo loginInfo) {
        Intent intent = NewWorklogActivity_.intent(ctx)
                .issueKey(issue.getKey())
                .loginInfo(loginInfo)
                .startDate(startDate)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS).get();
        return intent;
    }

    public static void create(Context ctx, Issue issue, Date startDate, LoginInfo loginInfo) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = createIntentForWorklog(ctx, issue, startDate, loginInfo);

        Notification notification =
                new NotificationCompat.Builder(ctx).
                        setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_stat_new_worklog)).
                        setSmallIcon(R.drawable.ic_stat_new_worklog).
                        setOngoing(true).
                        setContentTitle(issue.getKey() + ": " + issue.getSummary()).
                        setAutoCancel(false).
                        setContentText("Started at: " + TIME_FORMAT.format(startDate)).
                        setContentIntent(PendingIntent.getActivity(ctx, PENDING_REQUETS_ID++, intent, PendingIntent.FLAG_CANCEL_CURRENT)).
                        setTicker("Work on " + issue.getKey() + " started").
                        build();
        notificationManager.notify(issue.getKey(), NOTIFICATION_ID, notification);

        if (workingManager == null) {
            workingManager = new WorkingManager();
            workingManager.init(ctx);
        }
        workingManager.startWorking(issue.getKey());
    }

    public static void cancel(Context ctx, String issueKey) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(issueKey, NOTIFICATION_ID);

        if (workingManager == null) {
            workingManager = new WorkingManager();
            workingManager.init(ctx);
        }
        workingManager.stopWorking(issueKey);
    }
}

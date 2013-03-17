/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.tadamski.arij.activity.account.AccountSelector;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 *
 * @author tmszdmsk
 */
public class Application extends android.app.Application {
    // uncaught exception handler variable

    private UncaughtExceptionHandler defaultUEH;
    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("com.tadamski.arij", "FATAL EXCEPTION", ex);
            PendingIntent myActivity = PendingIntent.getActivity(Application.this.getBaseContext(), 192837,
                    new Intent(Application.this.getBaseContext(), AccountSelector.class), PendingIntent.FLAG_ONE_SHOT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    15000, myActivity);
            System.exit(2);


            // re-throw critical exception further to the os (important)
            defaultUEH.uncaughtException(thread, ex);
        }
    };

    public Application() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception 
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }
}

package com.tadamski.arij.issue.worklog;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dawid Drozd aka Gelldur on 03.05.15.
 */
public class WorkingManager {

	public void startWorking(Context context, String issueKey) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(".work", Context.MODE_PRIVATE);
		sharedPreferences.edit().putLong(issueKey, System.currentTimeMillis()).apply();
	}

	public void stopWorking(Context context, String issueKey) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(".work", Context.MODE_PRIVATE);
		sharedPreferences.edit().remove(issueKey).apply();
	}

	public long getStartWorking(Context context, String issueKey) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(".work", Context.MODE_PRIVATE);
		return sharedPreferences.getLong(issueKey, 0);
	}

	public boolean isWorking(Context context, String issueKey) {
		return getStartWorking(context, issueKey) > 0;
	}
}

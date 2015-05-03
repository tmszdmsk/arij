package com.tadamski.arij.issue.worklog;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dawid Drozd aka Gelldur on 03.05.15.
 */
public class WorkingManager {

	public void startWorking(String issueKey) {
		sharedPreferences.edit().putLong(issueKey, System.currentTimeMillis()).apply();
	}

	public void stopWorking(String issueKey) {
		sharedPreferences.edit().remove(issueKey).apply();
	}

	public long getStartWorking(String issueKey) {
		return sharedPreferences.getLong(issueKey, 0);
	}

	public boolean isWorking(String issueKey) {
		return getStartWorking(issueKey) > 0;
	}

	public void init(Context context) {
		sharedPreferences = context.getSharedPreferences(".work", Context.MODE_PRIVATE);

	}

	private SharedPreferences sharedPreferences;
}

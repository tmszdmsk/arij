package com.tadamski.arij.issue.activity.list;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.google.analytics.tracking.android.EasyTracker;
import com.tadamski.arij.issue.activity.IssueActivity;
import com.tadamski.arij.issue.dao.Issue.Summary;
import com.tadamski.arij.issue.dao.IssueDAO;
import roboguice.activity.RoboListActivity;

import javax.inject.Inject;

public class IssueListActivity extends RoboListActivity {

    private final String TAG = IssueListActivity.class.getName();
    @Inject
    private IssueDAO issueDao;

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setProgressBarIndeterminate(true);
        Log.i(TAG, "onCreate");
        AsyncTask<Void, Void, BaseAdapter> asyncTask = new AsyncTask<Void, Void, BaseAdapter>() {
            @Override
            protected BaseAdapter doInBackground(Void... params) {
                return new IssueListAdapter(IssueListActivity.this, issueDao.getTasksAssignedToLoggedInUser());
            }

            @Override
            protected void onPostExecute(BaseAdapter result) {
                IssueListActivity.this.setProgressBarIndeterminate(false);
                IssueListActivity.this.setListAdapter(result);
            }

        };
        asyncTask.execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(IssueListActivity.this, IssueActivity.class);
        intent.putExtra(IssueActivity.INTENT_EXTRA_ISSUE_KEY, ((Summary) getListAdapter().getItem(position)).getKey());
        startActivity(intent);
    }
}

package com.tadamski.arij.activity.issuelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import com.tadamski.arij.activity.issue.IssueActivity;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.issue.Issue.Summary;
import com.tadamski.arij.issue.IssueDAO;
import com.tadamski.arij.util.Callback;
import roboguice.activity.RoboListActivity;

import javax.inject.Inject;

public class IssueListActivity extends RoboListActivity {

    private final String TAG = IssueListActivity.class.getName();
    @Inject
    private IssueDAO issueDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setProgressBarIndeterminate(true);
        Log.i(TAG, "onCreate");
        AsyncTask<Void, Void, BaseAdapter> asyncTask = new AsyncTask<Void, Void, BaseAdapter>() {
            @Override
            protected BaseAdapter doInBackground(Void... params) {
                return new IssueListAdapter(IssueListActivity.this, issueDao.getTasksAssignedToLoggedInUser(), new OpenIssueAction());
            }

            @Override
            protected void onPostExecute(BaseAdapter result) {
                IssueListActivity.this.setProgressBarIndeterminate(false);
                IssueListActivity.this.setListAdapter(result);
            }

        };
        asyncTask.execute();
    }

    private class OpenIssueAction implements Callback<Issue.Summary> {

        @Override
        public void call(Summary issueSummary) {
            Intent intent = new Intent(IssueListActivity.this, IssueActivity.class);
            intent.putExtra(IssueActivity.INTENT_EXTRA_ISSUE_KEY, issueSummary.getKey());
            startActivity(intent);
        }
    }
}

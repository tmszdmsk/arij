/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.activity.issue;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tadamski.arij.R;
import com.tadamski.arij.activity.issue.properties.model.IssueProperty;
import com.tadamski.arij.activity.issue.properties.model.IssuePropertyGroup;
import com.tadamski.arij.activity.issue.properties.view.IssuePropertyGroupViewFactory;
import com.tadamski.arij.worklog.repository.NewWorklog;
import com.tadamski.arij.worklog.repository.WorkLogRepository;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.issue.IssueDAO;
import com.tadamski.arij.login.CredentialsService;
import com.tadamski.arij.worklog.notification.NewWorklogNotificationBuilder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.inject.Inject;
import roboguice.fragment.RoboFragment;

/**
 *
 * @author tmszdmsk
 */
public class IssueFragment extends RoboFragment implements LoaderCallbacks<Issue> {

    @Inject
    private IssueDAO issueDAO;
    @Inject
    private WorkLogRepository workLogRepository;
    @Inject
    private NotificationManager notificationManager;
    @Inject
    private CredentialsService credentialsService;
    private static final String TAG = IssueFragment.class.getName();

    public void loadIssue(String issueKey) {
        Bundle issueKeyBundle = new Bundle();
        issueKeyBundle.putString("issueKey", issueKey);
        getLoaderManager().restartLoader(0, issueKeyBundle, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.issue_fragment, container);
    }

    @Override
    public Loader<Issue> onCreateLoader(int i, Bundle bundle) {
        final String issueKey = bundle.getString("issueKey");
        Log.d(TAG, "loader creation, issueKey: " + issueKey);
        final AsyncTaskLoader<Issue> asyncTaskLoader = new AsyncTaskLoader<Issue>(getActivity()) {
            @Override
            public Issue loadInBackground() {
                Log.d(TAG, "loader called");
                return issueDAO.getIssueWithKey(issueKey);
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<Issue> loader, final Issue issue) {
        Log.d(TAG, "loader finished");
        IssuePropertyGroup basicGroup = getIssueDetailsProperties(issue);
        IssuePropertyGroup peopleGroup = getIssuePeopleProperties(issue);
        IssuePropertyGroup datesGroup = getIssueDateProperties(issue);
        LinearLayout view = (LinearLayout) this.getView().findViewWithTag("layout");
        final TextView description = new TextView(getActivity());
        description.setText(issue.getSummary().getDescription());
        final IssuePropertyGroupViewFactory viewFactory = new IssuePropertyGroupViewFactory();
        view.addView(viewFactory.createSingleTextView("Summary", issue.getSummary().getSummary(), getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createSingleTextView("Description", issue.getSummary().getDescription(), getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(basicGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(peopleGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(datesGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Button workLogButton = new Button(getActivity());
        workLogButton.setText("start work");
        workLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.notify(0, NewWorklogNotificationBuilder.createNotification(IssueFragment.this.getActivity().getApplicationContext(), issue, new Date(), credentialsService.getActive()));
            }
            
        });
        view.addView(workLogButton,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (getActivity() instanceof IssueLoadedListener) {
            ((IssueLoadedListener) getActivity()).issueLoaded(issue);
        }
    }

    @Override
    public void onLoaderReset(Loader<Issue> loader) {
        Log.d(TAG, "loader reset");
    }

    private IssuePropertyGroup getIssueDetailsProperties(Issue issue) {
        final ArrayList<IssueProperty> basicProperties = new ArrayList<IssueProperty>();
        issue.getSummary().getType();
        basicProperties.add(new IssueProperty("type", "Type", issue.getSummary().getType().getName(), null));
        basicProperties.add(new IssueProperty("priority", "Priority", issue.getSummary().getPriority().getName(), null));
//        basicProperties.add(new IssueProperty("labels", "Labels", "retrieve labels", null));
        basicProperties.add(new IssueProperty("status", "Status", issue.getSummary().getStatus().getName(), null));
        final Issue.Resolution resolution = issue.getSummary().getResolution();
        basicProperties.add(new IssueProperty("resolution", "Resolution", resolution == null ? "Unresolved" : resolution.getName(), null));
        IssuePropertyGroup basicGroup = new IssuePropertyGroup(basicProperties, "Details");
        return basicGroup;
    }

    private IssuePropertyGroup getIssuePeopleProperties(Issue issue) {
        final ArrayList<IssueProperty> people = new ArrayList<IssueProperty>();
        issue.getSummary().getType();
        people.add(new IssueProperty("assignee", "Assignee", issue.getSummary().getAssignee().getDisplayName(), null));
        people.add(new IssueProperty("reporter", "Reporter", issue.getSummary().getAssignee().getDisplayName(), null));
        IssuePropertyGroup peopleGroup = new IssuePropertyGroup(people, "People");
        return peopleGroup;
    }

    private IssuePropertyGroup getIssueDateProperties(Issue issue) {
        DateFormat df = DateFormat.getDateTimeInstance();
        final ArrayList<IssueProperty> dates = new ArrayList<IssueProperty>();
        issue.getSummary().getType();
        dates.add(new IssueProperty("created", "Created", df.format(issue.getSummary().getCreated()), null));
        dates.add(new IssueProperty("updated", "Updated", df.format(issue.getSummary().getUpdated()), null));
        if (issue.getSummary().getResolutionDate() != null) {
            dates.add(new IssueProperty("resolved", "Resolved", df.format(issue.getSummary().getResolutionDate()), null));
        }
        IssuePropertyGroup datesGroup = new IssuePropertyGroup(dates, "Dates");
        return datesGroup;
    }

    public interface IssueLoadedListener {

        public void issueLoaded(Issue issue);
    }
}

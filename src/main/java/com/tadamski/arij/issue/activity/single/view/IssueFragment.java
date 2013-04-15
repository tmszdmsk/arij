/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue.activity.single.view;

import android.app.NotificationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.CredentialsService;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.activity.properties.model.IssueProperty;
import com.tadamski.arij.issue.activity.properties.model.IssuePropertyGroup;
import com.tadamski.arij.issue.activity.properties.view.IssuePropertyGroupViewFactory;
import com.tadamski.arij.issue.dao.Issue;
import com.tadamski.arij.issue.dao.IssueDAO;
import com.tadamski.arij.worklog.notification.NewWorklogNotificationBuilder;
import com.tadamski.arij.worklog.repository.WorkLogRepository;
import roboguice.fragment.RoboFragment;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author tmszdmsk
 */
@EFragment(R.layout.issue_fragment)
public class IssueFragment extends RoboFragment {

    private static final String TAG = IssueFragment.class.getName();
    @Inject
    private IssueDAO issueDAO;
    @Inject
    private WorkLogRepository workLogRepository;
    @Inject
    private NotificationManager notificationManager;
    @Inject
    private CredentialsService credentialsService;
    @InstanceState
    Issue loadedIssue;

    @AfterViews
    void init() {
        if (loadedIssue != null) {
            onLoadFinished(loadedIssue);
        }
    }

    public void loadIssue(String issueKey, LoginInfo account) {
        loadIssueInBackground(issueKey, account);
    }

    @Background
    void loadIssueInBackground(String issueKey, LoginInfo account) {
        loadedIssue = issueDAO.getIssueWithKey(issueKey, account);
        onLoadFinished(loadedIssue);
    }

    @UiThread
    void onLoadFinished(final Issue issue) {
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
                NewWorklogNotificationBuilder.createNotification(IssueFragment.this.getActivity().getApplicationContext(), issue, new Date(), credentialsService.getActive());
            }

        });
        view.addView(workLogButton,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (getActivity() instanceof IssueLoadedListener) {
            ((IssueLoadedListener) getActivity()).issueLoaded(issue);
        }
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

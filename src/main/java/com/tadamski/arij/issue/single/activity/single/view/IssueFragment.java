/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue.single.activity.single.view;

import android.app.NotificationManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.UiThread;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.model.Resolution;
import com.tadamski.arij.issue.single.activity.properties.model.IssueProperty;
import com.tadamski.arij.issue.single.activity.properties.model.IssuePropertyGroup;
import com.tadamski.arij.issue.single.activity.properties.view.IssuePropertyGroupViewFactory;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * @author tmszdmsk
 */
@EFragment(R.layout.issue_fragment)
public class IssueFragment extends SherlockFragment {

    private static final String TAG = IssueFragment.class.getName();
    @Bean
    IssueService issueService;
    @SystemService
    NotificationManager notificationManager;
    LoginInfo actualLoginInfo;

    public void loadIssue(String issueKey, LoginInfo loginInfo) {
        this.actualLoginInfo = loginInfo;
        loadIssueInBackground(issueKey, loginInfo);
    }

    @Background
    void loadIssueInBackground(String issueKey, LoginInfo loginInfo) {
        Issue loadedIssue = issueService.getIssue(loginInfo, issueKey);
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
        description.setText(issue.getDescription());
        final IssuePropertyGroupViewFactory viewFactory = new IssuePropertyGroupViewFactory();
        view.addView(viewFactory.createSingleTextView("Summary", issue.getSummary(), getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createSingleTextView("Description", issue.getDescription(), getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(basicGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(peopleGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(datesGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (getActivity() instanceof IssueLoadedListener) {
            ((IssueLoadedListener) getActivity()).issueLoaded(issue);
        }
    }

    private IssuePropertyGroup getIssueDetailsProperties(Issue issue) {
        final ArrayList<IssueProperty> basicProperties = new ArrayList<IssueProperty>();
        basicProperties.add(new IssueProperty("type", "Type", issue.getIssueType().getName(), null));
        basicProperties.add(new IssueProperty("priority", "Priority", issue.getPriority().getName(), null));
        basicProperties.add(new IssueProperty("status", "Status", issue.getStatus().getName(), null));
        Resolution resolution = issue.getResolution();
        basicProperties.add(new IssueProperty("resolution", "Resolution", resolution == null ? "Unresolved" : resolution.getName(), null));
        IssuePropertyGroup basicGroup = new IssuePropertyGroup(basicProperties, "Details");
        return basicGroup;
    }

    private IssuePropertyGroup getIssuePeopleProperties(Issue issue) {
        final ArrayList<IssueProperty> people = new ArrayList<IssueProperty>();
        people.add(new IssueProperty("assignee", "Assignee", issue.getAssignee().getDisplayName(), null));
        people.add(new IssueProperty("reporter", "Reporter", issue.getAssignee().getDisplayName(), null));
        IssuePropertyGroup peopleGroup = new IssuePropertyGroup(people, "People");
        return peopleGroup;
    }

    private IssuePropertyGroup getIssueDateProperties(Issue issue) {
        DateFormat df = DateFormat.getDateTimeInstance();
        final ArrayList<IssueProperty> dates = new ArrayList<IssueProperty>();
        dates.add(new IssueProperty("created", "Created", df.format(issue.getCreated()), null));
        dates.add(new IssueProperty("updated", "Updated", df.format(issue.getUpdated()), null));
        if (issue.getResolutionDate() != null) {
            dates.add(new IssueProperty("resolved", "Resolved", df.format(issue.getResolutionDate()), null));
        }
        IssuePropertyGroup datesGroup = new IssuePropertyGroup(dates, "Dates");
        return datesGroup;
    }

    public interface IssueLoadedListener {
        public void issueLoaded(Issue issue);
    }
}

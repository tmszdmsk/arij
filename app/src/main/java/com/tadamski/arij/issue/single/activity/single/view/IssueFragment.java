/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tadamski.arij.issue.single.activity.single.view;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.activity.CommentsActivity;
import com.tadamski.arij.issue.comments.activity.CommentsActivity_;
import com.tadamski.arij.issue.comments.lastcommentview.LastCommentView;
import com.tadamski.arij.issue.comments.lastcommentview.LastCommentView_;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.model.Resolution;
import com.tadamski.arij.issue.resource.model.User;
import com.tadamski.arij.issue.resource.model.updates.ChangeAssigneeUpdate;
import com.tadamski.arij.issue.single.activity.properties.model.IssueProperty;
import com.tadamski.arij.issue.single.activity.properties.model.IssuePropertyGroup;
import com.tadamski.arij.issue.single.activity.properties.view.IssuePropertyGroupViewFactory;
import com.tadamski.arij.issue.worklog.list.WorklogsActivity;
import com.tadamski.arij.issue.worklog.list.WorklogsActivity_;
import com.tadamski.arij.issue.worklog.newlog.notification.NewWorklogNotification;
import com.tadamski.arij.issue.worklog.timetracking.TimeTrackingSummaryView;
import com.tadamski.arij.issue.worklog.timetracking.TimeTrackingSummaryView_;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author tmszdmsk
 */
@EFragment(R.layout.issue_fragment)
public class IssueFragment extends Fragment {

    private static final String TAG = IssueFragment.class.getName();
    @Bean
    IssueService issueService;
    @SystemService
    NotificationManager notificationManager;
    @ViewById(R.id.loading)
    View loadingIndicator;
    private LoginInfo actualLoginInfo;
    private String actualIssueKey;
    private Issue loadedIssue;
    private View view;
    private IssueFragmentListener issueFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        setMenuVisibility(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (loadedIssue != null) {
            onLoadFinished(loadedIssue);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.issue_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadIssue(String issueKey, LoginInfo loginInfo) {
        this.loadedIssue = null;
        this.actualIssueKey = issueKey;
        this.actualLoginInfo = loginInfo;
        enableLoadingIndicator();
        loadIssueInBackground(issueKey, loginInfo);
    }

    @Background
    void loadIssueInBackground(String issueKey, LoginInfo loginInfo) {
        Issue loadedIssue = issueService.getIssue(loginInfo, issueKey);
        onLoadFinished(loadedIssue);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IssueFragmentListener) {
            this.issueFragmentListener = (IssueFragmentListener) activity;
        } else {
            throw new IllegalArgumentException("activity must implement " + IssueFragmentListener.class.getName());
        }
    }

    @UiThread
    void onLoadFinished(final Issue issue) {
        loadedIssue = issue;
        setMenuVisibility(true);
        disableLoadingIndicator();
        Log.d(TAG, "loader finished");

        IssuePropertyGroup basicGroup = getIssueDetailsProperties(issue);
        IssuePropertyGroup peopleGroup = getIssuePeopleProperties(issue);
        IssuePropertyGroup datesGroup = getIssueDateProperties(issue);
        LinearLayout view = (LinearLayout) this.getView().findViewWithTag("layout");
        view.removeAllViews();
        final TextView description = new TextView(getActivity());
        description.setText(issue.getDescription());
        final IssuePropertyGroupViewFactory viewFactory = new IssuePropertyGroupViewFactory();
        view.addView(viewFactory.createSingleTextView("Summary", issue.getSummary(), getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createSingleTextView("Description", issue.getDescription(), getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TimeTrackingSummaryView timeTrackingSummaryView = TimeTrackingSummaryView_.build(getActivity());
        timeTrackingSummaryView.setTimeTracking(issue.getTimeTracking());
        view.addView(viewFactory.createCustomViewGroup("Time tracking", timeTrackingSummaryView, getActivity()));
        view.addView(viewFactory.createMultipropertiesView(basicGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(peopleGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.addView(viewFactory.createMultipropertiesView(datesGroup, getActivity()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LastCommentView lastCommentsView = LastCommentView_.build(getActivity());
        lastCommentsView.setCommentsList(issue.getComments());
        lastCommentsView.setShowCommentsCallback(new LastCommentView.ShowCommentsCallback() {
            @Override
            public void onShowComments() {
                onCommentsClicked();
            }
        });
        view.addView(viewFactory.createCustomViewGroup("Comments", lastCommentsView, getActivity()));
        issueFragmentListener.issueLoaded(issue);
    }

    void onCommentsClicked() {
        CommentsActivity_.intent(getActivity())
                .issueKey(actualIssueKey).loginInfo(actualLoginInfo)
                .commentsList(loadedIssue == null ? null : loadedIssue.getComments())
                .startForResult(CommentsActivity.REQUEST_SHOW_COMMENTS);
    }

    @OptionsItem(R.id.menu_item_worklog)
    void onWorklogClicked() {
        WorklogsActivity_.intent(getActivity())
                .issueKey(actualIssueKey)
                .loginInfo(actualLoginInfo)
                .worklogList(loadedIssue == null ? null : loadedIssue.getWorklog())
                .startForResult(WorklogsActivity.REQUEST_WORKLOG);
    }

    @OptionsItem(R.id.menu_item_start_work)
    void onStartWorkClicked() {
        Tracker.sendEvent("IssueFragment", "startWorkClicked", null, null);
        if (loadedIssue != null)
            NewWorklogNotification.create(getActivity().getApplicationContext(), loadedIssue, new Date(), actualLoginInfo);
    }

    @OptionsItem(R.id.menu_item_assign_to_me)
    void onAssignToMeClicked() {
        Tracker.sendEvent("IssueFragment", "assignToMeClicked", null, null);
        enableLoadingIndicator();
        assignToMeAsync();
    }

    @Background
    void assignToMeAsync() {
        issueService.updateIssue(actualLoginInfo, actualIssueKey, new ChangeAssigneeUpdate(new User(actualLoginInfo.getUsername())));
        loadIssue(actualIssueKey, actualLoginInfo);
        disableLoadingIndicator();
    }

    @UiThread
    void disableLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @UiThread
    void enableLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
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
        people.add(new IssueProperty("reporter", "Reporter", issue.getReporter().getDisplayName(), null));
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

    public interface IssueFragmentListener {
        public void issueLoaded(Issue issue);
    }
}

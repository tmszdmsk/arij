package com.tadamski.arij.issue.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.drawer.IssueListDrawerFragment;
import com.tadamski.arij.issue.list.drawer.QuickSearch;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;
import com.tadamski.arij.util.analytics.Tracker;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends SherlockFragmentActivity implements IssueListFragment.Listener, IssueListDrawerFragment.Listener {

    @FragmentById(R.id.fragment)
    IssueListFragment issueListFragment;
    @FragmentById(R.id.drawer_fragment)
    IssueListDrawerFragment drawerFragment;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @ViewById(R.id.drawer)
    View drawer;
    ActionBarDrawerToggle drawerToggle;
    @Bean
    IssueService issueService;
    @Extra
    LoginInfo loginInfo;
    @Extra
    Filter selectedFilter;
    @InstanceState
    Filter instanceFilter;
    @InstanceState
    String instanceQuery;

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.activityStart(this);
        initSelectedFilter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tracker.activityStop(this);
    }

    @OptionsItem(android.R.id.home)
    void drawer() {
        if (drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawer(drawer);
        } else {
            drawerLayout.openDrawer(drawer);
        }
    }

    @AfterViews
    void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer,
                R.string.open, R.string.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    void initSelectedFilter() {
        if (instanceFilter != null) {
            initWithFilter(instanceFilter);
        } else if (instanceQuery != null) {
            drawerFragment.selectQuery(instanceQuery);
            //TODO: drop it?
        } else if (selectedFilter != null) {
            initWithFilter(selectedFilter);
        } else {
            Filter defaultFilter = drawerFragment.getDefaultFilter();
            initWithFilter(defaultFilter);
        }
    }

    private void initWithFilter(Filter filter) {
        drawerFragment.selectFilter(filter);
        issueListFragment.executeFilter(filter.jql, loginInfo);
        getSupportActionBar().setTitle(filter.name);
    }

    @Override
    public void onIssueElementClick(Issue issue) {
        IssueActivity_.intent(this).issueKey(issue.getKey()).loginInfo(loginInfo).start();
    }

    @Override
    public void onQuickSearch(String query) {
        this.instanceFilter = null;
        this.instanceQuery = query;
        doUglyQuerySearch(query);
        drawerLayout.closeDrawer(drawer);
    }

    @Background
    void doUglyQuerySearch(String query) {
        QuickSearch quickSearch = new QuickSearch();
        QuickSearch.Action quickSearchAction = quickSearch.getJql(query, loginInfo);
        afterUglyQuerySearch(query, quickSearchAction);
    }

    @UiThread
    void afterUglyQuerySearch(String query, QuickSearch.Action quickSearchAction) {
        handleQuickSearchAction(quickSearchAction);
        getSupportActionBar().setTitle(query);
    }

    private void handleQuickSearchAction(QuickSearch.Action quickSearchAction) {
        if (quickSearchAction instanceof QuickSearch.DoJQL) {
            QuickSearch.DoJQL jqlAction = (QuickSearch.DoJQL) quickSearchAction;
            issueListFragment.executeFilter(jqlAction.jql, loginInfo);
        } else if (quickSearchAction instanceof QuickSearch.OpenIssue) {
            QuickSearch.OpenIssue openIssueAction = (QuickSearch.OpenIssue) quickSearchAction;
            IssueActivity_.intent(this).issueKey(openIssueAction.issueKey).loginInfo(loginInfo).start();
        } else {
            throw new IllegalArgumentException("cannot handle that type of quick search action: " + quickSearchAction.getClass());
        }
    }

    @Override
    public void onFilterSelected(Filter filter) {
        this.instanceQuery = null;
        this.instanceFilter = filter;
        issueListFragment.executeFilter(filter.jql, loginInfo);
        getSupportActionBar().setTitle(filter.name);
        drawerLayout.closeDrawer(drawer);
    }
}

package com.tadamski.arij.issue.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.drawer.IssueListDrawerFragment;
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
            executeQuery(instanceQuery);
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
        executeQuery(query);
        drawerLayout.closeDrawer(drawer);
    }

    private void executeQuery(String query) {
        issueListFragment.executeFilter("text ~ \"" + escapeQuery(query) + "\"", loginInfo);
        getSupportActionBar().setTitle(getString(R.string.quick_search_activity_title_prefix) + query);
    }

    private String escapeQuery(String query) {
        return query.replaceAll("\"", "\\\\\\\\\\\\\"");
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

package com.tadamski.arij.issue.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.drawer.IssueListDrawerFragment;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends FragmentActivity implements IssueListFragment.Listener, IssueListDrawerFragment.Listener {

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    void initSelectedFilter() {
        if (instanceFilter != null) {
            initWithFilter(instanceFilter);
        } else if (instanceQuery != null) {
            drawerFragment.selectQuery(instanceQuery);
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
        getActionBar().setTitle(filter.name);
    }

    @Override
    public void onIssueElementClick(Issue issue) {
        IssueActivity_.intent(this).issueKey(issue.getKey()).loginInfo(loginInfo).start();
    }

    @Override
    public void onJql(String jql) {
        getActionBar().setSubtitle(jql);
    }

    @Override
    public void onQuickSearch(String query) {
        this.instanceFilter = null;
        this.instanceQuery = query;
        issueListFragment.quickSearch(query, loginInfo);
        drawerLayout.closeDrawer(drawer);
        getActionBar().setTitle(query);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        this.instanceQuery = null;
        this.instanceFilter = filter;
        issueListFragment.executeFilter(filter.jql, loginInfo);
        getActionBar().setTitle(filter.name);
        drawerLayout.closeDrawer(drawer);
    }
}

package com.tadamski.arij.issue.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.list.filters.FiltersListAdapter;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.util.analytics.Tracker;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends SherlockFragmentActivity {

    @FragmentById(R.id.fragment)
    IssueListFragment fragment;
    @ViewById(R.id.drawer)
    ListView filtersListView;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bean
    IssueService issueService;
    @Extra
    LoginInfo loginInfo;
    @Extra
    Filter selectedFilter;
    @InstanceState
    Filter filterFromInstance;
    ActionBarDrawerToggle drawerToggle;
    DefaultFilters filters = new DefaultFilters();

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tracker.activityStop(this);
    }

    @OptionsItem(android.R.id.home)
    void drawer() {
        if (drawerLayout.isDrawerOpen(filtersListView)) {
            drawerLayout.closeDrawer(filtersListView);
        } else {
            drawerLayout.openDrawer(filtersListView);
        }
    }

    @AfterViews
    void initDrawer() {
        final FiltersListAdapter filtersListAdapter = new FiltersListAdapter(this, filters.getFilterList());
        filtersListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        filtersListView.setAdapter(filtersListAdapter);
        filtersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Filter filter = (Filter) adapterView.getItemAtPosition(i);
                Tracker.sendEvent("Filters", "filterSelected", filter.name, null);
                loadFilterInFragment(filter);
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer,
                R.string.open, R.string.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
        if (filterFromInstance != null) {
            selectedFilter = filterFromInstance;
        } else if (selectedFilter == null) {
            selectedFilter = filters.getFilterList().get(0);
        }
        loadFilterInFragment(selectedFilter);
    }

    void loadFilterInFragment(Filter selectedFilter) {
        fragment.executeFilter(selectedFilter, loginInfo);
        setActivityPropertiesFromFilter(selectedFilter);
    }

    void setActivityPropertiesFromFilter(Filter selectedFilter) {
        getSupportActionBar().setTitle(selectedFilter.name);
        filtersListView.setItemChecked(getFilterPosition(selectedFilter), true);
        drawerLayout.closeDrawer(filtersListView);
    }

    Integer getFilterPosition(Filter filter) {
        for (int i = 0; i < filtersListView.getAdapter().getCount(); i++) {
            if (filter.equals(filtersListView.getAdapter().getItem(i))) {
                return i;
            }
        }
        return null;
    }


}

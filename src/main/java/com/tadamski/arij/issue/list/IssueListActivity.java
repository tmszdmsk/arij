package com.tadamski.arij.issue.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.list.filters.FiltersListAdapter;
import com.tadamski.arij.issue.resource.IssueService;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends SherlockFragmentActivity {

    private final String TAG = IssueListActivity.class.getName();
    @FragmentById(R.id.fragment)
    IssueListFragment fragment;
    @ViewById(R.id.drawer)
    ListView filtersListView;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @NonConfigurationInstance
    int selectedFilterPosition = -1;
    @Bean
    IssueService issueService;
    @Extra
    LoginInfo loginInfo;
    ActionBarDrawerToggle drawerToggle;
    DefaultFilters filters = new DefaultFilters();

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
        filtersListView.setAdapter(filtersListAdapter);
        filtersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                filtersListView.setItemChecked(i, true);
                loadFilterInFragment(i);
                setActivityPropertiesFromFilter(i);
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer,
                R.string.open, R.string.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
        if (selectedFilterPosition == -1) {
            loadFilterInFragment(0);
        }
        setActivityPropertiesFromFilter(selectedFilterPosition);
    }

    void loadFilterInFragment(int position) {
        selectedFilterPosition = position;
        Filter filter = (Filter) filtersListView.getItemAtPosition(position);
        fragment.executeJql(filter.jql, loginInfo);
    }

    void setActivityPropertiesFromFilter(int position) {
        Filter filter = (Filter) filtersListView.getItemAtPosition(position);
        getSupportActionBar().setTitle(filter.name);
        filtersListView.setItemChecked(position, true);
        drawerLayout.closeDrawer(filtersListView);
    }


}

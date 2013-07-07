package com.tadamski.arij.issue.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.list.filters.FiltersListAdapter;
import com.tadamski.arij.issue.resource.IssueDAO;

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
    boolean loaded;
    @Bean
    IssueDAO issueDao;
    ActionBarDrawerToggle drawerToggle;
    @Extra
    LoginInfo loginInfo;
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
    void initFragment() {
        if (!loaded) {
            final FiltersListAdapter filtersListAdapter = new FiltersListAdapter(this, filters.getFilterList());
            filtersListView.setAdapter(filtersListAdapter);
            filtersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    filtersListView.setItemChecked(i, true);
                    selectFilter(filtersListAdapter.getItem(i));
                }
            });
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    R.drawable.ic_drawer,
                    R.string.open, R.string.close);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            drawerToggle.syncState();
            filtersListView.setItemChecked(0, true);
            selectFilter(filtersListAdapter.getItem(0));
            loaded = true;
        }
    }

    void selectFilter(Filter filter) {
        fragment.executeJql(filter.jql, loginInfo);
        IssueListActivity.this.setTitle(filter.name);
        drawerLayout.closeDrawer(filtersListView);
    }


}

package com.tadamski.arij.issue.activity.list;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.dao.IssueDAO;

@EActivity(R.layout.issue_list_activity)
public class IssueListActivity extends SherlockFragmentActivity {

    private final String TAG = IssueListActivity.class.getName();
    @FragmentById(R.id.fragment)
    IssueListFragment fragment;
    @ViewById(R.id.drawer)
    ListView drawerListView;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @NonConfigurationInstance
    boolean loaded;
    @Bean
    IssueDAO issueDao;
    @Bean
    CredentialsService service;
    ActionBarDrawerToggle drawerToggle;
    @Extra
    LoginInfo loginInfo;

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

    @AfterViews
    void initFragment() {
        if (!loaded) {
            fragment.executeJql("assignee=currentUser()", loginInfo);
            loaded = true;
        }
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"ABC", "DEF"}));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer,
                R.string.open, R.string.close);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }


}

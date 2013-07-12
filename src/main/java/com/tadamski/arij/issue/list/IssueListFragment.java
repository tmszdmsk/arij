package com.tadamski.arij.issue.list;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.single.activity.single.view.IssueActivity_;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 09.04.13
 * Time: 20:09
 * To change this template use File | Settings | File Templates.
 */
@EFragment(R.layout.issue_list_fragment)
public class IssueListFragment extends SherlockListFragment {


    @Bean
    IssueService issueService;
    Filter actualFilter;
    private LoginInfo actualLoginInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void executeFilter(Filter filter, LoginInfo loginInfo) {
        if (!filter.equals(this.actualFilter) || !loginInfo.equals(this.actualLoginInfo)) {
            this.actualFilter = filter;
            this.actualLoginInfo = loginInfo;
            IssueListAdapter adapter = new IssueListAdapter(getActivity(), new ArrayList<Issue>(), 1, filter.jql);
            ListAdapter issueListAdapter = new EndlessIssueListAdapter(issueService, getActivity(), adapter, loginInfo);
            setListAdapter(issueListAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Issue item = (Issue) getListAdapter().getItem(position);
        IssueActivity_.intent(getActivity()).issueKey(item.getKey()).loginInfo(actualLoginInfo).start();
    }
}

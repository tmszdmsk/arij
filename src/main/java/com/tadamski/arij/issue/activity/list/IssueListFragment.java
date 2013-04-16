package com.tadamski.arij.issue.activity.list;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.activity.single.view.IssueActivity_;
import com.tadamski.arij.issue.dao.Issue;
import com.tadamski.arij.issue.dao.IssueDAO;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 09.04.13
 * Time: 20:09
 * To change this template use File | Settings | File Templates.
 */
@EFragment(R.layout.issue_list_fragment)
public class IssueListFragment extends RoboFragment implements AdapterView.OnItemClickListener {

    @ViewById(android.R.id.list)
    ListView listView;
    @Inject
    IssueDAO issueDAO;
    ListAdapter issueListAdapter;
    private LoginInfo account;

    @AfterViews
    void initClickListener() {
        listView.setOnItemClickListener(this);
    }

    public void executeJql(String jql, LoginInfo account) {
        IssueListAdapter adapter = new IssueListAdapter(getActivity(), new ArrayList<Issue.Summary>(), 1, jql);
        issueListAdapter = new EndlessIssueListAdapter(issueDAO, getActivity(), adapter, account);
        listView.setAdapter(issueListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Issue.Summary item = (Issue.Summary) issueListAdapter.getItem(position);
        IssueActivity_.intent(getActivity()).issueKey(item.getKey()).account(account).start();
    }
}

package com.tadamski.arij.issue.activity.list;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.activity.single.view.IssueActivity_;
import com.tadamski.arij.issue.dao.Issue;
import com.tadamski.arij.issue.dao.IssueDAO;
import roboguice.fragment.RoboFragment;

import java.util.List;

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
    IssueListAdapter issueListAdapter;
    private LoginInfo account;

    @AfterViews
    void initListAdapter() {
        issueListAdapter = new IssueListAdapter(getActivity());
        listView.setAdapter(issueListAdapter);
    }

    @AfterViews
    void initClickListener() {
        listView.setOnItemClickListener(this);
    }

    public void executeJql(String jql, LoginInfo account) {
        this.account = account;
        issueListAdapter.getIssues().clear();
        issueListAdapter.notifyDataSetChanged();
        loadInBackground(jql, account);
    }

    @Background
    void loadInBackground(String jql, LoginInfo account) {
        List<Issue.Summary> result = issueDAO.executeJql(jql, 0L, 20L, account);
        onLoadSuccess(result);
    }

    @UiThread
    void onLoadSuccess(List<Issue.Summary> issues) {
        issueListAdapter.getIssues().addAll(issues);
        issueListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Issue.Summary item = issueListAdapter.getItem(position);
        IssueActivity_.intent(getActivity()).issueKey(item.getKey()).account(account).start();
    }
}

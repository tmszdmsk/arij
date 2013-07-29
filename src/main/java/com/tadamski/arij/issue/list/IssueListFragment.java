package com.tadamski.arij.issue.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.model.Issue;

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
    String actualJql;
    Listener listener;
    private LoginInfo actualLoginInfo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            listener = (Listener) activity;
        } else {
            throw new IllegalArgumentException("Activity has to implement Listener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void executeFilter(String jql, LoginInfo loginInfo) {
        if (!jql.equals(this.actualJql) || !loginInfo.equals(this.actualLoginInfo)) {
            this.actualJql = jql;
            this.actualLoginInfo = loginInfo;
            IssueListAdapter adapter = new IssueListAdapter(getActivity(), new ArrayList<Issue>(), 1, jql);
            ListAdapter issueListAdapter = new EndlessIssueListAdapter(issueService, getActivity(), adapter, loginInfo);
            setListAdapter(issueListAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Issue item = (Issue) getListAdapter().getItem(position);
        listener.onIssueElementClick(item);
    }

    public interface Listener {
        void onIssueElementClick(Issue issue);
    }
}

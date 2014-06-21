package com.tadamski.arij.issue.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.list.drawer.QuickSearch;
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
@EFragment
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getActivity().getString(R.string.no_issues_found));
    }

    public void executeFilter(String jql, LoginInfo loginInfo) {
        if (!jql.equals(this.actualJql) || !loginInfo.equals(this.actualLoginInfo)) {
            this.actualJql = jql;
            this.actualLoginInfo = loginInfo;
            IssueListAdapter adapter = new IssueListAdapter(getActivity(), new ArrayList<Issue>(), jql);
            ListAdapter issueListAdapter = new EndlessIssueListAdapter(issueService, getActivity(), adapter, loginInfo);
            setListAdapter(issueListAdapter);
            listener.onJql(jql);
        }
    }

    public void quickSearch(String query, LoginInfo loginInfo) {
        setListShown(false);
        quickSearchInBackground(query, loginInfo);
    }

    @Background
    void quickSearchInBackground(String query, LoginInfo loginInfo) {
        QuickSearch qs = new QuickSearch();
        QuickSearch.Action result = qs.perform(query, loginInfo);
        onQuickSearchAction(result, loginInfo);
    }

    @UiThread
    void onQuickSearchAction(QuickSearch.Action quickSearchAction, LoginInfo loginInfo) {
        setListShown(true);
        handleQuickSearchAction(quickSearchAction, loginInfo);
    }

    private void handleQuickSearchAction(QuickSearch.Action quickSearchAction, LoginInfo loginInfo) {
        if (quickSearchAction instanceof QuickSearch.DoJQL) {
            QuickSearch.DoJQL jqlAction = (QuickSearch.DoJQL) quickSearchAction;
            executeFilter(jqlAction.jql, loginInfo);
            listener.onJql(jqlAction.jql);
        } else if (quickSearchAction instanceof QuickSearch.OpenIssue) {
            QuickSearch.OpenIssue openIssueAction = (QuickSearch.OpenIssue) quickSearchAction;
            String jql = "issue = \"" + openIssueAction.issueKey + "\"";
            executeFilter(jql, loginInfo);
            listener.onJql(jql);
        } else {
            throw new IllegalArgumentException("cannot handle that type of quick search action: " + quickSearchAction.getClass());
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Issue item = (Issue) getListAdapter().getItem(position);
        listener.onIssueElementClick(item);
    }

    public interface Listener {
        void onIssueElementClick(Issue issue);

        void onJql(String jql);
    }
}

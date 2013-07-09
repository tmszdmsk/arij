package com.tadamski.arij.issue.worklog.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.worklog.resource.Worklog;
import com.tadamski.arij.issue.worklog.resource.WorklogList;
import com.tadamski.arij.issue.worklog.resource.WorklogService;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
@EFragment
public class WorklogsFragment extends SherlockListFragment {
    @Bean
    WorklogService worklogService;
    @ViewById(R.id.loading)
    View loadingIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.worklogs_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
    }

    void loadWorklogs(LoginInfo loginInfo, String issueKey, WorklogList worklogList) {
        if (worklogList == null) {
            loadWorklogsAsync(loginInfo, issueKey);
            loadingIndicator.setVisibility(View.VISIBLE);
        } else
            putWorklogsIntoList(worklogList.getWorklogs());
    }

    @Background
    void loadWorklogsAsync(LoginInfo loginInfo, String issueKey) {
        List<Worklog> worklogs = worklogService.getWorklogs(loginInfo, issueKey);
        putWorklogsIntoList(worklogs);
    }

    @UiThread
    void putWorklogsIntoList(List<Worklog> worklogs) {
        loadingIndicator.setVisibility(View.GONE);
        setListAdapter(createAdapter(worklogs));
        getListView().smoothScrollToPosition(getListAdapter().getCount());
    }

    private ListAdapter createAdapter(List<Worklog> worklogs) {
        return new WorklogsListAdapter(getActivity(), worklogs);
    }

}

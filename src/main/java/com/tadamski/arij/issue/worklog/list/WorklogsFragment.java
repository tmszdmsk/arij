package com.tadamski.arij.issue.worklog.list;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
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


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
    }

    void loadWorklogs(LoginInfo loginInfo, String issueKey, WorklogList worklogList) {
        if (worklogList == null)
            loadWorklogsAsync(loginInfo, issueKey);
        else
            putWorklogsIntoList(worklogList.getWorklogs());
    }

    @Background
    void loadWorklogsAsync(LoginInfo loginInfo, String issueKey) {
        List<Worklog> worklogs = worklogService.getWorklogs(loginInfo, issueKey);
        putWorklogsIntoList(worklogs);
    }

    @UiThread
    void putWorklogsIntoList(List<Worklog> worklogs) {
        setListAdapter(createAdapter(worklogs));
        getListView().smoothScrollToPosition(getListAdapter().getCount());
    }

    private ListAdapter createAdapter(List<Worklog> worklogs) {
        return new WorklogsListAdapter(getActivity(), worklogs);
    }

}

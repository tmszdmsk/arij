package com.tadamski.arij.issue.worklog.list;

import android.widget.ListAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.worklog.resource.Worklog;
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

    void loadWorklogs(LoginInfo loginInfo, String issueKey) {
        loadWorklogsAsync(loginInfo, issueKey);
    }

    @Background
    void loadWorklogsAsync(LoginInfo loginInfo, String issueKey) {
        List<Worklog> worklogs = worklogService.getWorklogs(loginInfo, issueKey);
        putCommentsIntoList(worklogs);
    }

    @UiThread
    void putCommentsIntoList(List<Worklog> worklogs) {
        setListAdapter(createAdapter(worklogs));
    }

    private ListAdapter createAdapter(List<Worklog> worklogs) {
        return new WorklogsListAdapter(getActivity(), worklogs);
    }

}

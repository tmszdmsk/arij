package com.tadamski.arij.issue.worklog.list;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.worklog.newlog.activity.NewWorklogActivity;
import com.tadamski.arij.issue.worklog.newlog.activity.NewWorklogActivity_;
import com.tadamski.arij.issue.worklog.resource.Worklog;
import com.tadamski.arij.issue.worklog.resource.WorklogList;
import com.tadamski.arij.issue.worklog.resource.WorklogService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
@EFragment
public class WorklogsFragment extends ListFragment {
    @Bean
    WorklogService worklogService;
    @ViewById(R.id.loading)
    View loadingIndicator;
    private LoginInfo actualLoginInfo;
    private String actualIssueKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.worklog_menu, menu);
    }

    @OptionsItem(R.id.menu_item_log_work)
    void onLogWorkClicked() {
        NewWorklogActivity_.intent(getActivity()).issueKey(actualIssueKey).loginInfo(actualLoginInfo).startForResult(NewWorklogActivity.REQUEST_CODE_LOG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.worklogs_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
    }

    void loadWorklogs(LoginInfo loginInfo, String issueKey, WorklogList worklogList) {
        this.actualLoginInfo = loginInfo;
        this.actualIssueKey = issueKey;
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
    }

    private ListAdapter createAdapter(List<Worklog> worklogs) {
        return new WorklogsListAdapter(getActivity(), worklogs);
    }

}

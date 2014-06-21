package com.tadamski.arij.issue.list;

import android.content.Context;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueService;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.util.endless.EndlessAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 16.04.13
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class EndlessIssueListAdapter extends EndlessAdapter {

    private IssueService issueService;
    private IssueListAdapter wrapped;
    private LoginInfo loginInfo;
    private IssuesResultList resultList;

    public EndlessIssueListAdapter(IssueService issueService, Context context, IssueListAdapter wrapped, LoginInfo loginInfo) {
        super(context, wrapped, R.layout.pending);
        this.issueService = issueService;
        this.wrapped = wrapped;
        this.loginInfo = loginInfo;
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        this.resultList = issueService.search(loginInfo, new SearchParams(wrapped.getJql(), (long) wrapped.getCount(), 20L));
        if (wrapped.getCount() + resultList.getIssues().size() >= resultList.getTotal())
            return false;
        else return true;
    }

    @Override
    protected void appendCachedData() {
        wrapped.getIssues().addAll(resultList.getIssues());
    }
}

package com.tadamski.arij.issue.list;

import android.content.Context;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.IssueDAO;
import com.tadamski.arij.util.endless.EndlessAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 16.04.13
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class EndlessIssueListAdapter extends EndlessAdapter {

    private IssueDAO issueDAO;
    private IssueListAdapter wrapped;
    private LoginInfo loginInfo;
    private IssueDAO.ResultList resultList;

    public EndlessIssueListAdapter(IssueDAO issueDAO, Context context, IssueListAdapter wrapped, LoginInfo loginInfo) {
        super(context, wrapped, R.layout.pending);
        this.issueDAO = issueDAO;
        this.wrapped = wrapped;
        this.loginInfo = loginInfo;
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        this.resultList = issueDAO.executeJql(wrapped.getJql(), (long) wrapped.getCount(), 20L, loginInfo);
        if (wrapped.getCount() + resultList.issues.size() >= resultList.total) return false;
        else return true;
    }

    @Override
    protected void appendCachedData() {
        wrapped.getIssues().addAll(resultList.issues);
    }
}

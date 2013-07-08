package com.tadamski.arij.issue.resource;

import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.issue.IssueResource;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.issue.resource.search.SearchResource;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

/**
 * Created by tmszdmsk on 08.07.13.
 */
@EBean
public class IssueService {

    public Issue getIssue(LoginInfo loginInfo, String issueKey) {
        IssueResource issueResource = RestAdapterProvider.get(IssueResource.class, loginInfo);
        return issueResource.getIssue(issueKey);
    }

    public IssuesResultList search(LoginInfo loginInfo, SearchParams searchParams) {
        SearchResource searchResource = RestAdapterProvider.get(SearchResource.class, loginInfo);
        return searchResource.searchForIssues(searchParams);
    }
}

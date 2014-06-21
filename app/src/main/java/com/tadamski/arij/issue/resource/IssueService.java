package com.tadamski.arij.issue.resource;

import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.resource.issue.IssueResource;
import com.tadamski.arij.issue.resource.issue.IssueUpdate;
import com.tadamski.arij.issue.resource.issue.IssuesResultList;
import com.tadamski.arij.issue.resource.model.Issue;
import com.tadamski.arij.issue.resource.model.updates.IssueUpdateParams;
import com.tadamski.arij.issue.resource.search.SearchParams;
import com.tadamski.arij.issue.resource.search.SearchResource;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

import org.androidannotations.annotations.EBean;

/**
 * Created by tmszdmsk on 08.07.13.
 */
@EBean
public class IssueService {

    public Issue getIssue(LoginInfo loginInfo, String issueKey) {
        IssueResource issueResource = RestAdapterProvider.get(IssueResource.class, loginInfo);
        return issueResource.getIssue(issueKey);
    }

    public Issue updateIssue(LoginInfo loginInfo, String issueKey, IssueUpdateParams updateParams) {
        IssueResource issueResource = RestAdapterProvider.get(IssueResource.class, loginInfo);
        return issueResource.updateIssue(issueKey, new IssueUpdate(updateParams));
    }

    public IssuesResultList search(LoginInfo loginInfo, SearchParams searchParams) {
        SearchResource searchResource = RestAdapterProvider.get(SearchResource.class, loginInfo);
        return searchResource.searchForIssues(searchParams);
    }
}

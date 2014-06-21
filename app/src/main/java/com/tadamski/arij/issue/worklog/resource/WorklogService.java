package com.tadamski.arij.issue.worklog.resource;

import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Created by t.adamski on 7/8/13.
 */
@EBean
public class WorklogService {
    public List<Worklog> getWorklogs(LoginInfo loginInfo, String issueKey) {
        WorklogResource commentsResource = getResource(loginInfo);
        return commentsResource.getWorklogs(issueKey).getWorklogs();
    }

    public Worklog addWorklog(LoginInfo loginInfo, String issueKey, Worklog worklog) {
        WorklogResource worklogResource = getResource(loginInfo);
        return worklogResource.addWorklog(issueKey, worklog);
    }

    WorklogResource getResource(LoginInfo loginInfo) {
        return RestAdapterProvider.get(WorklogResource.class, loginInfo);
    }
}

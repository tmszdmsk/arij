package com.tadamski.arij.issue.worklog.repository;

import com.googlecode.androidannotations.annotations.EBean;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

/**
 * @author tmszdmsk
 */
@EBean
public class WorkLogRepository {

    public void addNewWorklogItem(String issueKey, NewWorklog worklog, LoginInfo loginInfo) {
        WorklogResource worklogResource = RestAdapterProvider.get(WorklogResource.class, loginInfo);
        worklogResource.addWorklog(worklog, issueKey);
    }
}

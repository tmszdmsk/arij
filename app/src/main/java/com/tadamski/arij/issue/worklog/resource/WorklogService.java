package com.tadamski.arij.issue.worklog.resource;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.util.retrofit.RestAdapterProvider;

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

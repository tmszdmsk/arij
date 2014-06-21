package com.tadamski.arij.issue.list.filters;

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
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 26.06.13
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
@EBean
public class DefaultFilters {

    public List<Filter> getFilterList() {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("localMyOpenIssues","My open issues", "assignee = currentUser() AND resolution = Unresolved ORDER BY updatedDate DESC"));
        filters.add(new Filter("localReportedByMe","Reported by me", "reporter = currentUser() ORDER BY createdDate DESC"));
        filters.add(new Filter("localAllIssues","All issues", "ORDER BY createdDate DESC"));
        filters.add(new Filter("localOpenBugz","Open bugs", "type = Bug and resolution is empty"));
        filters.add(new Filter("localWatchedIssues", "Watched Issues", "watcher = currentUser() ORDER BY updatedDate DESC"));
        return filters;
    }

    public Filter getFilter(String id){
        for(Filter f: getFilterList()){
            if(f.id.equals(id)){
                return f;
            }
        }
        return null;
    }
}

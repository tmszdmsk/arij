package com.tadamski.arij.issue.worklog.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.worklog.HumanReadableDuration;
import com.tadamski.arij.issue.worklog.resource.Worklog;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmszdmsk on 07.07.13.
 */
public class WorklogsListAdapter extends BaseAdapter {
    Context ctx;
    List<Worklog> worklogs = new ArrayList<Worklog>();

    public WorklogsListAdapter(Context ctx, List<Worklog> worklogs) {
        this.ctx = ctx;
        this.worklogs = worklogs;
    }

    @Override
    public int getCount() {
        return worklogs.size();
    }

    @Override
    public Worklog getItem(int i) {
        return worklogs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.worklogs_list_elem, null);
        }
        PrettyTime p = new PrettyTime();
        TextView worklogTimeLogged = (TextView) view.findViewById(R.id.worklog_time_spent);
        TextView worklogComment = (TextView) view.findViewById(R.id.worklog_comment);
        TextView worklogDate = (TextView) view.findViewById(R.id.worklog_date);
        TextView worklogAuthor = (TextView) view.findViewById(R.id.worklog_author_name);
        Worklog worklog = getItem(i);
        worklogTimeLogged.setText(HumanReadableDuration.create(worklog.getTimeSpentSeconds()));
        worklogComment.setText(Strings.isNullOrEmpty(worklog.getComment()) ? ctx.getString(R.string.no_comment) : worklog.getComment());
        worklogAuthor.setText(worklog.getAuthor().getDisplayName());
        worklogDate.setText(p.format(worklog.getCreated()));
        return view;
    }
}

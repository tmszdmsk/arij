package com.tadamski.arij.issue.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.resource.model.Issue;

import java.text.DateFormat;
import java.util.List;

public class IssueListAdapter extends BaseAdapter {

    private static final DateFormat DATE_TIME_INSTANCE = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private final List<Issue> issues;
    private long totalNumberOfIssues;
    private String jql;
    private final LayoutInflater layoutInflater;

    public IssueListAdapter(Context ctx, List<Issue> init, long totalNumberOfIssues, String jql) {
        this.issues = init;
        this.totalNumberOfIssues = totalNumberOfIssues;
        this.jql = jql;
        this.layoutInflater = LayoutInflater.from(ctx);
    }

    public String getJql() {
        return jql;
    }

    public long getTotalNumberOfIssues() {
        return totalNumberOfIssues;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    @Override
    public int getCount() {
        return issues.size();
    }

    @Override
    public Issue getItem(int position) {
        return issues.get(position);
    }

    @Override
    public long getItemId(int position) {
        final Issue summary = getItem(position);
        return summary.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.issue_list_elem, null);
        }
        final Issue issueSummary = getItem(position);
        setViewProperties(convertView, issueSummary);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void setViewProperties(View convertView, final Issue issueSummary) {
        setTextViewText(convertView, R.id.project_shortcut, issueSummary.getKey().split("-")[0]);
        setTextViewText(convertView, R.id.issue_id_number, issueSummary.getKey().split("-")[1]);
        setTextViewText(convertView, R.id.issue_summary, issueSummary.getSummary());
        setTextViewText(convertView, R.id.issue_creation_date, DATE_TIME_INSTANCE.format(issueSummary.getCreated()));
    }

    private void setTextViewText(View parent, int textViewId, String textToSet) {
        TextView textView = (TextView) parent.findViewById(textViewId);
        textView.setText(textToSet);
    }
}

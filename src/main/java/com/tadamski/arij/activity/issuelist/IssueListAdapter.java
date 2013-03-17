package com.tadamski.arij.activity.issuelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.issue.Issue.Summary;
import com.tadamski.arij.util.Callback;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class IssueListAdapter extends BaseAdapter {

    private final List<Issue.Summary> issues;
    private final LayoutInflater layoutInflater;
    private final Callback<Issue.Summary> onClickAction;

    public IssueListAdapter(Context ctx, List<Issue.Summary> issues, Callback<Issue.Summary> onClickAction) {
        this.issues = issues;
        this.layoutInflater = LayoutInflater.from(ctx);
        this.onClickAction = onClickAction;
    }

    @Override
    public int getCount() {
        return issues.size();
    }

    @Override
    public Issue.Summary getItem(int position) {
        return issues.get(position);
    }

    @Override
    public long getItemId(int position) {
        final Summary summary = getItem(position);
        return summary.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.issue_list_elem, null);
        }
        final Summary issueSummary = getItem(position);
        setViewProperties(convertView, issueSummary);
        setClickListener(convertView, issueSummary);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void setViewProperties(View convertView, final Summary issueSummary) {
        setTextViewText(convertView, R.id.project_shortcut, issueSummary.getKey().split("-")[0]);
        setTextViewText(convertView, R.id.issue_id_number, issueSummary.getKey().split("-")[1]);
        setTextViewText(convertView, R.id.issue_summary, issueSummary.getSummary());
        setTextViewText(convertView, R.id.issue_creation_date, SimpleDateFormat.getDateTimeInstance().format(issueSummary.getCreated()));
    }

    private void setTextViewText(View parent, int textViewId, String textToSet) {
        TextView textView = (TextView) parent.findViewById(textViewId);
        textView.setText(textToSet);
    }

    private void setClickListener(View convertView, final Issue.Summary issueSummary) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAction.call(issueSummary);
            }
        });
    }
}

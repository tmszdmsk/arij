package com.tadamski.arij.issue.list.filters;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;

@EViewGroup(R.layout.filter_list_elem)
public class FilterView extends LinearLayout {
    @ViewById(R.id.filter_name)
    TextView name;

    public FilterView(Context context) {
        super(context);
    }

    public void setFilter(Filter filter) {
        name.setText(filter.name);
    }
}

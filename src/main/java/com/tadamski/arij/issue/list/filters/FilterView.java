package com.tadamski.arij.issue.list.filters;

import android.content.Context;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;

@EViewGroup(R.layout.filter_list_elem)
public class FilterView extends LinearLayout implements Checkable {
    @ViewById(R.id.filter_name)
    TextView name;
    @ViewById(R.id.radioButton)
    RadioButton radioButton;

    public FilterView(Context context) {
        super(context);
    }

    public void setFilter(Filter filter) {
        name.setText(filter.name);
    }

    @Override
    public boolean isChecked() {
        return radioButton.isChecked();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setChecked(boolean b) {
        radioButton.setChecked(b);
    }

    @Override
    public void toggle() {
        radioButton.toggle();
    }
}

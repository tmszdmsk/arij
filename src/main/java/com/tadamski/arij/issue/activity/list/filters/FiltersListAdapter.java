package com.tadamski.arij.issue.activity.list.filters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 26.06.13
 * Time: 19:11
 * To change this template use File | Settings | File Templates.
 */
public class FiltersListAdapter extends BaseAdapter {


    private Context ctx;
    private List<Filter> filters;

    public FiltersListAdapter(Context ctx, List<Filter> filters) {
        this.ctx = ctx;
        this.filters = filters;
    }

    @Override
    public int getCount() {
        return filters.size();
    }

    @Override
    public Filter getItem(int i) {
        return filters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(ctx);
        textView.setText(getItem(i).name);
        return textView;
    }
}

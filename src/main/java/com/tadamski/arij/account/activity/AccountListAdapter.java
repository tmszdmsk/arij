package com.tadamski.arij.account.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;

import java.util.List;

/**
 * @author tmszdmsk
 */
public class AccountListAdapter extends BaseAdapter {

    private final List<LoginInfo> accounts;
    private final LayoutInflater layoutInflater;

    public AccountListAdapter(Context ctx, List<LoginInfo> accounts) {
        this.accounts = accounts;
        this.layoutInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public LoginInfo getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.account_list_elem, null);
        }
        final TextView userName = (TextView) convertView.findViewById(R.id.username);
        final TextView jiraUrl = (TextView) convertView.findViewById(R.id.jira_url);
        final LoginInfo item = getItem(position);
        userName.setText(item.getUsername());
        jiraUrl.setText(item.getBaseURL());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}

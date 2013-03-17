package com.tadamski.arij.activity.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tadamski.arij.R;
import com.tadamski.arij.login.LoginInfo;
import com.tadamski.arij.util.Callback;
import java.util.List;

/**
 *
 * @author tmszdmsk
 */
public class AccountListAdapter extends BaseAdapter {

    private final Context ctx;
    private final List<LoginInfo> loginInfos;
    private final Callback<LoginInfo> onAccountClickAction;
    private final LayoutInflater layoutInflater;

    public AccountListAdapter(Context ctx, List<LoginInfo> loginInfos, Callback<LoginInfo> onAccountClickAction) {
        this.ctx = ctx;
        this.loginInfos = loginInfos;
        this.onAccountClickAction = onAccountClickAction;
        this.layoutInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return loginInfos.size();
    }

    @Override
    public LoginInfo getItem(int position) {
        return loginInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            view = layoutInflater.inflate(R.layout.account_list_elem, null);
        }
        final TextView textView = (TextView) view.findViewById(R.id.account_name);
        final LoginInfo item = getItem(position);
        textView.setText(item.getUsername()+"@"+item.getBaseURL());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAccountClickAction.call(item);
            }
        });
        return textView;
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

package com.tadamski.arij.issue.activity.properties.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.activity.properties.model.IssueProperty;
import com.tadamski.arij.issue.activity.properties.model.IssuePropertyGroup;

/**
 * @author tmszdmsk
 */
public class IssuePropertyGroupViewFactory {

    public View createMultipropertiesView(IssuePropertyGroup issuePropertyGroup, Context ctx) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.issue_property_group, null);
        TextView groupName = (TextView) view.findViewById(R.id.issue_property_group_name);
        groupName.setText(issuePropertyGroup.getName());
        ViewGroup propertiesLayout = (ViewGroup) view.findViewById(R.id.issue_property_group_properties_layout);
        for (IssueProperty issueProperty : issuePropertyGroup.getProperties()) {
            View propertyView = createPropertyView(issueProperty, ctx);
            propertiesLayout.addView(propertyView);
        }
        return view;
    }

    public View createSingleTextView(String name, String content, Context ctx) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.issue_property_group, null);
        TextView groupName = (TextView) view.findViewById(R.id.issue_property_group_name);
        groupName.setText(name);
        ViewGroup propertiesLayout = (ViewGroup) view.findViewById(R.id.issue_property_group_properties_layout);
        TextView contentView = new TextView(ctx);
        contentView.setText(content);
        propertiesLayout.addView(contentView);
        return view;
    }

    private View createPropertyView(IssueProperty issueProperty, Context ctx) {
        TextView result = new TextView(ctx);
        result.setText(issueProperty.getName() + ": " + issueProperty.getValue());
        return result;
    }
}

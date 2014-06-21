package com.tadamski.arij.issue.comments.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tadamski.arij.R;
import com.tadamski.arij.issue.comments.resource.Comment;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmszdmsk on 07.07.13.
 */
public class CommentsListAdapter extends BaseAdapter {
    Context ctx;
    List<Comment> comments = new ArrayList<Comment>();

    public CommentsListAdapter(Context ctx, List<Comment> comments) {
        this.ctx = ctx;
        this.comments = comments;
    }

    public void add(Comment object) {
        comments.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.comments_list_elem, null);
        }
        PrettyTime p = new PrettyTime();
        TextView commentText = (TextView) view.findViewById(R.id.comment_text);
        TextView commentDate = (TextView) view.findViewById(R.id.comment_date);
        TextView commentAuthorName = (TextView) view.findViewById(R.id.comment_author_name);
        Comment comment = getItem(i);
        commentText.setText(comment.getBody());
        commentAuthorName.setText(comment.getAuthor().getDisplayName());
        commentDate.setText(p.format(comment.getCreatedDate()));
        return view;
    }
}

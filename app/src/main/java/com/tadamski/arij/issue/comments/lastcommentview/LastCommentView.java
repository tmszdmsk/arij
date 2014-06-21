package com.tadamski.arij.issue.comments.lastcommentview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tadamski.arij.R;
import com.tadamski.arij.issue.comments.resource.Comment;
import com.tadamski.arij.issue.comments.resource.CommentsList;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.ocpsoft.prettytime.PrettyTime;

/**
 * Created by t.adamski on 7/11/13.
 */
@EViewGroup(R.layout.last_comment_view)
public class LastCommentView extends RelativeLayout {

    @ViewById(R.id.show_more_comments)
    Button showMoreComments;
    @ViewById(R.id.comment_text)
    TextView commentText;
    @ViewById(R.id.comment_author_name)
    TextView commentAuthorName;
    @ViewById(R.id.comment_date)
    TextView commentDate;
    @ViewById(R.id.include)
    View lastComment;
    private CommentsList commentsList;
    private ShowCommentsCallback showCommentsCallback;

    public LastCommentView(Context context) {
        super(context);
    }

    public LastCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastCommentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.show_more_comments)
    void onShowMoreCommentsClick() {
        Tracker.sendEvent("LastCommentView", "showMoreCommnts", null, null);
        getShowMoreCallback().onShowComments();
    }

    public void setShowCommentsCallback(ShowCommentsCallback showCommentsCallback) {
        this.showCommentsCallback = showCommentsCallback;
    }

    private ShowCommentsCallback getShowMoreCallback() {
        return showCommentsCallback;
    }

    private void updateView(CommentsList commentsList) {
        if (commentsList.getTotal() >= 1) {
            lastComment.setVisibility(VISIBLE);
            Comment latestComment = commentsList.getComments().get(commentsList.getComments().size() - 1);
            PrettyTime pt = new PrettyTime();
            commentAuthorName.setText(latestComment.getAuthor().getDisplayName());
            commentDate.setText(pt.format(latestComment.getCreatedDate()));
            commentText.setText(latestComment.getBody());
            if (commentsList.getTotal() == 1) {
                showMoreComments.setText("Click to reply");
            } else {
                showMoreComments.setText("Show " + (commentsList.getTotal() - 1) + " older comment(s) or reply");
            }
        } else {
            lastComment.setVisibility(GONE);
            showMoreComments.setText("No comments, click to add");
        }
    }

    public CommentsList getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(CommentsList commentsList) {
        this.commentsList = commentsList;
        updateView(commentsList);
    }

    public interface ShowCommentsCallback {
        public void onShowComments();
    }
}

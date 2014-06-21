package com.tadamski.arij.issue.comments.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.resource.Comment;
import com.tadamski.arij.issue.comments.resource.CommentsList;
import com.tadamski.arij.issue.comments.resource.CommentsService;
import com.tadamski.arij.util.analytics.Tracker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
@EFragment
public class CommentsFragment extends ListFragment {
    @Bean
    CommentsService commentsService;
    @ViewById(R.id.send_button)
    Button sendButton;
    @ViewById(R.id.comment_text)
    TextView commentText;
    @ViewById(R.id.loading)
    View loadingIndicator;
    private LoginInfo actualLoginInfo;
    private String actualIssueKey;
    private CommentsListAdapter listAdapter;
    private CommentsFragmentListener commentsFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comments_fragment, container, true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CommentsFragmentListener) {
            commentsFragmentListener = (CommentsFragmentListener) activity;
        } else {
            throw new IllegalArgumentException("activity should implement " + CommentsFragmentListener.class.getName());
        }
    }

    public void loadComments(LoginInfo loginInfo, String issueKey, CommentsList commentsList) {
        actualLoginInfo = loginInfo;
        actualIssueKey = issueKey;
        if (commentsList == null) {
            loadingIndicator.setVisibility(View.VISIBLE);
            loadCommentsAsync(loginInfo, issueKey);
        } else {
            putCommentsIntoList(commentsList.getComments());
        }
    }

    @Background
    void loadCommentsAsync(LoginInfo loginInfo, String issueKey) {
        List<Comment> comments = commentsService.getComments(loginInfo, issueKey);
        putCommentsIntoList(comments);
    }

    @UiThread
    void putCommentsIntoList(List<Comment> comments) {
        loadingIndicator.setVisibility(View.GONE);
        listAdapter = new CommentsListAdapter(getActivity(), comments);
        setListAdapter(listAdapter);
    }

    @AfterViews
    void sendButtonActivator() {
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendButton.setEnabled(shouldSendButtonBeEnabled());
            }
        });
        sendButton.setEnabled(shouldSendButtonBeEnabled());
    }

    @Click(R.id.send_button)
    void sendComment() {
        if (shouldSendButtonBeEnabled()) {
            Tracker.sendEvent("CommentsFragment", "sendComment", null, null);
            sendComment(commentText.getText().toString());
            commentText.setText("");
        }
    }

    @Background
    void sendComment(String commentText) {
        Comment newComment = new Comment(commentText);
        Comment sentComment = commentsService.addComment(actualLoginInfo, actualIssueKey, newComment);
        addSentComment(sentComment);
    }

    @UiThread
    void addSentComment(Comment comment) {
        commentsFragmentListener.onNewCommentSent();
        listAdapter.add(comment);
    }

    private boolean shouldSendButtonBeEnabled() {
        return !commentText.getText().toString().trim().isEmpty();
    }

    public interface CommentsFragmentListener {
        void onNewCommentSent();
    }

}

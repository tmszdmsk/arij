package com.tadamski.arij.issue.comments.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.*;
import com.tadamski.arij.R;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.issue.comments.resource.Comment;
import com.tadamski.arij.issue.comments.resource.CommentsService;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: tmszdmsk
 * Date: 27.06.13
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
@EFragment
public class CommentsFragment extends SherlockListFragment {
    @Bean
    CommentsService commentsService;
    @ViewById(R.id.send_button)
    ImageButton sendButton;
    @ViewById(R.id.comment_text)
    TextView commentText;
    private LoginInfo actualLoginInfo;
    private String actualIssueKey;
    private ArrayAdapter<Comment> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comments_fragment, container, true);
    }

    public void loadComments(LoginInfo loginInfo, String issueKey) {
        actualLoginInfo = loginInfo;
        actualIssueKey = issueKey;
        loadCommentsAsync(loginInfo, issueKey);
    }

    @Background
    void loadCommentsAsync(LoginInfo loginInfo, String issueKey) {
        List<Comment> comments = commentsService.getComments(loginInfo, issueKey);
        putCommentsIntoList(comments);
    }

    @UiThread
    void putCommentsIntoList(List<Comment> comments) {
        listAdapter = new ArrayAdapter<Comment>(getActivity(), android.R.layout.simple_list_item_1, comments);
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
        listAdapter.add(comment);
        getListView().smoothScrollToPosition(getListView().getCount());
    }

    private boolean shouldSendButtonBeEnabled() {
        return !commentText.getText().toString().trim().isEmpty();
    }


}

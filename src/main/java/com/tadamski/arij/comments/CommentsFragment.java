package com.tadamski.arij.comments;

import android.widget.ArrayAdapter;
import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.tadamski.arij.account.service.LoginInfo;
import com.tadamski.arij.comments.resource.Comment;

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

    public void loadComments(LoginInfo loginInfo, String issueKey) {
        loadCommentsAsync(loginInfo, issueKey);
    }

    @Background
    void loadCommentsAsync(LoginInfo loginInfo, String issueKey) {
        List<Comment> comments = commentsService.getComments(loginInfo, issueKey);
        putCommentsIntoList(comments);
    }

    @UiThread
    void putCommentsIntoList(List<Comment> comments) {
        setListAdapter(new ArrayAdapter<Comment>(getActivity(), android.R.layout.simple_list_item_1, comments));
    }


}

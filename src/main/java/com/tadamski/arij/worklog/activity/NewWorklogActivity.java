package com.tadamski.arij.worklog.activity;

import android.os.Bundle;
import android.util.Log;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.Issue;
import com.tadamski.arij.login.LoginInfo;
import java.util.Date;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;

/**
 *
 * @author tmszdmsk
 */
public class NewWorklogActivity extends RoboActivity {
    public static final String INTENT_ISSUE = "issue";
    public static final String INTENT_START_DATE = "startDate";
    public static final String INTENT_LOGIN_INFO = "loginInfo";
    
    private static final String TAG = NewWorklogActivity.class.getName();
    
    @InjectExtra(INTENT_ISSUE)
    private Issue issue;
    @InjectExtra(INTENT_START_DATE)
    private Date startDate;
    @InjectExtra(INTENT_LOGIN_INFO)
    private LoginInfo loginInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d(TAG, getIntent().toString());
    }
}

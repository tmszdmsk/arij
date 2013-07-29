package com.tadamski.arij.filter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;

/**
 * Created by tmszdmsk on 29.07.13.
 */
@EFragment(R.layout.filter_editor_fragment)
public class FilterEditorFragment extends SherlockFragment {

    @ViewById(R.id.jql)
    EditText jqlEditText;
    Listener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof Listener){
            listener = (Listener)activity;
        } else {
            throw new IllegalArgumentException("activity not implementing interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Click(R.id.test_button)
    void testButtonClicked(){
        listener.testButtonClicked(jqlEditText.getText().toString());
    }

    public interface Listener {
        void testButtonClicked(String jql);
    }

}

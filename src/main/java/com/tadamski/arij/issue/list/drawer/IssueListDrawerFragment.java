package com.tadamski.arij.issue.list.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.ViewById;
import com.tadamski.arij.R;
import com.tadamski.arij.issue.list.filters.DefaultFilters;
import com.tadamski.arij.issue.list.filters.Filter;
import com.tadamski.arij.issue.list.filters.FiltersListAdapter;
import com.tadamski.arij.util.analytics.Tracker;

import java.util.List;


/**
 * Created by tmszdmsk on 30.07.13.
 */
@EFragment(R.layout.issue_list_drawer_fragment)
public class IssueListDrawerFragment extends Fragment {

    @Bean
    DefaultFilters defaultFilters;
    @SystemService
    InputMethodManager inputMethodManager;
    @ViewById(R.id.search_edit_text)
    EditText queryEditText;
    @ViewById(R.id.filters_list)
    ListView filterList;
    private Listener listener;

    public void selectFilter(Filter filter) {
        Integer filterPosition = getFilterPosition(filter);
        if (filterPosition == null) {
            throw new IllegalArgumentException("filter not found on filter list");
        }
        queryEditText.setText("");
        filterList.setItemChecked(filterPosition, true);
    }

    public Filter getDefaultFilter() {
        return defaultFilters.getFilterList().get(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            this.listener = (Listener) activity;
        } else {
            throw new IllegalArgumentException("have to implement Listener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @AfterViews
    void initFilterList() {
        List<Filter> filters = defaultFilters.getFilterList();
        FiltersListAdapter filtersListAdapter = new FiltersListAdapter(getActivity(), filters);
        filterList.setAdapter(filtersListAdapter);
        filterList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Filter filter = (Filter) adapterView.getItemAtPosition(i);
                Tracker.sendEvent("Filters", "filterSelected", filter.name, null);
                queryEditText.setText("");
                filterList.clearChoices();
                listener.onFilterSelected(filter);
            }
        });
    }

    @AfterViews
    void initQuickSearch() {
        queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listener.onQuickSearch(v.getText().toString());
                    queryEditText.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    Integer getFilterPosition(Filter filter) {
        for (int i = 0; i < filterList.getAdapter().getCount(); i++) {
            if (filter.equals(filterList.getAdapter().getItem(i))) {
                return i;
            }
        }
        return null;
    }

    public interface Listener {
        void onQuickSearch(String query);

        void onFilterSelected(Filter filter);
    }
}

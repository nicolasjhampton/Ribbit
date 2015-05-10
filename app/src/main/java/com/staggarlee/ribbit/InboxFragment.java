package com.staggarlee.ribbit;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nicolas on 5/10/15.
 */
/**
 * A placeholder fragment containing a simple view.
 * This is referred to as "DummySectionFragment" in the
 * lesson.
 */
public class InboxFragment extends android.support.v4.app.ListFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }
}

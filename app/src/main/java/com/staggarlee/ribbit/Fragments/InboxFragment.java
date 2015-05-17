package com.staggarlee.ribbit.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.staggarlee.ribbit.Activities.MainActivity;
import com.staggarlee.ribbit.Adapters.MessageAdapter;
import com.staggarlee.ribbit.Constants.ParseConstants;
import com.staggarlee.ribbit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 5/10/15.
 */
/**
 * A placeholder fragment containing a simple view.
 * This is referred to as "DummySectionFragment" in the
 * lesson.
 */
public class InboxFragment extends android.support.v4.app.ListFragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();
    protected List<ParseObject> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inbox, container, false);

    }


    @Override
    public void onResume() {
        super.onResume();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo("recipientIds", ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                if (e == null) {
                    // success!

                    mMessages = messages;
                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject msg : mMessages) {
                        usernames[i] = msg.getString(ParseConstants.KEY_SENDER_NAME).toString();
                        i++;
                    }

                    MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);

                    setListAdapter(adapter);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Oops")
                            .setMessage(e.getMessage())
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            }
        });
    }



}
package com.staggarlee.ribbit.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.staggarlee.ribbit.Constants.Constants;
import com.staggarlee.ribbit.R;
import com.staggarlee.ribbit.Adapters.UserAdapter;

import java.util.List;

/**
 * Created by nicolas on 5/10/15.
 */

/**
 * A placeholder fragment containing a simple view.
 * This is referred to as "DummySectionFragment" in the
 * lesson.
 */
public class FriendsFragment extends Fragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();


    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendRelation;
    protected List<ParseUser> mFriends;
    protected GridView mGridview;
    protected TextView mEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_grid, container, false);

        mGridview = (GridView) rootView.findViewById(R.id.friendsGrid);

        mEmpty = (TextView) rootView.findViewById(android.R.id.empty);

        mGridview.setEmptyView(mEmpty);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendRelation = mCurrentUser.getRelation(Constants.KEY_FRIENDS_RELATION);



        ParseQuery<ParseUser> query = mFriendRelation.getQuery();
        query.orderByAscending(Constants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if (e == null) {

                    mFriends = friends;
                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername().toString();
                        i++;
                    }

                    if(mGridview.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(getActivity(),
                                mFriends);
                        mGridview.setAdapter(adapter);
                    } else {
                        ((UserAdapter) mGridview.getAdapter()).refill(mFriends);
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


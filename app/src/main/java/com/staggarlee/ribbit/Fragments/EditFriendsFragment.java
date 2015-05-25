package com.staggarlee.ribbit.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.staggarlee.ribbit.Adapters.UserAdapter;
import com.staggarlee.ribbit.Constants.Constants;
import com.staggarlee.ribbit.R;

import java.util.List;

/**
 * Created by nicolas on 5/11/15.
 */
public class EditFriendsFragment extends Fragment {

    public static final String TAG = EditFriendsFragment.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGridview;
    protected TextView mEmpty;
    protected AdapterView.OnItemClickListener mOnItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterViewCompat, View view, int position, long l) {

                    ImageView checkImageView = (ImageView) view.findViewById(R.id.checkImageView);

                    if(mGridview.isItemChecked(position)) {
                        // add friend
                        mFriendRelation.add(mUsers.get(position));
                        checkImageView.setVisibility(View.VISIBLE);

                    } else {
                        // remove friend
                        mFriendRelation.remove(mUsers.get(position));
                        checkImageView.setVisibility(View.INVISIBLE);
                    }

                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mGridview.getContext());
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_grid, container, false);

        mGridview = (GridView) rootView.findViewById(R.id.friendsGrid);

        mEmpty = (TextView) rootView.findViewById(android.R.id.empty);

        mGridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        mGridview.setOnItemClickListener(mOnItemClickListener);

        mGridview.setEmptyView(mEmpty);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendRelation = mCurrentUser.getRelation(Constants.KEY_FRIENDS_RELATION);


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(Constants.KEY_USERNAME);
        query.setLimit(1000);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                if (e == null) {
                    //Success
                    mGridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                    mUsers = users;
                    /*
                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    */

                    if(mGridview.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(getActivity(),
                                mUsers);
                        mGridview.setAdapter(adapter);
                    } else {
                        ((UserAdapter) mGridview.getAdapter()).refill(mUsers);
                    }

                    addFriendCheckmarks();


                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(mGridview.getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        if(mGridview.isItemChecked(position)) {
            // add friend
            mFriendRelation.add(mUsers.get(position));

        } else {
            // remove friend
            mFriendRelation.remove(mUsers.get(position));

        }

        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mGridview.getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });





    }
    */

    public void addFriendCheckmarks() {

        mFriendRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e == null) {
                    // mGridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

                    for(int i = 0 ; i < mUsers.size() ; i++) {
                        ParseUser user = mUsers.get(i);
                        for(ParseUser friend : friends){
                            if(friend.getObjectId().equals(user.getObjectId())) {
                               mGridview.setItemChecked(i, true);
                            }
                        }
                    }

                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(mGridview.getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }

}




package com.staggarlee.ribbit.Fragments;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import com.staggarlee.ribbit.Activities.RecipientsActivity;
import com.staggarlee.ribbit.Adapters.UserAdapter;
import com.staggarlee.ribbit.Constants.Constants;
import com.staggarlee.ribbit.HelperClasses.FileHelper;
import com.staggarlee.ribbit.R;

import java.util.ArrayList;
import java.util.List;


public class RecipientsFragment extends Fragment {

    public static final String TAG = RecipientsFragment.class.getSimpleName();


    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendRelation;
    protected List<ParseUser> mFriends;
    protected MenuItem mSendMenuItem;
    protected Uri mMediaUri;
    protected String mFileType;
    protected GridView mGridview;
    protected TextView mEmpty;
    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



            // activate or deactivate the send button
            if (mGridview.getCheckedItemCount() > 0) {
                RecipientsActivity.mSendMenuItem.setVisible(true);
            } else {
                RecipientsActivity.mSendMenuItem.setVisible(false);
            }

            // Get the checkImageView attached to this view that has been checked
            ImageView checkImageView = (ImageView) view.findViewById(R.id.checkImageView);

            // set the visibility of the checkImageView image
            if(mGridview.isItemChecked(i)) {
                // check recipient
                checkImageView.setVisibility(View.VISIBLE);

            } else {
                // uncheck recipient
                checkImageView.setVisibility(View.INVISIBLE);
            }

            /*   I was going to use this to mark the recipients, but this creates
         *   data that is stored for longer, and has to be maintained.
         *   Instead, we'll do it all at once with the getRecipientIds method.
         *
         * if (getListView().isItemChecked(position)) {
         *   mRecipients.add(mFriends.get(position));
         * } else {
         *   mRecipients.remove(mFriends.get(position));
         * }
         */

        }
    };



    public RecipientsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mMediaUri = getActivity().getIntent().getData();
        mFileType = getActivity().getIntent().getExtras().getString(Constants.KEY_FILE_TYPE);



    }

    @Override
    public void onResume() {
        super.onResume();
        mGridview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendRelation = mCurrentUser.getRelation(Constants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = mFriendRelation.getQuery()
                                      .orderByAscending(Constants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    mFriends = list;
                    /*
                    String[] usernames = new String[mFriends.size()];
                    for (int i = 0; i < mFriends.size(); i++) {
                        usernames[i] = mFriends.get(i).getUsername();
                    }
                    */
                    if (mGridview.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(getActivity(),
                                mFriends);
                        mGridview.setAdapter(adapter);
                    } else {
                        ((UserAdapter) mGridview.getAdapter()).refill(mFriends);
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mGridview.getContext());
                    builder.setTitle("Oops")
                            .setMessage(e.toString())
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.user_grid, container, false);

        mGridview = (GridView) rootView.findViewById(R.id.friendsGrid);

        mEmpty = (TextView) rootView.findViewById(android.R.id.empty);

        mGridview.setEmptyView(mEmpty);

        mGridview.setOnItemClickListener(mOnItemClickListener);

        return rootView;
    }

    /*  Constants used in this method:
     *  public static final String KEY_RECIPIENT_IDS = "recipientIds";
     *  public static final String KEY_SENDER_ID = "senderId";
     *  public static final String KEY_SENDER_NAME = "senderName";
     *  public static final String KEY_FILE = "file";
     *  public static final String KEY_FILE_TYPE = "fileType";
     */
    public ParseObject createMessage() {


        ParseObject message = new ParseObject(Constants.CLASS_MESSAGES);
        message.put(Constants.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(Constants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(Constants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(Constants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(RecipientsActivity.mContext, mMediaUri);

        if(fileBytes == null) {
            return null;
        } else {
            if(mFileType.equals(Constants.TYPE_IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            String fileName = FileHelper.getFileName(RecipientsActivity.mContext,
                                                     mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(Constants.KEY_FILE, file);
            return message;
        }
    }

    public ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<>();
          for(int i = 0; i < mGridview.getCount() ; i++) {
              if(mGridview.isItemChecked(i)) {
                  recipientIds.add(mFriends.get(i).getObjectId());
              }
          }
        return recipientIds;

    }




}

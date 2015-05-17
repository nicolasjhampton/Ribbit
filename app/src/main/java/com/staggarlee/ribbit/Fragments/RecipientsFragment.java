package com.staggarlee.ribbit.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import com.staggarlee.ribbit.Activities.RecipientsActivity;
import com.staggarlee.ribbit.Constants.ParseConstants;
import com.staggarlee.ribbit.HelperClasses.FileHelper;
import com.staggarlee.ribbit.R;

import java.util.ArrayList;
import java.util.List;


public class RecipientsFragment extends android.support.v4.app.ListFragment {

    public static final String TAG = RecipientsFragment.class.getSimpleName();


    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendRelation;
    protected List<ParseUser> mFriends;
    protected MenuItem mSendMenuItem;
    protected Uri mMediaUri;
    protected String mFileType;

    public RecipientsFragment() {}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMediaUri = getActivity().getIntent().getData();
        mFileType = getActivity().getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = mFriendRelation.getQuery()
                                      .orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    mFriends = list;
                    String[] usernames = new String[mFriends.size()];
                    for (int i = 0; i < mFriends.size(); i++) {
                        usernames[i] = mFriends.get(i).getUsername();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(),
                            android.R.layout.simple_list_item_checked, usernames);
                    setListAdapter(adapter);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
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
        return inflater.inflate(R.layout.fragment_recipients, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
            // Display send button if a recipient is selected
            if (l.getCheckedItemCount() > 0) {
                RecipientsActivity.mSendMenuItem.setVisible(true);
            } else {
                RecipientsActivity.mSendMenuItem.setVisible(false);
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


    /*  Constants used in this method:
     *  public static final String KEY_RECIPIENT_IDS = "recipientIds";
     *  public static final String KEY_SENDER_ID = "senderId";
     *  public static final String KEY_SENDER_NAME = "senderName";
     *  public static final String KEY_FILE = "file";
     *  public static final String KEY_FILE_TYPE = "fileType";
     */
    public ParseObject createMessage() {


        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(RecipientsActivity.mContext, mMediaUri);
        if(fileBytes == null) {
            return null;
        } else {
            if(mFileType.equals(ParseConstants.TYPE_IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            String fileName = FileHelper.getFileName(RecipientsActivity.mContext,
                                                     mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(ParseConstants.KEY_FILE, file);
            return message;
        }
    }

    public ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<>();
          for(int i = 0; i < getListView().getCount() ; i++) {
              if(getListView().isItemChecked(i)) {
                  recipientIds.add(mFriends.get(i).getObjectId());
              }
          }
        return recipientIds;

    }




}

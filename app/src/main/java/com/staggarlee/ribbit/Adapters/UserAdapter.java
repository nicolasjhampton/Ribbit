package com.staggarlee.ribbit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.staggarlee.ribbit.Constants.MD5Util;
import com.staggarlee.ribbit.Constants.Constants;
import com.staggarlee.ribbit.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by nicolas on 5/17/15.
 */
public class UserAdapter extends ArrayAdapter<ParseUser> {

    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapter(Context context, List<ParseUser> users) {
        super(context, R.layout.user_item, users);
        mContext = context;
        mUsers = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create a new viewholder
        ViewHolder holder;
        // is this a new view, or is it created already?
        if(convertView == null) {
            // if this is a new view block (scrolling) then inflate and store views,
            // then give it a holder.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            // (attaching the holder to the view)
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Prepare the next view and store it in the ViewHolder
        ParseUser user = mUsers.get(position);
        String email = user.getEmail().trim().toLowerCase();
        String gravatarUrl = "";
        if(email.equals("")) {
            holder.userImageView.setImageResource(R.drawable.avatar_empty);
        } else {
            String hash = MD5Util.md5Hex(email);
            gravatarUrl = Constants.GRAVATAR_URL
                    + hash + Constants.SIZE_REQUEST
                    + Constants.DEFAULT_REQUEST;

            Picasso.with(mContext)
                    .load(gravatarUrl)
                    .placeholder(R.drawable.avatar_empty)
                    .into(holder.userImageView);

        }


        holder.nameLabel.setText(user.getUsername());



        return convertView;
    }

    public void refill(List<ParseUser> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        ImageView userImageView;
        TextView nameLabel;



    }
}

package com.staggarlee.ribbit.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.staggarlee.ribbit.Constants.Constants;
import com.staggarlee.ribbit.R;

import java.util.Date;
import java.util.List;

/**
 * Created by nicolas on 5/17/15.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create a new viewholder
        ViewHolder holder;
        // is this a new view, or is it created already?
        if(convertView == null) {
            // if this is a new view block (scrolling) then inflate and store views,
            // then give it a holder.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.messageIcon = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.senderLabel = (TextView) convertView.findViewById(R.id.senderLabel);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            // (attaching the holder to the view)
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Prepare the next view and store it in the ViewHolder
        ParseObject message = mMessages.get(position);
        Date createdAt = message.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(createdAt.getTime(),
                now, DateUtils.SECOND_IN_MILLIS).toString();
        holder.timeLabel.setText(convertedDate);


        switch (message.getString(Constants.KEY_FILE_TYPE)) {
            case Constants.TYPE_IMAGE:
                holder.messageIcon.setImageResource(R.drawable.ic_picture);
                break;
            case Constants.TYPE_VIDEO:
                holder.messageIcon.setImageResource(R.drawable.ic_video);
                break;
            default:
                // do nothing, should never get here
        }

        holder.senderLabel.setText(message.getString(Constants.KEY_SENDER_NAME));



        return convertView;
    }

    public void refill(List<ParseObject> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        ImageView messageIcon;
        TextView senderLabel;
        TextView timeLabel;


    }
}

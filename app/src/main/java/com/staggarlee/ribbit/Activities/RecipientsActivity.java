package com.staggarlee.ribbit.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.staggarlee.ribbit.Fragments.RecipientsFragment;
import com.staggarlee.ribbit.R;


public class RecipientsActivity extends ActionBarActivity {


    public static Context mContext;
    public static MenuItem mSendMenuItem;
    protected RecipientsFragment list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);
        mContext = this;
        list = new RecipientsFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, list).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        mSendMenuItem = menu.getItem(0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_send:
                // send to recipients checked
                ParseObject message = list.createMessage();
                if(message == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Oops")
                            .setMessage(R.string.general_error)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    send(message);
                    finish();
                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    protected void send(ParseObject message) {

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(RecipientsActivity.this,
                                   "Send successful!", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setTitle("Oops")
                            .setMessage(R.string.general_error)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });



    }
}

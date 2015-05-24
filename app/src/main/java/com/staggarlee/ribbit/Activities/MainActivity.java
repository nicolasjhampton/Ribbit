package com.staggarlee.ribbit.Activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.parse.ParseUser;
import com.staggarlee.ribbit.Constants.Constants;
import com.staggarlee.ribbit.R;
import com.staggarlee.ribbit.Adapters.SectionsPagerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

// ***The TabListener has been deprecated, will look for update later***
// ***The MainActivity class is implementing TabListener, so the     ***
// *** activity itself is listening for tab changes. We'll have to   ***
// *** replace this later. Remember that TabListener is an interface,***
// *** so we'll be looking for a replacement interface later.        ***
public class MainActivity extends ActionBarActivity  {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PHOTO_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; //10 MB

    protected Uri mMediaUri;


    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    switch(which) {
                        case 0: //take picture
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mMediaUri = getOutputMediaUri(MEDIA_TYPE_IMAGE);
                            if(mMediaUri == null) {
                                Toast.makeText(MainActivity.this,
                                        getString(R.string.error_external_storage),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                            }
                            break;
                        case 1: //take video
                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            mMediaUri = getOutputMediaUri(MEDIA_TYPE_VIDEO);
                            if(mMediaUri == null) {
                                Toast.makeText(MainActivity.this,
                                        getString(R.string.error_external_storage),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                            }
                            break;
                        case 2: //choose picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent, CHOOSE_PHOTO_REQUEST);
                            break;
                        case 3: //choose video
                            Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseVideoIntent.setType("video/*");
                            Toast.makeText(MainActivity.this,
                                           R.string.video_file_size_warning,
                                           Toast.LENGTH_LONG).show();
                            startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUEST);
                            break;

                    }

                }
            };
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private Uri getOutputMediaUri(int mediaType) {
        if(isExternalStorageAvailable()){
            String appName = MainActivity.this.getString(R.string.app_name);
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appName);
            if(!mediaStorageDir.exists()) {
                if(!mediaStorageDir.mkdirs()) {
                    Log.e(TAG, "Failed to make directory");
                    return null;
                }
            }
            File mediaFile = null;
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            formatter.setTimeZone(TimeZone.getDefault());
            switch (mediaType) {
                case 4:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + formatter.format(now) + ".jpg");
                    break;
                case 5:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + formatter.format(now) + ".mp4");
                    break;
            }

            if(mediaFile.equals(null)) {
                return null;
            }

            Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
            return Uri.fromFile(mediaFile);

        } else {
            return null;
        }

    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Get the user cached on disk if present
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            // If no one is logged in, go to the login screen
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_gradient));


        // ***This is where setNavigationMode has been deprecated***


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                break;
            case R.id.action_edit_friends:
                Intent intent = new Intent(this, EditFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.menu_camera_label)
                        .setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if everything went right, fin out what we got back from where
        if(resultCode == RESULT_OK){
            // this catches to see if the data came back from a gallery
            if(requestCode == CHOOSE_PHOTO_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST) {
                //check to see if there's any data in the data
                if(data == null){
                    // error if there's no data
                    Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
                } else {
                    // store the Uri if data came back
                    mMediaUri = data.getData();
                }
                if(requestCode == CHOOSE_VIDEO_REQUEST){
                    //check video size to see if it's larger than 10 mb
                    InputStream inputStream = null;
                    int fileSize = 0;
                    try{
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e){
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if(fileSize >= FILE_SIZE_LIMIT){
                        Toast.makeText(MainActivity.this, R.string.error_video_file_too_large, Toast.LENGTH_LONG).show();
                        return;
                    }
                }



            } else {
                // This is for the Take_Picture and Take_Video actions
                // we add the new file we get to the application cache we got it from
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }

            //send us to the RecipientsFragment
            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            String fileType;
            if(requestCode == TAKE_PHOTO_REQUEST || requestCode == CHOOSE_PHOTO_REQUEST) {
                fileType = Constants.TYPE_IMAGE;
            } else {
                fileType = Constants.TYPE_VIDEO;
            }
            recipientsIntent.putExtra(Constants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);


            //display error if something went wrong
        } else if(resultCode != RESULT_CANCELED){
           Toast.makeText(MainActivity.this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }



    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

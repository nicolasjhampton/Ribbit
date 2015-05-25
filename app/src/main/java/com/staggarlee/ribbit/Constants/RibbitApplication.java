package com.staggarlee.ribbit.Constants;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by nicolas on 5/8/15.
 */
public class RibbitApplication extends Application {

    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(Constants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "v4bbB45r8hWcOmYUXoANTKLOQhABz9dpsN3tuZQR", "wRvgslf8nZBe42urfpRljTBAJOAKdSsNPTakgVGB");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }


}

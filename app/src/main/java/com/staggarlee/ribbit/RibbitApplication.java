package com.staggarlee.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by nicolas on 5/8/15.
 */
public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "v4bbB45r8hWcOmYUXoANTKLOQhABz9dpsN3tuZQR", "wRvgslf8nZBe42urfpRljTBAJOAKdSsNPTakgVGB");

    }


}

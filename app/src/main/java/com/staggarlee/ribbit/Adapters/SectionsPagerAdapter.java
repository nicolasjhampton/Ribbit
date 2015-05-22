package com.staggarlee.ribbit.Adapters;

/**
 * Created by nicolas on 5/9/15.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.staggarlee.ribbit.Fragments.FriendsFragment;
import com.staggarlee.ribbit.Fragments.InboxFragment;
import com.staggarlee.ribbit.R;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a InboxFragment (defined as a static inner class below).

        switch (position) {
            case 0:
                return new InboxFragment();

            case 1:
                return new FriendsFragment();

                // return the new FriendsFragment() here
            default:
                return new InboxFragment();
        }


    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }
    /*
     * Function for applying the icon to the tab. We don't have tabs,
     * so I'm commenting this out until I can find a way to apply to
     * the PagerTab.
     *
     *
    public int getIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;
            default:
                return R.drawable.ic_tab_inbox;
        }
    }
    */
}

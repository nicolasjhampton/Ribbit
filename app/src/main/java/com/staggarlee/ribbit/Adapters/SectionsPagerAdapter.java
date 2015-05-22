package com.staggarlee.ribbit.Adapters;

/**
 * Created by nicolas on 5/9/15.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

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
    protected Drawable mInboxImage;
    protected Drawable mFriendsImage;


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

        mInboxImage = ContextCompat.getDrawable(mContext, R.drawable.ic_tab_inbox);
        mFriendsImage = ContextCompat.getDrawable(mContext, R.drawable.ic_tab_friends);

        SpannableStringBuilder sb;
        ImageSpan span;

        switch (position) {
            case 0:
                sb = new SpannableStringBuilder(" " + mContext.getString(R.string.title_section1).toUpperCase(l)); // space added before text for convenience

                mInboxImage.setBounds(0, 0, mInboxImage.getIntrinsicWidth(), mInboxImage.getIntrinsicHeight());
                span = new ImageSpan(mInboxImage, ImageSpan.ALIGN_BOTTOM);
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return sb;
            case 1:
                sb = new SpannableStringBuilder(" " + mContext.getString(R.string.title_section2).toUpperCase(l)); // space added before text for convenience

                mFriendsImage.setBounds(0, 0, mFriendsImage.getIntrinsicWidth(), mFriendsImage.getIntrinsicHeight());
                span = new ImageSpan(mFriendsImage, ImageSpan.ALIGN_BOTTOM);
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return sb;
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

package com.staggarlee.ribbit.Activities;



import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.staggarlee.ribbit.Fragments.EditFriendsFragment;


public class EditFriendsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        EditFriendsFragment list = new EditFriendsFragment();
        fm.beginTransaction().add(android.R.id.content, list).commit();
    }


}
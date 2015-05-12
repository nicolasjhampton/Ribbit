package com.staggarlee.ribbit;


import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;



public class EditFriendsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getFragmentManager();
        EditFriendsFragment list = new EditFriendsFragment();
        fm.beginTransaction().add(android.R.id.content, list).commit();
    }


}
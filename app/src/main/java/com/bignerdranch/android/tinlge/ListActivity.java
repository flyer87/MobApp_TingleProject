package com.bignerdranch.android.tinlge;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class ListActivity extends AppCompatActivity {

    private static ThingLab sThingLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fulllist_container);
        if(fragment == null){
            fragment = new ListFragment();
            fm.beginTransaction().add(R.id.fulllist_container,fragment).commit();
        }
    }
}

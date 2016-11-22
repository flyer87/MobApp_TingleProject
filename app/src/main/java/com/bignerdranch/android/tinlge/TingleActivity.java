package com.bignerdranch.android.tinlge;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// , PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
public class TingleActivity extends AppCompatActivity
        implements TingleFragment.ToActivityOnDataStateChanged{

    private Fragment list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle); // activity_tingle.xml (land) for landscape mode

        FragmentManager fm = getSupportFragmentManager();

        Fragment tingle_fragment = fm.findFragmentById(R.id.tingle_fragment_container);
        if (tingle_fragment == null) {
            tingle_fragment = new TingleFragment();
            fm.beginTransaction()
                    .add(R.id.tingle_fragment_container, tingle_fragment)
                    .commit();
        }

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            list_fragment = fm.findFragmentById(R.id.list_fragment_container);
            if (list_fragment == null) {
                list_fragment = new ListFragment();
                fm.beginTransaction()
                        .add(R.id.list_fragment_container, list_fragment)
                        .commit();
            }
        }
    }


    @Override
    public void stateChange() {
        FragmentManager fm = getSupportFragmentManager();

        list_fragment = fm.findFragmentById(R.id.list_fragment_container);
        if (list_fragment != null) {
            fm.beginTransaction().remove(list_fragment).commit();
            list_fragment = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.list_fragment_container, list_fragment)
                    .commit();
        }
    }
}

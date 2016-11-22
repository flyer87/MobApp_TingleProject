package com.bignerdranch.android.tinlge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Omer on 02.03.2016.
 */
public class Show extends AppCompatActivity implements ShowFragment.ToActivityOnItemDeleted {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.item_container);
        if (fragment==null) {
            fragment = new ShowFragment();
            fm.beginTransaction().add(R.id.item_container, fragment).commit();
        }
    }

    @Override
    public void stateChanged(Context context) {
        //Intent intent = new Intent(ShowFragment.this.getContext(), TingleActivity.class);
        Intent intent = new Intent(context, TingleActivity.class);
        startActivity(intent);


    }
}

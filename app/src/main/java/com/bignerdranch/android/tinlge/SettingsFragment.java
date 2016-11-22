package com.bignerdranch.android.tinlge;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Omer on 13.04.2016.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}

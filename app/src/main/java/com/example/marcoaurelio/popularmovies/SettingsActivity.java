/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies;

import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 * Contains the SettingsFragment.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}

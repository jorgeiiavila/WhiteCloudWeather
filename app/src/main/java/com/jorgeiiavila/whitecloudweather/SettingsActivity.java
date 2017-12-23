package com.jorgeiiavila.whitecloudweather;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Load the Settings Fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.container_settings, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        public static final String KEY_PREF_LOCATION = "location_preference";
        public static final String KEY_PREF_UNITS = "unit_preference";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.fragment_settings);
        }
    }
}
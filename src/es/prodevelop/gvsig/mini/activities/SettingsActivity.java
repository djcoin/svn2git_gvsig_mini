package es.prodevelop.gvsig.mini.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import es.prodevelop.gvsig.mini.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        
    }

}
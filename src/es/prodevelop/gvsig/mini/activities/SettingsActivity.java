package es.prodevelop.gvsig.mini.activities;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.settings.Settings;

/**
 * This is the Activity to let the user to configure the settings for the
 * application. It is build from the xml/settings.xml. At the moment this class
 * configure and update automatically, CheckBoxPreference, EditTextPreference,
 * Preference and ListPreference. Use strings.xml to set the values of localized
 * data. Use arrays.xml to add entries and values for ListPreference.
 * 
 * Summaries are update automatically and values set to the Settings singleton.
 * To add new preferences use the string-array: "settings_properties" in
 * arrays.xml, to add a new key for the preference, and then add the Preference
 * to settings.xml with the new key added.
 * 
 * From any class of the application, the value of a preference could be get
 * from Settings using its key.
 * 
 * @author aromeu
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private final static Logger log = Logger.getLogger(SettingsActivity.class
			.getName());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			CompatManager.getInstance().getRegisteredLogHandler()
					.configureLogger(log);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.settings);

			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);
			updateSummaries();

		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreate", e);
			LogFeedbackActivity.showSendLogDialog(this);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences,
			String key) {
		try {
			updateSummaries();
		} catch (Exception e) {
			log.log(Level.SEVERE, "onSharedPreferenceChanged", e);
		}
	}

	/**
	 * Iterates the settings_properties string-array in arrays.xml and process
	 * the Preferences
	 */
	public void updateSummaries() {
		try {
			String[] properties = getResources().getStringArray(
					R.array.settings_properties);

			for (String s : properties) {
				Preference p = this.getPreference(s);
				processPreference(p);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "onSharedPreferenceChanged", e);
		}
	}

	/**
	 * Works for CheckBoxPreference, EditTextPreference, ListPreference and
	 * Preference, taken their values, putting them on Settings (using the key
	 * of the Preference) and updating the summary text
	 * 
	 * @param p
	 */
	private void processPreference(Preference p) {
		try {
			if (p instanceof CheckBoxPreference) {
				processCheckBoxPreference((CheckBoxPreference) p);
			} else if (p instanceof EditTextPreference) {
				processEditTextPreference((EditTextPreference) p);
			} else if (p instanceof ListPreference) {
				processListPreference((ListPreference) p);
			} else if (p instanceof Preference) {
				processSimplePreference(p);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "onSharedPreferenceChanged", e);
		}
	}

	private void processCheckBoxPreference(CheckBoxPreference p) {
		boolean isChecked = false;
		if (p.isChecked())
			isChecked = true;
		else if (p.getKey().compareTo(
				getText(R.string.settings_key_gps).toString()) == 0) {
			CheckBoxPreference c = (CheckBoxPreference) this
					.findPreference(getText(R.string.settings_key_orientation));
			c.setChecked(isChecked);
			Settings.getInstance().putValue(c.getKey(), new Boolean(isChecked));
		}

		Settings.getInstance().putValue(p.getKey(), new Boolean(isChecked));
	}

	private void processEditTextPreference(EditTextPreference p) {
		Settings.getInstance().putValue(p.getKey(), p.getText());
		p.setSummary(p.getText());
	}

	private void processListPreference(ListPreference p) {
		Settings.getInstance().putValue(p.getKey(), p.getValue());
		p.setSummary(p.getEntry());
	}

	private void processSimplePreference(Preference p) {
		String s = "";
		try {
			s = Settings.getInstance().getValue(p.getKey()).toString();
		} catch (Exception ignore) {
			// Maybe the property is not set
		}

		if (p.getKey().compareTo(
				getText(R.string.settings_key_data_transfer).toString()) == 0) {
			if (s.compareTo("") == 0)
				s = "0";
		}

		p.setSummary(String.format(getText(
				R.string.summary_settings_downloaded_data).toString(), s));

	}

	private Preference getPreference(String id) {
		return this.getPreferenceScreen().findPreference(id);

	}

}
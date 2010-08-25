package es.prodevelop.gvsig.mini.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import es.prodevelop.gvsig.mini.R;

public class Settings {

	private static Settings instance;

	private HashMap<String, Object> properties = new HashMap<String, Object>();

	private ArrayList<OnSettingsChangedListener> observers = new ArrayList<OnSettingsChangedListener>();
	
	ArrayList<String> keysChanged = new ArrayList<String>();

	public static Settings getInstance() {
		if (instance == null)
			instance = new Settings();
		return instance;
	}

	public void putValue(String key, Object value) {
		properties.put(key, value);
		if (!keysChanged.contains(key))
			keysChanged.add(key);
	}
	
	public void notifyObserversWithChanges() {
		try {
			for (String key: keysChanged) {
				for (OnSettingsChangedListener o : observers) {
					o.onSettingChange(key, properties.get(key));
				}
			}
		} catch (Exception e) {
			
		} finally {
			keysChanged.clear();
		}
	}

	public Object getValue(String key) {
		return properties.get(key);
	}

	public boolean getBooleanValue(String key) throws NoSuchFieldError {
		try {
			return Boolean.valueOf(properties.get(key).toString())
					.booleanValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(key);
		}
	}

	public String getStringValue(String key) throws NoSuchFieldError {
		try {
			return properties.get(key).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(key);
		}
	}

	public int getIntValue(String key) throws NoSuchFieldError {
		try {
			return Integer.valueOf(properties.get(key).toString()).intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(key);
		}
	}

	public void initializeFromSharedPreferences(Context context) {
		PreferenceManager.setDefaultValues(context, R.xml.settings, false);		
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		HashMap<String, ?> map = (HashMap<String, ?>) preferences.getAll();
		Iterator<String> keys = map.keySet().iterator();

		String key;
		while (keys.hasNext()) {
			key = keys.next();
			properties.put(key, map.get(key));
		}
	}

	public void addOnSettingsChangedListener(OnSettingsChangedListener listener) {
		if (!observers.contains(listener))
			this.observers.add(listener);
	}

}

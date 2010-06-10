package es.prodevelop.gvsig.mini.settings;

import java.util.ArrayList;
import java.util.Hashtable;

public class Settings {
	
	private static Settings instance;
	
	private Hashtable<String, Object> properties = new Hashtable<String, Object>();
	
	private ArrayList<OnSettingsChangedListener> observers = new ArrayList<OnSettingsChangedListener>(); 
	
	public static Settings getInstance() {
		if (instance == null)
			instance = new Settings();
		return instance;
	}
	
	public void putValue(String key, Object value) {
		properties.put(key, value);
		for (OnSettingsChangedListener o : observers) {
			o.onSettingChange(key, value);
		}
	}
	
	public Object getValue(String key) {
		return properties.get(key);
	}
	
	public void initializeFromSharedPreferences() {
		
	}
	
	public void addOnSettingsChangedListener(OnSettingsChangedListener listener) {
		this.observers.add(listener);
	}

}

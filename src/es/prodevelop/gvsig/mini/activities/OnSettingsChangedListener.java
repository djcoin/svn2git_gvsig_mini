package es.prodevelop.gvsig.mini.activities;

/**
 * Observer of changes of Settings
 * @author aromeu
 *
 */
public interface OnSettingsChangedListener {
	
	/**
	 * Called when a setting has changed. The value must be cast to its correct type
	 * @param key The key of the setting changed. Must be one of settings_properties in arrays.xml
	 * @param value The new value of the setting
	 */
	public void onSettingChange(String key, Object value);

}
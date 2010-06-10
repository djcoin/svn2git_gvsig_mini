package es.prodevelop.gvsig.mini.settings;

/**
 * Observer of changes of Settings
 * @author aromeu
 *
 */
public interface OnSettingsChangedListener {
	
	/**
	 * Called when a setting has change. The value must be cast to its corrent type
	 * @param key The key of the setting change. Must be one of settings_properties in arrays.xml
	 * @param value The new value of the setting
	 */
	public void onSettingChange(String key, Object value);

}

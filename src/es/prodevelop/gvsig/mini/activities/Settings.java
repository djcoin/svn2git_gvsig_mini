/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
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
			for (String key : keysChanged) {
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

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

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

	public void updateBooleanSharedPreference(String key, Boolean value,
			Context context) {
		try {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

//			if (preferences.contains(key)) {
				Editor edit = preferences.edit();
				edit.putBoolean(key, value);
				edit.commit();
				
				putValue(key, value);
//			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
	
	public void updateStringSharedPreference(String key, String value,
			Context context) {
		try {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

//			if (preferences.contains(key)) {
				Editor edit = preferences.edit();
				edit.putString(key, value);
				edit.commit();
				
				
//			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
}

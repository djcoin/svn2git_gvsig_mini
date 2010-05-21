package es.prodevelop.gvsig.mini.common.android;

import android.content.Context;
import es.prodevelop.gvsig.mini.common.CompatManager;

/**
 * This class is a facade to register all the implementations of the Android
 * compat API
 * 
 * @author aromeu
 * 
 */
public class Initializer {

	/**
	 * Registers all the implementations of the Android compat API into de
	 * CompatManager singleton	  
	 * @param context The Android application context
	 */
	public static void registerAll(Context context) {
		CompatManager.getInstance().registerContext(new AndroidContext(context));
	}
}

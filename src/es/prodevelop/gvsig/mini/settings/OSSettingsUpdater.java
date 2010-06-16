package es.prodevelop.gvsig.mini.settings;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.wms.OSRenderer;

public class OSSettingsUpdater {

	private final static Logger log = Logger.getLogger(OSSettingsUpdater.class
			.getName());

	public static void synchronizeRendererWithSettings(OSRenderer osr,
			Context context) {
		try {
			if (osr.getType() != MapRenderer.OS_RENDERER)
				return;
			boolean isCustomEnabled = false;
			try {
				isCustomEnabled = Settings.getInstance().getBooleanValue(
						context.getText(R.string.settings_key_os_custom)
								.toString());
			} catch (NoSuchFieldError ignore) {

			}

			if (isCustomEnabled) {
				osr.setKey(Settings.getInstance().getStringValue(
						context.getText(R.string.settings_key_os_key)
								.toString()));
				osr.setKeyURL(Settings.getInstance().getStringValue(
						context.getText(R.string.settings_key_os_url)
								.toString()));
			} else {
				osr.setDefaultKeysAndURLs();
			}
			// Layers.getInstance().persist();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
}

package es.prodevelop.gvsig.mini.activities;

import java.util.logging.Level;

import es.prodevelop.gvsig.mini.views.overlay._PointOverlay;

import android.os.Bundle;

public class _MapPOI extends Map {
	private _PointOverlay ovi;

	public _MapPOI() {
		// TODO Auto-generated constructor stub
	}
	
	public void loadUI(Bundle savedInstanceState) {
		try {
			super.loadUI(savedInstanceState);
			ovi = new _PointOverlay(this, osmap, _PointOverlay.DEFAULT_NAME);
			this.osmap.addOverlay(ovi);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError ou) {
			System.gc();
			log.log(Level.SEVERE, "", ou);
		}
	}
}

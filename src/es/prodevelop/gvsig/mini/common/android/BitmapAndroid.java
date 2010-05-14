package es.prodevelop.gvsig.mini.common.android;

import android.graphics.Bitmap;
import es.prodevelop.gvsig.mini.common.IBitmap;

public class BitmapAndroid implements IBitmap {

	private Bitmap bitmap;

	public BitmapAndroid(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public Bitmap getBitmap() {
		return bitmap;
	}

	@Override
	public void recycle() {
		if (bitmap != null)
			bitmap.recycle();
	}

}

package es.prodevelop.gvsig.mini.symbol;

import android.graphics.Bitmap;
import es.prodevelop.gvsig.mini.geom.Point;

public abstract class SymbolSelector {

	public abstract Bitmap getSymbol(Point p);

	public abstract String getText(Point p);

	public abstract int[] getMidSymbol(Point p);

	public int[] getMidPopup(Point p) {
		final int[] mid = getMidSymbol(p);
		if (mid != null && mid.length == 2)
			return new int[] { 0, getMidSymbol(p)[1] };
		return new int[] { 0, 0 };
	}

	public int getMaxTouchDistance(Point p) {
		return getMidPopup(p)[1] * 2;
	}
}

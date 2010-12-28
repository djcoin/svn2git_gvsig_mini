package es.prodevelop.gvsig.mini.symbol;

import android.graphics.Bitmap;
import es.prodevelop.gvsig.mini.geom.Point;

public abstract class SymbolSelector {

	public abstract Bitmap getSymbol(Point p);

	public abstract String getText(Point p);

	public abstract int[] getMidSymbol();
}

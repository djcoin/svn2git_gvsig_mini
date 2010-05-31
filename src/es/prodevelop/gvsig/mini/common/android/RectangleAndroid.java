package es.prodevelop.gvsig.mini.common.android;

import android.graphics.Rect;
import es.prodevelop.gvsig.mini.common.IRect;

public class RectangleAndroid implements IRect {
	
	private Rect instance;
	
	public RectangleAndroid() {
		instance = new Rect();
	}

	@Override
	public boolean intersects(int left, int top, int right, int bottom) {
		return instance.intersects(left, top, right, bottom);
	}

	@Override
	public void set(int left, int top, int right, int bottom) {
		instance.set(left, top, right, bottom);
	}

	@Override
	public int getArea() {
		return (instance.bottom - instance.top) * (instance.right - instance.left); 
	}
}

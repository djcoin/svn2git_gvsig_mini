/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2009 Prodevelop.
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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Pequeña y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.map.ViewPort;

/**
 * A MapOverlay to draw transient geometries. For example the rectangle zoom envelope. 
 * It also manages touchevents
 * @author aromeu 
 * @author rblanco
 *
 */
public class AcetateOverlay extends MapOverlay {
	
	public AcetateOverlay(Context context, TileRaster tileRaster) {
		super(context, tileRaster);
		try {			
			log.setClientID(this.toString());
			filledPaint = new Paint();			
			filledPaint.setStyle(Style.FILL_AND_STROKE);
			filledPaint.setAntiAlias(true);
			filledPaint.setStrokeWidth(3);			
			filledPaint.setColor(Color.RED);
			filledPaint.setAlpha(50);
			
			rectanglePaint = new Paint();
			rectanglePaint.setStyle(Style.STROKE);
			filledPaint.setStrokeWidth(3);	
			rectanglePaint.setAntiAlias(true);
			rectanglePaint.setColor(Color.RED);
			
			path = new Path();
			t = getTileRaster();
		} catch (Exception e) {
			log.error(e);
		}			
	}

	private final static Logger log = LoggerFactory.getLogger();
	
	private int mTouchDownX;
	private int mTouchDownY;
	public static int mTouchMapOffsetX;
	public static int mTouchMapOffsetY;
	private int fromX = -1;
	private int fromY = -1;
	private int toX = -1;
	private int toY = -1;
	TileRaster t;
	Paint filledPaint;
	Paint rectanglePaint;
	Path path;


	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		try {	
			if (drawZoomRectangle && rectangle != null) {
				if (rectangle.width() <= 0 || rectangle.height()<= 0) {
					rectangle.left -=1;
					rectangle.right += 1;
					rectangle.top -= 1;
					rectangle.bottom +=1;
				}
				c.drawRect(rectangle, rectanglePaint);
			}
			if (toX >= 0 && toY >= 0 && fromX >= 0 && fromY >= 0 && !maps.panMode) {
				path.rewind();				
				path.moveTo(fromX, fromY);
				path.lineTo(fromX, toY);
				path.lineTo(toX, toY);
				path.lineTo(toX, fromY);
				path.lineTo(fromX, fromY);
				c.drawPath(path, filledPaint);
				c.drawPath(path, rectanglePaint);				
			}	
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateRectangle() {
		int toX = mTouchMapOffsetX + mTouchDownX;
		int toY = mTouchDownY + mTouchMapOffsetY;
		
		if (this.mTouchDownX < toX) {
			fromX = this.mTouchDownX;			
			this.toX = toX;
		} else {
			fromX = toX;
			this.toX = this.mTouchDownX;
		}
		
		if (this.mTouchDownY < toY) {
			fromY = this.mTouchDownY;
			this.toY = toY;
		} else {
			fromY = toY;
			this.toY = this.mTouchDownY;
		}
	}

	public boolean onTouchEvent(final MotionEvent event) {
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (t.panMode) {
					t.mTouchDownX = (int) event.getX();
					t.mTouchDownY = (int) event.getY();
					t.mTouchDownY = (int) event.getY();
				} else {
					this.mTouchDownX = (int) event.getX();
					this.mTouchDownY = (int) event.getY();
				}
								
				t.invalidate();				
				return true;				
			case MotionEvent.ACTION_MOVE:
				if (t.panMode) {
					t.mTouchMapOffsetX = (int) event.getX() - t.mTouchDownX;
					t.mTouchMapOffsetY = (int) event.getY() - t.mTouchDownY;					
					ViewPort.mTouchMapOffsetX = (int) event.getX() - t.mTouchDownX;
					ViewPort.mTouchMapOffsetY = (int) event.getY() - t.mTouchDownY;
				} else {
					mTouchMapOffsetX = (int) event.getX() - mTouchDownX;
					mTouchMapOffsetY = (int) event.getY() - mTouchDownY;
					this.updateRectangle();
				}				
				t.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:
				final int viewWidth_2 = t.centerPixelX;
				final int viewHeight_2 = t.centerPixelY;
				
				if (t.panMode) {					
					double[] center = t.getMRendererInfo().fromPixels(
							new int[] {viewWidth_2, viewHeight_2});
					t.mTouchMapOffsetX = 0;
					t.mTouchMapOffsetY = 0;
					ViewPort.mTouchMapOffsetX = 0;
					ViewPort.mTouchMapOffsetY = 0;
					t.setMapCenter(center[0], center[1]);
				} else {					
					this.updateRectangle();
					
					mTouchMapOffsetX = 0;
					mTouchMapOffsetY = 0;
					double[] minXY = t.getMRendererInfo().fromPixels(
							new int[] { fromX, fromY });
					double[] maxXY = t.getMRendererInfo().fromPixels(
							new int[] { this.toX, this.toY });
					
					Extent currentExtent = new Extent(minXY[0], minXY[1], maxXY[0], maxXY[1]);
					
					t.zoomToExtent(currentExtent, false);
					t.setMapCenter(currentExtent.getCenter().getX(), currentExtent.getCenter().getY());
				}				
				resetRectangle();
				
//				
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			return true;
		}

	}
	
	private void resetRectangle() {
		fromX = -1;
		fromY = -1;
		toX = -1;
		toY = -1;
	}
	
	boolean drawZoomRectangle = false;
	Rect rectangle;
	
	public void drawZoomRectangle(Rect r) {
		try {
			drawZoomRectangle = true;
			rectangle = r;			
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	public void cleanZoomRectangle() {
		try {
			drawZoomRectangle = false;			
			rectangle = null;
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	/**
	 * returns null
	 */
	public Feature getNearestFeature(Pixel pixel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * returns null
	 */
	public ItemContext getItemContext() {
		return null;
	}
	
	@Override
	public void destroy() {
		try {
			filledPaint = null;
			rectanglePaint = null;
			path = null;
		} catch (Exception e) {
			log.error(e);
		}
	}

}

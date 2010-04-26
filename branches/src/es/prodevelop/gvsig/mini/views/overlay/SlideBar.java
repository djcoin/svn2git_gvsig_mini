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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

/**
 * A Vertical SeekBar implementation
 * @author aromeu 
 * @author rblanco
 *
 */
public class SlideBar extends SeekBar {

	private int oHeight = 320, oWidth = 29;
	private int oProgress = -1, oOffset = -1;;
	private float xPos = -1, yPos = -1;
	private int top = -1, bottom = -1, left = -1, right = -1;
	Bitmap thumb;
	Bitmap thumbPressed;
	Bitmap thumbPaint;
	Bitmap back;
	Bitmap progress;
	Paint p;
	float thumbPos = 0;
	OnSeekBarChangeListener listener;
	TileRaster tileRaster;
	private boolean adjustThumb = true;
	public int portions = 0;

	public SlideBar(Context context, TileRaster tileRaster) {
		super(context);
		thumb = ResourceLoader.getBitmap(R.drawable.arrow);
		thumbPaint = ResourceLoader.getBitmap(R.drawable.arrow);
		thumbPressed = ResourceLoader.getBitmap(R.drawable.arrow_on);		
		back = ResourceLoader.getBitmap(R.drawable.zoom_scales_rotate);
		progress = ResourceLoader.getBitmap(R.drawable.zoom_scales_on_rotate);
		p = new Paint();
		p.setAntiAlias(true);
		oWidth = thumb.getHeight() + back.getHeight();
		// oWidth = back.getHeight() + (back.getHeight() / 3);
		this.setPadding(10, 0, 10, 0);
		this.tileRaster = tileRaster;
	}

	public SlideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		oOffset = this.getThumbOffset();
		oProgress = this.getProgress();
	}

	public SlideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		oHeight = height * 3 / 4;
		this.setMeasuredDimension(oWidth, oHeight + thumb.getHeight() + 1);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		left = l;
		right = r;
		top = t;
		bottom = b;
	}

	@Override
	protected void dispatchDraw(Canvas c) {
		try {
			super.dispatchDraw(c);
			updateThumb();

			int maxZoomLevel = tileRaster.getMRendererInfo().getZOOM_MAXLEVEL();
			float portion = oHeight / (float) maxZoomLevel;
			float j = (float) ((float) thumb.getHeight() / 2.0);
			for (int i = 0; i <= maxZoomLevel; i++) {
				c.drawBitmap(back, j, 0, p);
				j += portion;
			}

			j = (float) thumb.getHeight() / 2.0f;
			int currentZoom = tileRaster.getTempZoomLevel();
			float last = j;
			for (int i = 0; i <= currentZoom; i++) {
				last = j;
				c.drawBitmap(progress, j, 0, p);
				j += portion;				
			}

			float x = (float) (last) - ((float) back.getWidth() / 2.0f);
			if (!adjustThumb)
				x = (float) (((float) thumbPos) - ((float) back.getWidth() / 2.0f));

			// if (x < v.getHeight() / 2.0f)
			// x = (v.getHeight() / 2) - (back.getWidth() / 2);

			c.drawBitmap(thumbPaint, x, back.getHeight() / 2, p);
		} catch (Exception e) {

		}
	}

	protected void onDraw(Canvas c) {
		c.rotate(90);
		c.translate(0, -oWidth);
		// super.onDraw(c);
	}

	public boolean onTouchEvent(MotionEvent event) {
		try {
			xPos = event.getX();
			yPos = event.getY();
			int p = (int) (100 * yPos / ((this.oHeight + ((float) thumb
					.getHeight() / 2.0f))));
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				thumbPaint = thumbPressed;
				listener.onStartTrackingTouch(this);
				this.setProgress(p);
				updateThumb();
				adjustThumb = false;				
				listener.onProgressChanged(this, this.getProgress(), true);				
				tileRaster.postInvalidate();
				return true;
			case MotionEvent.ACTION_MOVE:				
				thumbPaint = thumbPressed;
				oOffset = this.getThumbOffset();
				oProgress = this.getProgress();
				
				this.setProgress(p);
				updateThumb();
				adjustThumb = false;
				listener.onProgressChanged(this, this.getProgress(), true);
				tileRaster.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:
				thumbPaint = thumb;				
				listener.onStopTrackingTouch(this);
				this.setProgress(p);
				updateThumb();				
				tileRaster.postInvalidate();
				adjustThumb = true;
			}
		} catch (Exception e) {

		} finally {
			return true;
		}
	}

	public void updateThumb() {
		thumbPos = (float) this.getProgress()
				* (this.oHeight + ((float) thumb.getHeight() / 2.0f)) / 100.0f;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	public void setThumbOffset(int offset) {
		super.setThumbOffset(-10000);
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener seekListener) {
		super.setOnSeekBarChangeListener(seekListener);
		listener = seekListener;
	}
}
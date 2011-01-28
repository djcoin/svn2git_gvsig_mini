/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2011 Prodevelop.
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
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.views.overlay;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import es.prodevelop.gvsig.mini.R;

public class PopupView extends View {

	private Bitmap mid;
	private Bitmap left;
	private Bitmap right;
	private Bitmap arrow;

	private int x = 0;
	private int y = 0;

	private int textWidth = 0;
	private String text = "";
	private Rect bounds = new Rect();

	int offsetY = 2;

	int maxX;
	int maxY;

	// int panX;
	// int panY;

	private Paint textPaint = Paints.poiTextPaint;

	public PopupView(Activity context) {
		super(context);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		if (metrics.densityDpi != DisplayMetrics.DENSITY_LOW) {
			textPaint = Paints.poiHighTextPaint;
			offsetY = 4;
		}
		mid = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.mid);
		left = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.left_round);
		right = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.right_round);
		arrow = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.arrow_24);
	}

	public void setMaxSize(int x, int y) {
		maxX = x - 50;
		maxY = y;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setText(String text) {
		textWidth = (int) textPaint.measureText(text, 0, text.length());

		boolean modifiedText = false;
		while (textWidth > maxX) {
			text = text.substring(0, text.length() - 1);
			textWidth = (int) textPaint.measureText(text, 0, text.length());
			modifiedText = true;
		}

		if (modifiedText)
			text += "...";
		this.text = text;

		textPaint.getTextBounds(text, 0, text.length(), bounds);
	}

	public int getParts() {
		int size = textWidth / mid.getWidth();
		size++;
		if (size == 1) {
			size = 2;
		}
		return size;
	}

	@Override
	protected void dispatchDraw(final Canvas canvas) {
		try {
			// int x = (panX != 0) ? panX : this.x;
			// int y = (panY != 0) ? panY : this.y;

			if (this.getVisibility() == View.VISIBLE
					&& text.trim().length() > 0) {

			} else {
				return;
			}
			// Log.d("", "drawPopup");

			int size = getParts();

			boolean isEven = (size % 2) == 0;

			int count = 1;
			int pair = 0;
			int offset = 0;
			int i = 0;
			if (!isEven) {
				offset = mid.getWidth() / 2;
				canvas.drawBitmap(mid, x - offset,
						y - arrow.getHeight() - mid.getHeight() + offsetY,
						Paints.pcenter);
				size--;
			}

			for (i = 0; i < size; i++) {

				if (pair == 2) {
					pair = 0;
					count++;
				}

				pair++;

				int sign = 1;
				if (i % 2 == 0)
					sign = -1;

				if (sign > 0)
					count--;

				canvas.drawBitmap(mid, x + (sign * (mid.getWidth() * count))
						+ (sign * offset),
						y - arrow.getHeight() - mid.getHeight() + offsetY,
						Paints.pcenter);
				if (sign > 0)
					count++;
			}
			canvas.drawBitmap(left,
					x - (mid.getWidth() * count) - left.getWidth() - offset, y
							- arrow.getHeight() - mid.getHeight() + offsetY,
					Paints.pcenter);
			canvas.drawBitmap(right, x + (mid.getWidth() * (count)) + offset, y
					- arrow.getHeight() - mid.getHeight() + offsetY,
					Paints.pcenter);

			canvas.drawBitmap(arrow, x - (arrow.getWidth() / 2),
					y - (arrow.getHeight()), Paints.pcenter);

			canvas.drawText(text, x - (textWidth / 2), y - arrow.getHeight()
					- ((bounds.bottom - bounds.top)) + (2 * offsetY), textPaint);
			// canvas.drawBitmap(left, x - mid.getWidth() / 2 - left.getWidth(),
			// y - mid.getHeight()/2, Paints.pcenter);

			super.dispatchDraw(canvas);
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		int w = getPartsWidth();
		int h = getPartsHeight();
		super.onMeasure(w, h);
		setMeasuredDimension(w, h);
	}

	public int getPartsWidth() {
		int w = getParts() * mid.getWidth() + left.getWidth()
				+ right.getWidth();
		return w;
	}

	public int getPartsHeight() {
		int h = mid.getHeight() + arrow.getHeight();
		return h;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (this.getVisibility() == View.VISIBLE) {

		} else {
			return false;
		}
		int w = getPartsWidth();
		int h = getPartsHeight();
		if (event.getX() > (x - (w / 2)) && event.getX() < (x + w / 2)
				&& event.getY() > (y - h - arrow.getHeight() + offsetY)
				&& event.getY() < (y - arrow.getHeight()) + offsetY)
			return true;

		return false;
	}

	// public void updatePos(int x, int y) {
	// panX = this.x + x;
	// panY = this.y + y;
	// invalidate();
	// }
	//
	// public void incrementPos(int x, int y) {
	// panX = 0;
	// panY = 0;
	// this.x += x;
	// this.y += y;
	// invalidate();
	// }
}

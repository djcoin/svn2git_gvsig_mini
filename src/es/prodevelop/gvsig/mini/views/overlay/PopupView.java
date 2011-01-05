package es.prodevelop.gvsig.mini.views.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
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

	public PopupView(Context context) {
		super(context);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
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
		textWidth = (int) Paints.poiTextPaint.measureText(text, 0,
				text.length());

		boolean modifiedText = false;
		while (textWidth > maxX) {
			text = text.substring(0, text.length() - 1);
			textWidth = (int) Paints.poiTextPaint.measureText(text, 0,
					text.length());
			modifiedText = true;
		}

		if (modifiedText)
			text += "...";
		this.text = text;

		Paints.poiTextPaint.getTextBounds(text, 0, text.length(), bounds);
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
//			Log.d("", "drawPopup");

			int size = getParts();

			boolean isEven = (size % 2) == 0;

			int count = 1;
			int pair = 0;
			int offset = 0;
			int i = 0;
			if (!isEven) {
				offset = mid.getWidth() / 2;
				canvas.drawBitmap(mid, x - offset,
						y - arrow.getHeight() - mid.getHeight() + 2,
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
					- ((bounds.bottom - bounds.top)), Paints.poiTextPaint);
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

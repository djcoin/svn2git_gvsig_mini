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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;



import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * A ViewGroup to layout Views in a Cirle with animation. The length of the
 * circunference is divided into the number of views of the CircularRouleteView
 * @author aromeu 
 * @author rblanco
 *
 */
public class CircularRouleteView extends ViewGroup {
	private int radius = 0;
	private Bitmap center;
	private final static Logger log = Logger
			.getLogger(CircularRouleteView.class.getName());
	Paint p;
	Paint pcenter;
	Path path;

	/**
	 * The constructor
	 * @param context
	 */
	public CircularRouleteView(Context context) {
		super(context);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
//			log.setClientID(this.toString());
			p = new Paint();
			pcenter = new Paint();
			pcenter.setAntiAlias(true);
			pcenter.setAlpha(230);
			p.setAntiAlias(true);
			p.setARGB(75, 49, 49, 49);
			path = new Path();

			p.setStyle(Paint.Style.FILL);
			center = ResourceLoader.getBitmap(R.drawable.center_focus);
//			log.setLevel(Utils.LOG_LEVEL);
		} catch (OutOfMemoryError e) {			
			System.gc();
			log.log(Level.SEVERE,"",e);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}

	}

	/**
	 * The constructor
	 * @param context
	 * @param animate True if applies animation
	 */
	public CircularRouleteView(Context context, boolean animate) {
		this(context);
		try {
			if (animate) {
				AnimationSet set = new AnimationSet(true);
				Animation animation = new AlphaAnimation(0.0f, 1.0f);
				animation.setDuration(300);
				set.addAnimation(animation);
				Animation animation2 = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				animation2.setDuration(300);
				set.addAnimation(animation2);
				Animation animation1 = new ScaleAnimation(0, 1, 0, 1,
						Animation.RELATIVE_TO_SELF, 1,
						Animation.RELATIVE_TO_SELF, 1);
				animation1.setDuration(300);
				set.addAnimation(animation1);
				LayoutAnimationController controller = new LayoutAnimationController(
						set, 0.25f);
				setLayoutAnimation(controller);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	@Override
	public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
		location[0] = getLeft();
		location[1] = getTop();
		dirty.set(0, 0, getWidth(), getHeight());
		return getParent();
	}

	@Override
	protected void dispatchDraw(final Canvas canvas) {
		try {
			canvas.drawBitmap(center, (width / 2) - (center.getWidth() / 2),
					(height / 2) - (center.getHeight() / 2), pcenter);
			path.rewind();
			path.addCircle(width / 2, height / 2, radius, Path.Direction.CW);
			path.moveTo(0, 0);
			path.lineTo(0, height);
			path.lineTo(width, height);
			path.lineTo(width, 0);
			canvas.drawPath(path, p);			
			super.dispatchDraw(canvas);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	private int width = 0;
	private int height = 0;
	private int minX = 99999;
	private int minY = 99999;
	private int childWidth = 0;

	@Override
	protected void onLayout(final boolean changed, final int l, final int t,
			final int r, final int b) {
		final int width = this.getRight() - this.getLeft();
		final int height = this.getBottom() - this.getTop();
		this.width = width;
		this.height = height;
		final int count = getChildCount();
		double angle = 360 / count;
		if (width > height) {
			radius = height / 3;
		} else {
			radius = width / 3;
		}
		int offsetX = width / 2;
		int offsetY = height / 2;

		for (int i = 0; i < count; i++) {
			final View view = getChildAt(i);
			this.childWidth = childWidth;
			final int childWidth = view.getMeasuredWidth();
			final int childHeight = view.getMeasuredHeight();
			if (i == 0) {
				offsetX -= childWidth / 2;
				offsetY -= childHeight / 2;
			}
			final int childLeft = (int) Math.round((Math.sin(Math
					.toRadians(angle * i)) * radius))
					+ offsetX;
			final int childTop = (int) Math.round((Math.cos(Math
					.toRadians(angle * i)) * radius))
					+ offsetY;
			if (childLeft < minX)
				minX = childLeft;
			if (childTop < minY)
				minY = childTop;

			view.layout(childLeft, childTop, childLeft + childWidth, childTop
					+ childHeight);
		}
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		final int w = getDefaultSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int h = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(w, h);
		}

		super.onMeasure(w, h);
		setMeasuredDimension(w, h);
	}

	public int getRadius() {
		return this.radius;
	}
}

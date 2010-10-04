package es.prodevelop.gvsig.mini.views.overlay;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * Static Paints to use anywhere
 * @author aromeu
 *
 */
public class Paints {
	
	public static Paint filledPaint;
	public static Paint rectanglePaint;
	
	public static Paint pathPaint;
	public static Paint circlePaint;
	
	public static Paint p;
	public static Paint pcenter;
	
	public static Paint mPaintR = new Paint();
	public static Paint whitePaint = new Paint();
	public static Paint normalPaint = new Paint();
	public static Paint rotatePaint = new Paint();
	
	public static Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	public static Paint circlePaintV = new Paint();
	
	
	
	static {
		/** ACETATE OVERLAY **/
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
		/** ACETATE OVERLAY **/
		
		/** GEOM DRAWER **/		
		pathPaint = new Paint();
		pathPaint.setAntiAlias(false);
		pathPaint.setStrokeWidth(4);
		pathPaint.setStyle(Paint.Style.STROKE);
		pathPaint.setARGB(150, 137, 0, 182);

		circlePaint = new Paint();
		circlePaint.setAntiAlias(false);
		circlePaint.setStrokeWidth(4);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		circlePaint.setARGB(50, 137, 0, 182);
		/** GEOM DRAWER **/
		
		
		/** ROULETTE VIEW **/
		p = new Paint();
		pcenter = new Paint();
		pcenter.setAntiAlias(true);
		pcenter.setAlpha(230);
		p.setAntiAlias(true);
		p.setARGB(75, 49, 49, 49);
		p.setStyle(Paint.Style.FILL);
		/** ROULETTE VIEW **/
		
		rotatePaint.setFlags(Paint.FILTER_BITMAP_FLAG);
		whitePaint.setColor(Color.WHITE);
		
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setARGB(255, 137, 0, 182);
		mPaint.setStrokeWidth(1);

		circlePaintV.setAntiAlias(true);
		circlePaintV.setStyle(Paint.Style.FILL);
		circlePaintV.setARGB(25, 137, 0, 182);
	}
}

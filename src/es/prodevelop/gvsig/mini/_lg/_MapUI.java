package es.prodevelop.gvsig.mini._lg;

import java.util.logging.Level;

import com.markupartist.android.widget.ActionBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ZoomControls;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.views.overlay.CircularRouleteView;
import es.prodevelop.gvsig.mini.views.overlay.SlideBar;
import es.prodevelop.gvsig.mini.views.overlay.ViewSimpleLocationOverlay;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class _MapUI extends VanillaMap {

	private static final String TAG = _MapUI.class.getName();
	
	protected RelativeLayout rl;
	// comming from the Map
	protected ZoomControls z = null;
	protected CircularRouleteView c;
	
	protected SlideBar s;
	protected ActionBar actionBar;


	@Override
	protected void postCreate() {
		this.setContentView(rl);

		LayoutInflater inflater = getLayoutInflater();
		getWindow().addContentView(
				inflater.inflate(R.layout.actionbars, null),
				new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		

		/*
		 * Add items and set the contentbar title
		 */

		actionBar.setTitle(R.string.action_bar_title);
		
	}
	
	@Override
	protected void preCreate() {
		rl = new RelativeLayout(this);
	};
	
	
	public ActionBar getActionbar() {
		return this.actionBar;
	}
	
	public void setActionbar(ActionBar actionbar) {
		this.actionBar = actionbar;
	}
	
	
	/**
	 * Instantiates the UI: TileRaster, ZoomControls, SlideBar in a
	 * RelativeLayout
	 * 
	 * @param savedInstanceState
	 */
	public void loadUI(Bundle savedInstanceState) {
		try {
			log.log(Level.FINE, "load UI");
			final LayoutInflater factory = LayoutInflater.from(this);
			rl.addView(this.osmap, new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			z = new ZoomControls(this);
			final TextView l = new TextView(this);
			final RelativeLayout.LayoutParams sParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			//
			rl.addView(l, sParams);

			/* Creating the main Overlay */
			{
				this.mMyLocationOverlay = new ViewSimpleLocationOverlay(this,
						osmap, ViewSimpleLocationOverlay.DEFAULT_NAME);

//				this.osmap.addOverlay(new NameFinderOverlay(this, osmap,
//						NameFinderOverlay.DEFAULT_NAME));
//				this.osmap.addOverlay(new RouteOverlay(this, osmap,
//						RouteOverlay.DEFAULT_NAME));
				this.osmap.addOverlay(mMyLocationOverlay);
			}

			final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			zzParams.addRule(RelativeLayout.ALIGN_BOTTOM);
			zzParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rl.addView(z, zzParams);
			z.setId(107);
			z.setVisibility(View.VISIBLE);

			z.setOnZoomInClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Log.d(TAG, "zooming in !!!");
						_MapUI.this.osmap.zoomIn();

					} catch (Exception e) {
						log.log(Level.SEVERE, "onZoomInClick: ", e);
					}
				}
			});
			z.setOnZoomOutClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Log.d(TAG, "zooming out !!!");
						_MapUI.this.osmap.zoomOut();
						// Map.this.osmap.switchPanMode();
					} catch (Exception e) {
						log.log(Level.SEVERE, "onZoomOutClick: ", e);
					}
				}
			});

			s = new SlideBar(this, this.osmap);

			final RelativeLayout.LayoutParams slideParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			// slideParams.addRule(RelativeLayout.ABOVE,
			// z.getId());
			s.setVisibility(View.INVISIBLE);
			slideParams.addRule(RelativeLayout.ALIGN_TOP);
			slideParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			slideParams.addRule(RelativeLayout.ALIGN_RIGHT);
			slideParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					int zoom = (int) Math.floor(progress
							* (_MapUI.this.osmap.getMRendererInfo()
									.getZOOM_MAXLEVEL() + 1 - _MapUI.this.osmap
									.getMRendererInfo().getZoomMinLevel())
							/ 100);
					osmap.drawZoomRectangle(zoom);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					try {
						int zoom = (int) Math.floor(seekBar.getProgress()
								* (_MapUI.this.osmap.getMRendererInfo()
										.getZOOM_MAXLEVEL() + 1 - _MapUI.this.osmap
										.getMRendererInfo().getZoomMinLevel())
								/ 100);
						// int zoom = ((SlideBar)seekBar).portions;
						_MapUI.this.updateSlider(zoom);
						_MapUI.this.osmap.setZoomLevel(zoom, true);
						_MapUI.this.osmap.cleanZoomRectangle();
					} catch (Exception e) {
						log.log(Level.SEVERE, "onStopTrackingTouch: ", e);
					}
				}
			});

			rl.addView(s, slideParams);

			/* Controls */
			{

				// View ivLayers = (View) factory.inflate(
				// R.layout.layers_image_button, null);
				// ivLayers.setId(117);
				// final RelativeLayout.LayoutParams layersParams = new
				// RelativeLayout.LayoutParams(
				// RelativeLayout.LayoutParams.WRAP_CONTENT,
				// RelativeLayout.LayoutParams.WRAP_CONTENT);
				// layersParams.addRule(RelativeLayout.ALIGN_TOP);
				// layersParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				//
				// rl.addView(ivLayers, layersParams);
				//
				// ivLayers.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// try {
				// viewLayers();
				// } catch (Exception e) {
				// log.log(Level.SEVERE, "onLayersClick: ", e);
				// osmap.postInvalidate();
				// }
				// }
				// });
				this.updateSlider();
			}
			log.log(Level.FINE, "ui loaded");
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError ou) {
			System.gc();
			log.log(Level.SEVERE, "", ou);
		}
	}



	/**
	 * This method sinchronizes the SlideBar position with the current
	 * TileRaster zoom level
	 */
	public void updateSlider() {
		try {
			int progress = Math
					.round(this.osmap.getTempZoomLevel()
							* 100
							/ (this.osmap.getMRendererInfo().getZOOM_MAXLEVEL() + 1 - _MapUI.this.osmap
									.getMRendererInfo().getZoomMinLevel()));
			// if (progress == 0)
			// progress = 1;
			this.s.setProgress(progress);
			this.updateZoomControl();
		} catch (Exception e) {
			log.log(Level.SEVERE, "updateSlider: ", e);
		}
	}

	
	/**
	 * Forces to update the SlideBar to an specific zoom level
	 * 
	 * @param zoom
	 *            The zoom level
	 */
	public void updateSlider(int zoom) {
		try {
			int progress = zoom
					* 100
					/ (this.osmap.getMRendererInfo().getZOOM_MAXLEVEL() + 1 - _MapUI.this.osmap
							.getMRendererInfo().getZoomMinLevel());
			// if (progress == 0)
			// progress = 1;
			this.s.setProgress(progress);
			this.updateZoomControl();
		} catch (Exception e) {
			log.log(Level.SEVERE, "updateSlider: ", e);
		}
	}

	/**
	 * Switchs from visible to invisible an viceversa the SlideBar
	 */
	public void switchSlideBar() {
		try {
			int size = rl.getChildCount();
			View v;
			boolean hasCircularView = false;
			for (int i = 0; i < size; i++) {
				v = rl.getChildAt(i);
				if (v instanceof CircularRouleteView) {
					hasCircularView = true;
				}
			}
			if (hasCircularView) {
				clearContext();
				return;
			}

			if (s.getVisibility() == View.VISIBLE) {
				s.setVisibility(View.INVISIBLE);
			} else {
				s.setVisibility(View.VISIBLE);
			}

			clearContext();
			// Update context for the Scale bar has been displayed
			userContext.setLastExecScaleBar();

		} catch (Exception e) {
			log.log(Level.SEVERE, "switchSlideBar: ", e);
		}
	}

	
	/**
	 * Synchronize zoomcontrols with TileRaster.MapRenderer zoom level
	 */
	public void updateZoomControl() {
		try {
			MapRenderer r = _MapUI.this.osmap.getMRendererInfo();

			if (r.getZoomLevel() > r.getZoomMinLevel())
				z.setIsZoomOutEnabled(true);
			else
				z.setIsZoomOutEnabled(false);

			if (r.getZOOM_MAXLEVEL() > r.getZoomLevel()) {
				z.setIsZoomInEnabled(true);
			} else {
				z.setIsZoomInEnabled(false);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "updateZoomControl: ", e);
		}
	}


}

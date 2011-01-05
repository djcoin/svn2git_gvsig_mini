package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProviderListener;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.OsmPOISymbolSelector;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class ExpandedClusterOverlay extends PointOverlay implements
		QuadtreeProviderListener {

	private double visibleResolution = 0;

	private Cluster mClusterToExpand;
	private PerstOsmPOIClusterProvider mPoiProvider;

	private QuadtreeProviderListener clusterRemovedListener;
	private boolean showRect = false;

	public ExpandedClusterOverlay(Context context, TileRaster tileRaster,
			String name, Cluster clusterToExpand, boolean showRect) {
		super(context, tileRaster, name);
		this.showRect = showRect;
		visibleResolution = tileRaster.getMRendererInfo().getCurrentRes();
		this.mClusterToExpand = clusterToExpand;
		mPoiProvider = POIProviderManager.getInstance().getPOIProvider();
		// this.setName(String.valueOf(clusterToExpand.getID()));
		setSymbolSelector(new OsmPOISymbolSelector());
	}

	public void getPOIsOfClusterAsynch() {
		try {
			mPoiProvider.expandClusterAsynch(mClusterToExpand, this);
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	@Override
	public void onClustersRetrieved(Collection clusters, String category,
			boolean clearPrevious, Cancellable cancellable) {
		if (clusterRemovedListener != null)
			clusterRemovedListener.onClustersRetrieved(clusters, category,
					clearPrevious, cancellable);

	}

	@Override
	public void onPOISRetrieved(Collection pois, boolean clearPrevious,
			Cancellable cancellable) {
		convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo()
				.getSRS(), (ArrayList) pois, cancellable);
		this.setPoints((ArrayList) pois);
	}

	@Override
	public void onClusterExpanded(Collection pois, boolean clearPrevious,
			Cancellable cancellable, Cluster clusterExpanded) {
		onPOISRetrieved(pois, clearPrevious, cancellable);
	}

	@Override
	public void onExtentChanged(Extent newExtent, int zoomLevel,
			double resolution) {

		this.setVisible(resolution <= visibleResolution);

	}

	public void setClusterRemovedListener(
			QuadtreeProviderListener clusterRemovedListener) {
		this.clusterRemovedListener = clusterRemovedListener;
	}

	public QuadtreeProviderListener getClusterRemovedListener() {
		return clusterRemovedListener;
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		super.onDraw(c, maps);
		final MapRenderer renderer = getTileRaster().getMRendererInfo();

		if (!showRect || !isVisible())
			return;
		Extent boundingBox = this.mClusterToExpand.getBoundingBox();
		double[] minXY = ConversionCoords.reproject(boundingBox.getMinX(),
				boundingBox.getMinY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS(renderer.getSRS()));

		double[] maxXY = ConversionCoords.reproject(boundingBox.getMaxX(),
				boundingBox.getMaxY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS(renderer.getSRS()));

		int[] minPixXY = renderer.toPixels(minXY);
		int[] maxPixXY = renderer.toPixels(maxXY);

		c.drawRect((float) minPixXY[0], (float) maxPixXY[1],
				(float) maxPixXY[0], (float) minPixXY[1], Paints.circlePaint);

		c.drawBitmap(ResourceLoader.getBitmap(R.drawable.add_48),
				maxPixXY[0] - 24, maxPixXY[1] - 24, Paints.mPaintR);
	}

	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		if (showRect) {
			final MapRenderer renderer = getTileRaster().getMRendererInfo();

			Extent boundingBox = this.mClusterToExpand.getBoundingBox();
			double[] minXY = ConversionCoords.reproject(boundingBox.getMinX(),
					boundingBox.getMinY(), CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS(renderer.getSRS()));

			double[] maxXY = ConversionCoords.reproject(boundingBox.getMaxX(),
					boundingBox.getMaxY(), CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS(renderer.getSRS()));

			int[] minPixXY = renderer.toPixels(minXY);
			int[] maxPixXY = renderer.toPixels(maxXY);

			Pixel addIconPos = new Pixel(maxPixXY[0] - 24, maxPixXY[1] - 24);

			Pixel p = new Pixel((int) e.getX(), (int) e.getY());
			if (p.distance(addIconPos) < 48) {
				showRect = false;
				this.mClusterToExpand.setExpanded(true);
			} else {
				this.getTileRaster().removeOverlay(this.getName());
				destroy();
			}
			return false;
		} else {
			return super.onSingleTapUp(e, osmtile);
		}
	}

	// @Override
	// public void onLayerChanged(String layerName) {
	// if (getPoints() == null)
	// return;
	// MapRenderer newRenderer;
	// try {
	// newRenderer = Layers.getInstance().getRenderer(layerName);
	// convertCoordinates(renderer.getSRS(), newRenderer.getSRS(),
	// getPoints(), null);
	// renderer = newRenderer;
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// Log.e("", e.getMessage());
	// }
	// }

}

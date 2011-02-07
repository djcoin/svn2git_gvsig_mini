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

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.memory.cluster.ClusterNode;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProviderListener;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.osmpoi.OSMPOIContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
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

	private QuadtreeProviderListener clusterRemovedListener;
	private boolean showRect = false;

	private String cat = "";

	private Bitmap addIcon;
	private ClusterNode mNode;

	public ExpandedClusterOverlay(Context context, TileRaster tileRaster,
			String name, Cluster clusterToExpand, boolean showRect) {
		super(context, tileRaster, name);
		this.showRect = showRect;
		visibleResolution = tileRaster.getMRendererInfo().getCurrentRes();
		this.mClusterToExpand = clusterToExpand;
		// this.setName(String.valueOf(clusterToExpand.getID()));
		setSymbolSelector(new OsmPOISymbolSelector());
		cat = name.split("#")[0];
		addIcon = ResourceLoader.getBitmap(R.drawable.add_48);
	}

	public void getPOIsOfClusterAsynch() {
		try {
			if (checkRemove())
				return;
			POIProviderManager.getInstance().getPOIProvider()
					.expandClusterAsynch(mClusterToExpand, this);
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
		if (checkRemove())
			return;
		setNodeExpanded(true);
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

	private boolean checkRemove() {
		if (!POICategories.selected.contains(cat)) {
			getTileRaster().removeOverlay(getName());
			destroy();
			return true;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		super.onDraw(c, maps);
		final MapRenderer renderer = getTileRaster().getMRendererInfo();

		if (!showRect || !isVisible() || checkRemove())
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

		c.drawBitmap(addIcon, maxPixXY[0] - (addIcon.getWidth() / 2),
				maxPixXY[1] - (addIcon.getHeight() / 2), Paints.mPaintR);
	}

	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		if (!isVisible())
			return false;
		if (showRect) {
			final MapRenderer renderer = getTileRaster().getMRendererInfo();

			Extent boundingBox = this.mClusterToExpand.getBoundingBox();
			// double[] minXY =
			// ConversionCoords.reproject(boundingBox.getMinX(),
			// boundingBox.getMinY(), CRSFactory.getCRS("EPSG:4326"),
			// CRSFactory.getCRS(renderer.getSRS()));

			double[] maxXY = ConversionCoords.reproject(boundingBox.getMaxX(),
					boundingBox.getMaxY(), CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS(renderer.getSRS()));

			// int[] minPixXY = renderer.toPixels(minXY);
			int[] maxPixXY = renderer.toPixels(maxXY);

			Pixel addIconPos = new Pixel(
					maxPixXY[0] - (addIcon.getWidth() / 2), maxPixXY[1]
							- (addIcon.getHeight() / 2));

			Pixel p = new Pixel((int) e.getX(), (int) e.getY());
			if (p.distance(addIconPos) < addIcon.getWidth()) {
				showRect = false;
				setNodeExpanded(true);
			} else {
				this.getTileRaster().removeOverlay(this.getName());
				destroy();
			}
			return true;
		} else {
			return super.onSingleTapUp(e, osmtile);
		}
	}

	public void setNodeExpanded(final boolean expanded) {
		this.mClusterToExpand.setExpanded(expanded);
		try {
			if (mNode == null)				
				mNode = POIProviderManager.getInstance().getPOIProvider()
						.getClusterTree(cat)
						.getNodeByID(this.mClusterToExpand.getID());
			mNode.setExpanded(expanded);
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public boolean isRemovable(String categoryExpandedName) {
		return (showRect || getName().startsWith(categoryExpandedName));
	}

	public boolean isRemovable() {
		return (showRect);
	}

	public void destroy() {
		super.destroy();
		setNodeExpanded(false);
	}
	
	@Override
	public ItemContext getItemContext() {
		try {
			return new OSMPOIContext(getTileRaster().map, false, false,
					(Point) getPoints().get(getSelectedIndex()));
		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
			return null;
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

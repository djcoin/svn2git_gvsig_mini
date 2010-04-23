/* SIGATEX. Gestión de Recursos Turísticos de Extremadura
 * Funded by European Union. FEDER program.
 *
 * Copyright (C) 2008 Junta de Extremadura.
 * Developed by: Prodevelop, S.L.
 * For more information, contact:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-  1307,USA.
 *
 * For more information, contact:
 *
 * Junta de Extremadura
 * Consejería de Cultura y Turismo
 * C/ Almendralejo 14 Mérida
 * 06800 Badajoz
 * SPAIN
 *
 * Tel: +34 924007009
 * http://www.culturaextremadura.com
 *
 * or
 *
 * Prodevelop, S.L.
 * Pza. Juan de Villarrasa, 14 - 5
 * Valencia
 * Spain
 * Tel: +34 963510612
 * e-mail: prode@prodevelop.es
 * http://www.prodevelop.es
 * 
 * Modified for gvSIG Mini.
 *
 */
package es.prodevelop.gvsig.mini.map;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;

/**
 * This class knows how to convert screen coordinates into map coordinates and
 * viceversa
 * 
 * @author Alberto Romeu Carrasco (aromeu@prodevelop.es)
 * @author aromeu 
 * @author rblanco - modified for gvSIG Mini
 */
public class ViewPort {

	private double dist1Pixel;
	public static double[] resolutions = new double[] { 1100, 550, 275,
			100, 50, 25, 10, 5, 2, 1, 0.5 };

	public Point origin = new Point(258000, 4485000);

	/**
	 * The constructor
	 */
	public ViewPort() {
		dist1Pixel = resolutions[0];
	}

	/**
	 * Returns a real point from a screen pixel
	 * 
	 * @param pixel
	 *            The screen pixel
	 * @return A Point in the real world coordinates using the SRS of the
	 *         baseLayer of the map
	 */
	public Point toMapPoint(final Pixel pixel, final int width,
			final int height, final Point center) {
		Point coordinate = null;
		try {

			final double deltaX = (double) pixel.getX() - (width / 2);
			final double deltaY = (double) pixel.getY() - (height / 2);

			coordinate = new Point(center.getX() + deltaX * this.dist1Pixel,
					center.getY() - deltaY * this.dist1Pixel);

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return coordinate;
	}
	
	public static double[] toMapPoint(final int[] pixel, final int width,
			final int height, Point center, final double res) {
		double[] coordinate = null;
		try {

			final double deltaX = (double) pixel[0] - (width / 2);
			final double deltaY = (double) pixel[1] - (height / 2);

			coordinate = new double[]{center.getX() + deltaX * res,
					center.getY() - deltaY * res};

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return coordinate;
	}

	/**
	 * Returns a screen pixel from a real point
	 * 
	 * @param coordinates
	 *            The map point
	 * @return A screen pixel
	 */
	public Pixel fromMapPoint(final Point coordinates, final double extentMinX,
			final double extentMaxY) {

		return new Pixel((int) Math.ceil(1 / this.dist1Pixel
				* (coordinates.getX() - extentMinX)), (int) Math.ceil(1
				/ this.dist1Pixel * (extentMaxY - coordinates.getY())));
	}

	/**
	 * Returns a screen pixel from a real point
	 * 
	 * @param coordinates
	 *            The map point
	 * @return A screen pixel
	 */
	public int[] fromMapPoint(final double[] coordinates,
			final double extentMinX, final double extentMaxY) {

		return new int[] {
				((int) (1 / this.dist1Pixel
						* (coordinates[0] - extentMinX))),
				(int) (1 / this.dist1Pixel
						* (extentMaxY - coordinates[1])) };
	}
	
	public static int[] fromMapPoint(final double[] coordinates,
			final double extentMinX, final double extentMaxY, double res) {

		return new int[] {
				((int) (1 / res
						* (coordinates[0] - extentMinX))),
				(int) (1 / res
						* (extentMaxY - coordinates[1]))};
	}

	/**
	 * calculates de distance in pixels from a real distance
	 * 
	 * @param distance
	 * @return distance in pixels
	 */
	public int fromMapDistance(final double distance) {
		return (int) (distance / this.dist1Pixel);
	}

	/**
	 * calculates de distance in the real world coordinates from a screen pixels
	 * distance
	 * 
	 * @param distance
	 * @return real distance
	 */
	public double toMapDistance(final int distance) {
		return distance * this.dist1Pixel;
	}

	/**
	 * Calculates the extent of the map given the center of the map and a
	 * resolution
	 * 
	 * @param centerCoordinate
	 *            The center of the map
	 * @param zoomResolution
	 *            The resolution of a pixel of the map
	 * @return The Extent of the map
	 */
	public static Extent calculateExtent(final Point centerCoordinate,
			final double zoomResolution, final int w, final int h) {

		// System.out.println("width, height: "+size.getWidth()+","+size.getHeight());
		final double width = w * zoomResolution;
		final double height = h * zoomResolution;

		return (new Extent(centerCoordinate.getX() - width / 2,
				centerCoordinate.getY() - height / 2, centerCoordinate.getX()
						+ width / 2, centerCoordinate.getY() + height / 2));
	}

	/**
	 * Calculates the extent of the map with the current center of the map and
	 * the current resolution
	 * 
	 * @return The Extent of the map
	 */
	public Extent calculateExtent(final int w, final int h, final Point center) {

		final double width = w * this.dist1Pixel;
		final double height = h * this.dist1Pixel;

		return (new Extent(center.getX() - width / 2, center.getY() - height
				/ 2, center.getX() + width / 2, center.getY() + height / 2));
	}

	/**
	 * @return the dist1Pixel
	 */
	public double getDist1Pixel() {
		return dist1Pixel;
	}

	/**
	 * @param dist1Pixel
	 *            the dist1Pixel to set
	 */
	public void setDist1Pixel(final double dist1Pixel) {
		this.dist1Pixel = dist1Pixel;
	}
}

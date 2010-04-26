/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   http://www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integraci�n de Tecnolog�as SL
 *   Conde Salvatierra de �lava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 *
 *    or
 *
 *   Instituto de Rob�tica
 *   Apartado de correos 2085
 *   46071 Valencia
 *   (Spain)
 *   
 *   +34 963 543 577
 *   jjordan@robotica.uv.es
 *   http://robotica.uv.es
 *   
 */

package es.prodevelop.gvsig.mini.phonecache;



/**
 * Basic projection class.
 * 
 * @see es.prodevelop.geodetic.utils.conversion.GeoUtils
 * 
 * @author jldominguez
 *
 */
public class Projection  {

	private String abbreviation = null;
	private String unit = "?";
	private double metersPerUnit = 1;
	
	/**
	 * Constructor
	 * @param ab abbreviation
	 */
	public Projection(String ab, String u, double meters_per_unit) {
		abbreviation = ab;
		unit = u;
		metersPerUnit = meters_per_unit;
		
	}

	/**
	 * @see es.prodevelop.gvsig.mobile.fmap.proj.IProjection#getScale(double, double, double, double)
	 */
	public double getScale(double minX, double maxX, double img_width, double dpi) {
		
		double inches = img_width / dpi;
		double meters = inches * 0.0254; 
		return GeoUtils.getScale(this, maxX-minX, meters);
	}

	/**
	 * @see es.prodevelop.gvsig.mobile.fmap.proj.IProjection#getAbrev()
	 */
	public String getAbrev() {
		return abbreviation;
	}

	/**
	 * @see es.prodevelop.gvsig.mobile.fmap.proj.IProjection#getUnitsAbbrev()
	 */
	public String getUnitsAbbrev() {

		return unit;
//		if (abbreviation.length() == 9) return "�";
//		if (abbreviation.indexOf("EPSG:230") != -1) return "m";
//		if (abbreviation.indexOf("EPSG:326") != -1) return "m";
//		if (abbreviation.indexOf("EPSG:327") != -1) return "m";
//		return "u";
	}

	/**
	 * @see es.prodevelop.gvsig.mobile.fmap.proj.IProjection#getMetersPerProjUnit()
	 */
	public double getMetersPerProjUnit() {
		return metersPerUnit;
		
//		if (abbreviation.compareToIgnoreCase(GeoUtils.CRS_CODE_WGS_84) == 0) {
//			return 10000000.0 / 90.0;
//		} else {
//			return 1.0;
//		}
	}
}

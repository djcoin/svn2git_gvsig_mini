/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   http://www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
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
 *   Instituto de Robótica
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
 * Utility class to create projections.
 * 
 * @see es.prodevelop.gvsig.mobile.fmap.proj.IProjection
 * 
 * @author jldominguez
 *
 */
public class CRSFactory {

	/**
	 * Creates a projection for the given projection abbreviation
	 * 
	 * @param projName the projection abbreviation (example "EPSG:4326")
	 * @return the projection object
	 */
	public static Projection getCRS(String projName) {
		
		if ((projName.length() == 9) && (projName.indexOf("EPSG:4") == 0)) {
			return new Projection(projName, "g", 2 * Math.PI * 6378137 / 360.0);
		} else {
			if (projName.indexOf("EPSG:230") == 0) {
				return new Projection(projName, "m", 1.0);
			} else {
				if (projName.indexOf("EPSG:326") == 0) {
					return new Projection(projName, "m", 1.0);
				} else {
					if (projName.indexOf("EPSG:327") == 0) {
						return new Projection(projName, "m", 1.0);
					} else {
						if(GeoUtils.isMercatorProjection(projName)) {
							return new Projection(projName, "m", 2 * Math.PI * 6378137 / 360.0);
						} else {
							if ((projName.indexOf("EPSG:275") == 0) && (projName.length() == 10)) {
								return new Projection(projName, "m", 1.0);
							} else {
								if (projName.indexOf("EPSG:27700") == 0) {
									return new Projection(projName, "m", 1.0);
								} else {
									if (projName.indexOf("EPSG:900913") == 0) {
										return new Projection(projName, "m", 1.0);
									} else {
										return new Projection(projName, "u", 1.0);
									}									
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean isSupportedSRS(final String SRS) {
		Projection p = CRSFactory.getCRS(SRS);
		if (p.getUnitsAbbrev().compareTo("u") != 0) return true;
		return false;
	}
	
}

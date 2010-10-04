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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Pequeña y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.ors;

import java.util.Vector;

import es.prodevelop.gvsig.mini.geom.Extent;

public class ORSRoute {

	private Vector instructions;
	private Extent bBox;
	private double totalTime = 0;
	private double totalDistance = 0;

	public ORSRoute() {

	}

	public void addInstruction(ORSInstruction i) {
		if (instructions == null)
			instructions = new Vector();

		instructions.add(i);
	}

	public Vector getInstructions() {
		return instructions;
	}

	public void setBBox(Extent bBox) {
		this.bBox = bBox;
	}

	public void setBBox(double minX, double minY, double maxX, double maxY) {
		this.bBox = new Extent(minX, minY, maxX, maxY);
	}

	public Extent getBBox() {
		return bBox;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

}

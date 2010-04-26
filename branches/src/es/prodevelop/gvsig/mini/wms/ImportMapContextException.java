/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/* CVS MESSAGES:
*
* $Id: ImportMapContextException.java 29645 2009-06-29 17:03:35Z jpiera $
* $Log$
* Revision 1.1  2006-07-21 11:51:13  jaume
* improved appearance in wms panel and a wmc bug fixed
*
*
*/
package es.prodevelop.gvsig.mini.wms;
/**
 * Exception representing an error in the process of importing a Web Map Context
 * file into gvSIG.<br><br>
 *
 * The error can be critical or not defining if the process can be finished with the
 * intervention of the user of otherwise it is impossible.<br><br>
 *
 * You should check the <b>isCritical()</b> method in order to know if you can continue
 * importing data or not. If the answer of this method is <b>true</b>, aborting the
 * process is strongly encouraged; or execute further instructions at your own risk.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class ImportMapContextException extends Exception {
	private boolean critical;

	public ImportMapContextException(String message, boolean critical) {
		super(message);
		this.critical = critical;
	}

	/**
	 * If <b>true</b> then the elements could be created but with inconsistent values that
	 * the user can fix modifying its properties. <br><br>
	 *
	 * If <b>false</b> then it is not possible to create the elements.
	 * @return
	 */
	public boolean isCritical() {
		return critical;
	}
}

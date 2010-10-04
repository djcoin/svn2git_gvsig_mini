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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la PequeÒa y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.test;

import junit.framework.TestCase;
import es.prodevelop.gvsig.mini.openls.OpenLSRouteParser;
import es.prodevelop.gvsig.mini.ors.ORSInstruction;
import es.prodevelop.gvsig.mini.ors.ORSRoute;

public class TestORS extends TestCase {

	byte[] data;

	public void setUp() {
		data = ("<xls:XLS xmlns:xls=\"http://www.opengis.net/xls\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.1\" xsi:schemaLocation=\"http://www.opengis.net/xls http://schemas.opengis.net/ols/1.1.0/RouteService.xsd\">"
				+ "<xls:ResponseHeader xsi:type=\"xls:ResponseHeaderType\"/>"
				+ "<xls:Response xsi:type=\"xls:ResponseType\" requestID=\"123456789\" version=\"1.1\" numberOfResponses=\"1\">"
				+ "<xls:DetermineRouteResponse xsi:type=\"xls:DetermineRouteResponseType\">"
				+ "  <xls:RouteSummary>"
				+ "<xls:TotalTime>PT5M35S</xls:TotalTime>"
				+ "<xls:TotalDistance uom=\"YD\" value=\"2324.7\"/>"
				+ "<xls:BoundingBox srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0892488 50.7254372</gml:pos>"
				+ "<gml:pos>7.1038766 50.7323634</gml:pos>"
				+ "</xls:BoundingBox>"
				+ "</xls:RouteSummary>"
				+ "<xls:RouteGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0892567 50.7265543</gml:pos>"
				+ "<gml:pos>7.089248823722613 50.72653868037818</gml:pos>"
				+ "<gml:pos>7.089294 50.7265159</gml:pos>"
				+ "<gml:pos>7.090037 50.7261616</gml:pos>"
				+ "<gml:pos>7.0913708 50.7254372</gml:pos>"
				+ "<gml:pos>7.0917231 50.7257491</gml:pos>"
				+ "<gml:pos>7.0923754 50.7263577</gml:pos>"
				+ "<gml:pos>7.0924374 50.7264387</gml:pos>"
				+ "<gml:pos>7.0924494 50.7264436</gml:pos>"
				+ "<gml:pos>7.0925224 50.7264738</gml:pos>"
				+ "<gml:pos>7.0925963 50.7264979</gml:pos>"
				+ "<gml:pos>7.0928406 50.726496</gml:pos>"
				+ "<gml:pos>7.0932603 50.7264373</gml:pos>"
				+ "<gml:pos>7.0936289 50.7263983</gml:pos>"
				+ "<gml:pos>7.0938771 50.7262957</gml:pos>"
				+ "<gml:pos>7.0941002 50.7263717</gml:pos>"
				+ "<gml:pos>7.0943277 50.726494</gml:pos>"
				+ "<gml:pos>7.0943711 50.7265261</gml:pos>"
				+ "<gml:pos>7.0954017 50.727344</gml:pos>"
				+ "<gml:pos>7.0961441 50.7279214</gml:pos>"
				+ "<gml:pos>7.096643 50.7275477</gml:pos>"
				+ "<gml:pos>7.0977916 50.7267005</gml:pos>"
				+ "<gml:pos>7.099853 50.7276044</gml:pos>"
				+ "<gml:pos>7.0991354 50.7283003</gml:pos>"
				+ "<gml:pos>7.10147 50.7295444</gml:pos>"
				+ "<gml:pos>7.1015803 50.7294534</gml:pos>"
				+ "<gml:pos>7.1026116 50.72861</gml:pos>"
				+ "<gml:pos>7.1026132 50.7286108</gml:pos>"
				+ "<gml:pos>7.1033282 50.7289039</gml:pos>"
				+ "<gml:pos>7.1035322 50.7289761</gml:pos>"
				+ "<gml:pos>7.1037983 50.7290793</gml:pos>"
				+ "<gml:pos>7.1038766 50.7291254</gml:pos>"
				+ "<gml:pos>7.1030118 50.7302563</gml:pos>"
				+ "<gml:pos>7.1029554 50.7303335</gml:pos>"
				+ "<gml:pos>7.1025106 50.730813</gml:pos>"
				+ "<gml:pos>7.1021372 50.7312146</gml:pos>"
				+ "<gml:pos>7.1015506 50.7314696</gml:pos>"
				+ "<gml:pos>7.1011456 50.7316453</gml:pos>"
				+ "<gml:pos>7.1008962 50.7317438</gml:pos>"
				+ "<gml:pos>7.1006842 50.7318215</gml:pos>"
				+ "<gml:pos>7.1004414 50.7319002</gml:pos>"
				+ "<gml:pos>7.099446 50.7321509</gml:pos>"
				+ "<gml:pos>7.0988993 50.7322913</gml:pos>"
				+ "<gml:pos>7.0986258 50.7323634</gml:pos>"
				+ "<gml:pos>7.0986258 50.7323634</gml:pos>"
				+ "</gml:LineString> "
				+ "</xls:RouteGeometry>"
				+ "<xls:RouteInstructionsList xls:lang=\"es\">"
				+ "<xls:RouteInstruction duration=\"PT44S\" description=\"AcciÛn n∫ 1\">"
				+ "<xls:Instruction>Comience en: (East) en Nuﬂallee</xls:Instruction>"
				+ "<xls:distance value=\"214\" uom=\"YD\"/>"
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0892567 50.7265543</gml:pos> "
				+ "<gml:pos>7.089248823722613 50.72653868037818</gml:pos>"
				+ "<gml:pos>7.089294 50.7265159</gml:pos>"
				+ "<gml:pos>7.090037 50.7261616</gml:pos>"
				+ "<gml:pos>7.0913708 50.7254372</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT16S\" description=\"AcciÛn n∫ 2\">"
				+ "<xls:Instruction>Conduzca hacia la izquierda en Meckenheimer Allee</xls:Instruction>"
				+ "<xls:distance value=\"147\" uom=\"YD\"/>"
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0913708 50.7254372</gml:pos>"
				+ "<gml:pos>7.0917231 50.7257491</gml:pos>"
				+ "<gml:pos>7.0923754 50.7263577</gml:pos>"
				+ "<gml:pos>7.0924374 50.7264387</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT17S\" description=\"AcciÛn n∫ 3\">"
				+ "<xls:Instruction>Conduzca hacia la derecha en Poppelsdorfer Allee</xls:Instruction>"
				+ "<xls:distance value=\"118\" uom=\"YD\"/>"
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0924374 50.7264387</gml:pos>"
				+ "<gml:pos>7.0924494 50.7264436</gml:pos>"
				+ "<gml:pos>7.0925224 50.7264738</gml:pos>"
				+ "<gml:pos>7.0925963 50.7264979</gml:pos>"
				+ "<gml:pos>7.0928406 50.726496</gml:pos>"
				+ "<gml:pos>7.0932603 50.7264373</gml:pos>"
				+ "<gml:pos>7.0936289 50.7263983</gml:pos>"
				+ "<gml:pos>7.0938771 50.7262957</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT41S\" description=\"AcciÛn n∫ 4\">"
				+ "<xls:Instruction>Conduzca hacia la izquierda en Poppelsdorfer Allee</xls:Instruction>"
				+ "<xls:distance value=\"266\" uom=\"YD\"/> "
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0938771 50.7262957</gml:pos>"
				+ "<gml:pos>7.0941002 50.7263717</gml:pos>"
				+ "<gml:pos>7.0943277 50.726494</gml:pos>"
				+ "<gml:pos>7.0943711 50.7265261</gml:pos>"
				+ "<gml:pos>7.0954017 50.727344</gml:pos>"
				+ "<gml:pos>7.0961441 50.7279214</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT32S\" description=\"AcciÛn n∫ 5\">"
				+ "<xls:Instruction>Conduzca hacia la derecha en Argelanderstraﬂe</xls:Instruction>"
				+ "<xls:distance value=\"196\" uom=\"YD\"/> "
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0961441 50.7279214</gml:pos>"
				+ "<gml:pos>7.096643 50.7275477</gml:pos>"
				+ "<gml:pos>7.0977916 50.7267005</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT31S\" description=\"AcciÛn n∫ 6\">"
				+ "<xls:Instruction>Conduzca hacia la izquierda en Kˆnigstraﬂe</xls:Instruction>"
				+ "<xls:distance value=\"193\" uom=\"YD\"/>"
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0977916 50.7267005</gml:pos>"
				+ "<gml:pos>7.099853 50.7276044</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT12S\" description=\"AcciÛn n∫ 7\">"
				+ "<xls:Instruction>Conduzca hacia la izquierda en Bonner Talweg</xls:Instruction>"
				+ "<xls:distance value=\"101\" uom=\"YD\"/>"
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.099853 50.7276044</gml:pos>"
				+ "<gml:pos>7.0991354 50.7283003</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "<xls:RouteInstruction duration=\"PT19S\" description=\"AcciÛn n∫ 8\">"
				+ "<xls:Instruction>Conduzca hacia la derecha en Heinrich-von-Kleist-Straﬂe</xls:Instruction>"
				+ "<xls:distance value=\"235\" uom=\"YD\"/>"
				+ "<xls:RouteInstructionGeometry>"
				+ "<gml:LineString srsName=\"EPSG:4326\">"
				+ "<gml:pos>7.0991354 50.7283003</gml:pos>"
				+ "<gml:pos>7.10147 50.7295444</gml:pos>"
				+ "</gml:LineString>"
				+ "</xls:RouteInstructionGeometry>"
				+ "</xls:RouteInstruction>"
				+ "</xls:RouteInstructionsList>"
				+ "</xls:DetermineRouteResponse>"
				+ "</xls:Response>"
				+ "</xls:XLS>").getBytes();
	}

	public void testParse() {

		OpenLSRouteParser orp = new OpenLSRouteParser();
		ORSRoute route = orp.parse(data);

		assertEquals(route.getTotalDistance(), 2324.7);
		assertEquals(route.getBBox().getMinX(), 7.0892488);
		assertEquals(route.getBBox().getMinY(), 50.7254372);
		assertEquals(route.getBBox().getMaxX(), 7.1038766);
		assertEquals(route.getBBox().getMaxY(), 50.7323634);
		assertEquals(route.getInstructions().size(), 8);
		ORSInstruction rins = (ORSInstruction) route.getInstructions()
				.elementAt(4);
		assertTrue(rins.getDescription().contains(
				"Conduzca hacia la derecha en"));
		assertEquals(rins.getXCoords()[0], 7.0961441);
		assertEquals(rins.getXCoords()[1], 7.096643);
		assertEquals(rins.getXCoords()[2], 7.0977916);
	}
}

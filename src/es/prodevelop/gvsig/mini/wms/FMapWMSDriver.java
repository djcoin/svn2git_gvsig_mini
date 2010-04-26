/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package es.prodevelop.gvsig.mini.wms;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import org.gvsig.remoteclient.exceptions.ServerErrorException;
import org.gvsig.remoteclient.wms.ICancellable;
import org.gvsig.remoteclient.wms.WMSClient;
import org.gvsig.remoteclient.wms.WMSLayer;
import org.gvsig.remoteclient.wms.WMSStatus;
import org.gvsig.remoteclient.wms.WMSStyle;
import org.gvsig.remoteclient.wms.util.Rectangle2D;


/**
 * Driver WMS.
 *
 * @author Jaume Dominguez Faus
 */
public class FMapWMSDriver {
	private WMSClient client;
    private WMSLayerNode fmapRootLayer;
    private TreeMap layers = new TreeMap();
    private URL url;

    private FMapWMSDriver() {}

    protected FMapWMSDriver(URL url) throws ConnectException, IOException {
    	this.setUrl(url);
    	client = new WMSClient(url.toString());
    	
    }

    public String[] getLayerNames(){
    	return client.getLayerNames();
    }
    public String[] getLayerTitles(){
    	return client.getLayerTitles();
    }
    
	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.drivers.WMSDriver#getMap(org.gvsig.remoteClient.wms.WMSStatus)
	 */
    public File getMap(WMSStatus status, ICancellable cancel) throws WMSException {
        try {
			return client.getMap(status, cancel);
        } catch (org.gvsig.remoteclient.exceptions.WMSException e) {
            throw new WMSException(e.getMessage());
        } catch (ServerErrorException e) {
            throw new WMSException("WMS Unexpected server error."+e.getMessage());
        }
    }

    /**
     * Gets the legend graphic of one layer
     */
    public File getLegendGraphic(WMSStatus status, String layerName, ICancellable cancel) throws WMSException {
        try {
        	File f = client.getLegendGraphic(status, layerName, cancel);
			return f;
        } catch (org.gvsig.remoteclient.exceptions.WMSException e) {
            throw new WMSException(e.getMessage());
        } catch (ServerErrorException e) {
            throw new WMSException("WMS Unexpected server error."+e.getMessage());
        }
    }

	/**
	 * Devuelve WMSClient a partir de su URL.
	 *
	 * @param url URL.
	 *
	 * @return WMSClient.
	 * @throws IOException
	 * @throws ConnectException
	 *
	 * @throws UnsupportedVersionException
	 * @throws IOException
	 */


    /**
     * Establishes the connection.
     * @param override, if true the previous downloaded data will be overridden
     * @return <b>true</b> if the connection was successful, or <b>false</b> if it was no possible
     * to establish the connection for any reason such as version negotiation.
     */
    public boolean connect(boolean override, ICancellable cancel) {
    	if (override) {
    		fmapRootLayer = null;
    		layers.clear();
    	}
		return client.connect(override, cancel);
    }

    public boolean connect(WMSCancellable cancel) {
    	return client.connect(false, cancel);
    }

    /**
     * @return the version of this client.
     */
    public String getVersion() {
    	return client.getVersion();
    }

    /**
     * @return The title of the service offered by the WMS server.
     */
    public String getServiceTitle() {
		return client.getServiceInformation().title;
    }

//    /**
//     * Returns a Hash table containing the values for each online resource.
//     * Using as key a String with name of the WMS request and the value returned
//     * by the hash is another string containing the corresponding Url
//     * @return HashTable
//     */
//    public Hashtable getOnlineResources() {
//    	return client.getServiceInformation().getSupportedOperationsByName();
//    }
    
    /**
     * @return <b>Vector</b> containing strings with the formats supported by this server.
     */
    public Vector getFormats() {

    	return client.getFormats();
    }

    /**
     * @return <b>Boolean</b> returns if the WMS is queryable (suppports GetFeatureInfo)
     */
    public boolean isQueryable() {
        return client.isQueryable();
    }
    /**
     * @return <b>Boolean</b> returns if the WMS supports getLegendGraphic operation (suppports GetFeatureInfo)
     */
    public boolean hasLegendGraphic() {
        return client.hasLegendGraphic();
    }
    /**
     * @return A tree containing the info of all layers available on this server.
     */
    public WMSLayerNode getLayersTree() {
        if (fmapRootLayer == null){
            WMSLayer clientRoot;
            if (client.getLayersRoot() == null) {
            	client.connect(false, null);
            }
            clientRoot = client.getLayersRoot();


            fmapRootLayer = parseTree(clientRoot, null);
        }
        return fmapRootLayer;
    }

    /**
     * Parses the client's layer node and translates it to a fmap's layer node
     * @param WMSLayer
     * @return WMSLayerNode
     */
    private WMSLayerNode parseTree(WMSLayer node, WMSLayerNode parentNode) {

        WMSLayerNode myNode = new WMSLayerNode();

        // Name
        myNode.setName(node.getName());

        // Title
        myNode.setTitle(node.getTitle());

        // Transparency
        myNode.setTransparency(node.hasTransparency());

        // SRS
        myNode.setSrs(node.getAllSrs());


        // Queryable

        myNode.setQueryable(node.isQueryable() && client.getServiceInformation().isQueryable());

        // Parent layer
        myNode.setParent(parentNode);

        // Abstract
        myNode.setAbstract(node.getAbstract());

        // Fixed Size
        myNode.setFixedSize(node.getfixedWidth(), node.getfixedHeight());

        // LatLonBox
        if (node.getLatLonBox()!=null)
            myNode.setLatLonBox(node.getLatLonBox().toString());

        // Keywords
        ArrayList keywords = node.getKeywords();
        for (int i = 0; i < keywords.size(); i++) {
        	myNode.addKeyword((String) keywords.get(i));
		}

        // Styles
        ArrayList styles = node.getStyles();
        for (int i = 0; i < styles.size(); i++) {
            WMSStyle style = (WMSStyle) styles.get(i);
            myNode.addStyle(style);
        }        

        // Children
        int children = node.getChildren().size();
        myNode.setChildren(new ArrayList());
        for (int i = 0; i < children; i++) {
            myNode.getChildren().add(parseTree((WMSLayer)node.getChildren().get(i), myNode));
        }

        if (myNode.getName()!=null)
            layers.put(myNode.getName(), myNode);

        return myNode;
    }

    /**
     * @return
     */
    public String getAbstract() {

    	return client.getServiceInformation().abstr;

    }

    /**
     * @param layerName
     * @param srs
     * @return
     */
    public Rectangle2D getLayersExtent(String[] layerName, String srs) {
    	return client.getLayersExtent(layerName, srs);
    }

    /**
     * @param string
     * @return
     */
    public WMSLayerNode getLayer(String layerName) {
        if (getLayers().get(layerName) != null)
        {
            return (WMSLayerNode)layers.get(layerName);
        }
        return null;
    }

    /**
     * @return
     */
    private TreeMap getLayers() {
        if (fmapRootLayer == null){
            fmapRootLayer = getLayersTree();
        }
        return layers;
    }

    /**
     * @param wmsStatus
     * @param i
     * @param j
     * @param max_value
     * @return
     * @throws WMSException
     */
    public String getFeatureInfo(WMSStatus _wmsStatus, int i, int j, int max_value, ICancellable cancellable) throws WMSException {
        try {
            return client.getFeatureInfo(_wmsStatus, i, j, max_value, cancellable);
        } catch (org.gvsig.remoteclient.exceptions.WMSException e) {
            throw new WMSException();
		}
    }

    public WMSClient getClient(){
    	return client;

    }
    
    public String getLayerName(String title) {
    	return client.getLayerName(title);
    }

	public void setUrl(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}
}

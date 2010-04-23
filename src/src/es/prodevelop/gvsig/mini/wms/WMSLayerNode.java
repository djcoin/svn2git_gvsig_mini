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
* $Id: WMSLayerNode.java 29645 2009-06-29 17:03:35Z jpiera $
* $Log$
* Revision 1.11  2007-01-08 07:57:34  jaume
* *** empty log message ***
*
* Revision 1.10  2006/05/25 15:46:45  jaume
* *** empty log message ***
*
* Revision 1.8  2006/05/25 10:35:09  jaume
* *** empty log message ***
*
* Revision 1.7  2006/05/25 10:28:12  jaume
* *** empty log message ***
*
* Revision 1.6  2006/03/21 16:02:06  jaume
* *** empty log message ***
*
* Revision 1.5  2006/02/28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.3.2.9  2006/02/20 15:23:08  jaume
* Se muestran algunas de las capas de aes
*
* Revision 1.3.2.8  2006/02/17 12:57:34  jaume
* oculta/eXconde los nombres de las capas y ademï¿½s corrige el error de selecciï¿½n de varios styles si hay alguna capa seleccionada repetida
*
* Revision 1.3.2.7  2006/02/16 10:36:41  jaume
* *** empty log message ***
*
* Revision 1.3.2.6  2006/02/15 11:50:08  jaume
* *** empty log message ***
*
* Revision 1.3.2.5  2006/02/10 13:22:35  jaume
* now analyzes dimensions on demand
*
* Revision 1.3.2.4  2006/02/02 12:12:54  jaume
* se muestra el nombre de la capa (ademï¿½s del tï¿½tulo) en los diï¿½logos
*
* Revision 1.3.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.4  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.3.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.3  2006/01/24 18:01:17  jaume
* *** empty log message ***
*
* Revision 1.1.2.11  2006/01/23 12:54:45  jaume
* *** empty log message ***
*
* Revision 1.1.2.10  2006/01/20 15:22:46  jaume
* *** empty log message ***
*
* Revision 1.1.2.9  2006/01/17 12:55:40  jaume
* *** empty log message ***
*
* Revision 1.1.2.8  2006/01/05 23:15:53  jaume
* *** empty log message ***
*
* Revision 1.1.2.7  2006/01/04 18:09:02  jaume
* Time dimension
*
* Revision 1.1.2.6  2006/01/03 18:08:40  jaume
* *** empty log message ***
*
* Revision 1.1.2.5  2006/01/02 18:08:01  jaume
* Tree de estilos
*
* Revision 1.1.2.4  2005/12/30 08:56:19  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2005/12/29 08:26:54  jaume
* some gui issues where fixed
*
* Revision 1.1.2.2  2005/12/26 16:51:40  jaume
* Handles STYLES, layer saving does nothing ï¿½?
*
* Revision 1.1.2.1  2005/12/21 15:59:04  jaume
* Refatoring maintenance
*
*
*/
/**
 *
 */
package es.prodevelop.gvsig.mini.wms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

import org.gvsig.remoteclient.wms.WMSStyle;
import org.gvsig.remoteclient.wms.util.Dimension;



/**
 * Class defining the node of the layer tree of a common WMS service.
 * @author jaume
 *
 */
public class WMSLayerNode {
    private String _name;
    private String _title;
    private Vector srs;
    private boolean queryable;
    private boolean transparency;
    private String lAbstract;
    private String latLonBox;
    private int selectedStyleIndex = 0;
    private ArrayList styles = new ArrayList();
    private ArrayList dimensions;
    private ArrayList keywords;

    private ArrayList children = new ArrayList();
    private WMSLayerNode _parent;
	private Dimension fixedSize;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }

    /**
     * @return
     */
    public ArrayList getChildren() {
        return children;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return Returns the namedStyles.
     */
    public ArrayList getStyles() {
        return styles;
    }

    public ArrayList getKeywords(){
    	return keywords;
    }
    /**
     * @return Returns the queryable.
     */
    public boolean isQueryable() {
        return queryable;
    }

    /**
     * @param queryable The queryable to set.
     */
    public void setQueryable(boolean queryable) {
        this.queryable = queryable;
    }
    /**
     * @return Returns the srs.
     */
    public Vector getAllSrs() {
        if ((srs.size() == 0) && _parent!=null)
            return _parent.getAllSrs();
        return srs;
    }

    /**
     * @param srs The srs to set.
     */
    public void setSrs(Vector srs) {
        this.srs = srs;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this._title = title.trim();
    }

    /**
     * @return Returns the transparency.
     */
    public boolean isTransparent() {
        return transparency;
    }

    /**
     * @param transparency The transparency to set.
     */
    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    /**
     * Sets the list of sons of this layer.
     * @param children
     */
    public void setChildren(ArrayList children) {
        this.children = children;
    }

    /**
     * returns the layer whose this is son of.
     * @return
     */
    public WMSLayerNode getParent(){
        return _parent;
    }
    /**
     * @param parentNode
     */
    public void setParent(WMSLayerNode parentNode) {
        this._parent = parentNode;
    }

    public ArrayList getDimensions(){
        return dimensions;
    }

    /**
     * Gets the layer abstract.
     *
     * @return Returns the abstract.
     */
    public String getAbstract() {
        return lAbstract;
    }

    /**
     * Sets the layer abstract.
     *
     * @param abstract The abstract to set.
     */
    public void setAbstract(String _abstract) {
        lAbstract = _abstract;
    }

//    /**
//     * @param name
//     * @param units
//     * @param unitSymbol
//     * @param dimensionExpression
//     */
//    public void addDimension(String name, String units, String unitSymbol, String dimExpression) {
//    	if (dimensions == null)
//    		dimensions = new ArrayList();
//    	if (name.equalsIgnoreCase("time")) {
//    		try {
//    			dimensions.add(new TimeDimension(units, unitSymbol,
//    					dimExpression));
//    		} catch (IllegalArgumentException e) {
//    			// The TIME class does not yet support this kind of time so it
//    			// will be treated as a DefaultDimension
//    			dimensions.add(new DefaultDimension(name.toUpperCase(),
//    					units, unitSymbol, dimExpression));
//    		}
//    	} else if (name.equalsIgnoreCase("sequence")) {
//    		// TODO Not yet implemented
//    		return;
//    	} else {
//    		dimensions.add(new DefaultDimension(name.toUpperCase(), units,
//    				unitSymbol, dimExpression));
//    	}
//
//    }

    /**
     * Sets the Latitude-Longitude box text to be shown in an user interface layer descriptor.
     * @param latLonBox
     */
    public void setLatLonBox(String _latLonBox) {
        latLonBox = _latLonBox;
    }

    /**
     * Returns the Latitude-Longitude box text to be shown in an user interface layer descriptor.
     * @return
     */
    public String getLatLonBox() {
        return latLonBox;
    }

    /**
     * When a server cannot renderize images but just server them in constant size and
     * BBox, the layer must have this value set in order to correctly work.
     *
     * @param fixedWidth - the constant value for the image width
     * @param fixedHeight - the constant value for the image height
     */
    public void setFixedSize(int fixedWidth, int fixedHeight) {
		fixedSize = new Dimension(fixedWidth, fixedHeight);
	}

    /**
     * Returns the size of this layer (which is constant-sized)
     * @return
     */
	public Dimension getFixedSize() {
		return fixedSize;
	}

	/**
	 * Tells whether the layer is constant-sized or not.
	 * @return boolean
	 */
	public boolean isSizeFixed() {
		return fixedSize            != null &&
		       fixedSize.width  > 0    &&
		       fixedSize.height > 0;
	}

    /**
     *
     * @param _name
     * @param _title
     * @param _abstract
     */
    public void addStyle(WMSStyle style) {
    	if (style.getName().equalsIgnoreCase("default"))
    		selectedStyleIndex = styles.size();
        if (styles==null)
            styles = new ArrayList();
        styles.add(new FMapWMSStyle(style, this));

    }

    /**
     * Returns the style marked as selected or null if none.
     * @return FMapWMSStyle
     */
    public FMapWMSStyle getSelectedStyle() {
    	if (styles == null || selectedStyleIndex > styles.size()-1 || selectedStyleIndex == -1)
    		return null;
    	return (FMapWMSStyle) styles.get(selectedStyleIndex);
    }

    /**
     * Marks the style of this layer given by the index as selected
     * @param inex of the style
     */
    public void setSelectedStyleByIndex(int index) {
		selectedStyleIndex = index;
	}

    /**
     * Marks the style of this layer given by its name as selected. If
     * this layer has no style with this name, then the layer is marked
     * as none selected.
     * @param style name
     */
	public void setSelectedStyleByName(String styName) {
		if (styName == null || styName.equals(""))
			setSelectedStyleByIndex(-1);
		for (int i = 0; i < styles.size(); i++) {
			FMapWMSStyle sty = (FMapWMSStyle) styles.get(i);
			if (sty.name.equals(styName)) {
				setSelectedStyleByIndex(i);
				return;
			}

		}
		setSelectedStyleByIndex(-1);
	}

	public void addKeyword(String keyword) {
		if (keywords == null)
			keywords = new ArrayList();
		keywords.add(keyword);
	}

    public String toString(){
    	String str;
    	if (getName()==null)
    		str = getTitle();
    	else
    		str = "["+getName()+"] "+getTitle();
        return str;
    }

    /**
     * Creates a new instance of WMSLayerNode containing a copy of this,
     * but with no children and parent set.
     */
    public Object clone(){
        WMSLayerNode clone       = new WMSLayerNode();
        clone._name              = this._name;
        clone.queryable          = this.queryable;
        clone.srs                = this.srs;
        clone._title             = this._title;
        clone.transparency       = this.transparency;
        clone.styles             = new ArrayList();
        clone.lAbstract          = this.lAbstract;
        clone.latLonBox          = this.latLonBox;
        clone.selectedStyleIndex = this.selectedStyleIndex;

        if (keywords != null) {
        	clone.keywords = new ArrayList(keywords.size());
        	for (int i = 0; i < keywords.size(); i++) {
				clone.keywords.add((String) keywords.get(i));
			}
        }
        if (styles!=null)
        	for (int i=0; i<styles.size(); i++){
        		FMapWMSStyle sty = (FMapWMSStyle) ((FMapWMSStyle) this.styles.get(i)).clone();
        		sty.parent = this;
        		clone.styles.add(sty);
        	}   

        return clone;
    }


	/**
     * Just a C-struct-like class.
     * @author jaume
     *
     */
    public class FMapWMSStyle {
    	/*
    	 * Please! ensure that the fields are double, int, or Object
    	 * or otherwise add the corresponding entry in the clone() method.
    	 */
        public String name;
        public String title;
        public String styleAbstract;
        public String format;
        public String type;
        public String href;
        public WMSLayerNode parent;
        public int legendHeight;
        public int legendWidth;
        /*
    	 * Please! ensure that the fields are double, int, or Object
    	 * or otherwise add the corresponding entry in the clone() method.
    	 */

        /**
         * Creates a new instance of FMapWMSStyle
         * @param name
         * @param title
         * @param styleAbstract
         * @param parent
         */
        public FMapWMSStyle(WMSStyle style, WMSLayerNode parent){

            this.name = style.getName();
            this.title = style.getTitle();
            this.styleAbstract = style.getAbstract();
            this.legendWidth = style.getLegendURLWidth();
            this.legendHeight = style.getLegendURLHeight();
            this.format = style.getLegendURLFormat();
            this.href = style.getLegendURLOnlineResourceHRef();
            this.type = style.getLegendURLOnlineResourceType();
            this.parent = parent;
        }

        public FMapWMSStyle() {
			// TODO Auto-generated constructor stub
		}

		public String toString(){
            return title;
        }

        public Object clone() {
            FMapWMSStyle clone = new FMapWMSStyle();
            Field[] fields = FMapWMSStyle.class.getFields();
            for (int i = 0; i < fields.length; i++) {
            	try {
            		Class clazz = getClass();
            		String fieldName = fields[i].getName();
            		// int entry
    				if (fields[i].getType().equals(Integer.class)) {
    					clazz.getField(fieldName).
    					setInt(clone, clazz.getField(fieldName)
    								.getInt(this));
    				// double entry
    				} else if (fields[i].getType().equals(Double.class)) {
    					clazz.getField(fieldName).
						setDouble(clone, clazz.getField(fieldName)
								.getDouble(this));
    				// any object entry
    				} else {
    					clazz.getField(fieldName).
						set(clone, clazz.getField(fieldName)
								.get(this));
    				}
            	} catch (NullPointerException e) {
            		e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
            return clone;
        }
    }

}

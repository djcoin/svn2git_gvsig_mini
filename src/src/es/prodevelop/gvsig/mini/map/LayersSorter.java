package es.prodevelop.gvsig.mini.map;

import java.util.Vector;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

/**
 * Class to sort a Vector of Strings. This class sorts a Vector
 * containing the name of the layers in alphabetical order by MapRenderer type
 * @author aromeu
 *
 */
public class LayersSorter {
	
	private final static Logger log = LoggerFactory.getLogger(LayersSorter.class);
	
	public LayersSorter() {
		
	}
	
	public Vector<String> sort(Vector<String> layers) {
		try {
			final int size = layers.size();
			
			Vector<String> temp = new Vector<String>(size);
			
			String layerName = null;
			String num = null;
			for(int i = 0; i<size; i++) {
				try {
					layerName = layers.elementAt(i);
					final int index = layerName.indexOf("|");
					if (index != -1) {
						num = layerName.substring(0, index);
						int position = Integer.valueOf(num).intValue();
						temp.setElementAt(layerName, position);
					} else {
						temp.add(layerName);
					}
				} catch (Exception e) {
					if (layerName != null)
						temp.add(layerName);
				}
			}
			return temp;
		} catch (Exception e) {
			log.error(e);
			return null;
		}		
	}

}

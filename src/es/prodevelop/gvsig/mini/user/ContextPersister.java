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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Miguel Montesinos mmontesinos@prodevelop.es
 *
 *

 */
/**
 * 
 */
package es.prodevelop.gvsig.mini.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Environment;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.util.Utils;

	

/**
 * Used for storing user behaviour and context in order to adapt the application
 * to the user
 * @author mmontesinos
 *
 */
public class ContextPersister{

	private final static Logger log = Logger.getLogger(ContextPersister.class.getName());
	private String fileName;
	private String dirPath;

	/**
	 * Class constructor with file name.<p>
	 * Generates the properties file route and name
	 * @param _fileName name of the file including extension to be used for
	 *                  storing and retrieving context attributes 
	 * @throws IOException if _fileName is null or empty
	 */
	public ContextPersister(String _fileName) throws IOException{
		try {
			this.setFileName(_fileName);
		} catch (IOException e) {
			log.log(Level.SEVERE,e.getMessage());
		}
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *  Default constructor without assigning a known file name for persistance
	 */
	public ContextPersister() {
		// Nothing to do
	}

	/**
	 * Sets the name of the file including extension to be used for storing and
	 * retrieving context attributes  
	 * @param _fileName name of the file including extension
	 * @throws IOException if the file name is empty or null
	 */
	public void setFileName(String _fileName) throws IOException{
		try {
			if ((_fileName != null) && (_fileName != "")){
				fileName = _fileName;
//				log.setLevel(Utils.LOG_LEVEL);
//				log.setClientID(this.toString());
				String SDDIR = Environment.getExternalStorageDirectory().getPath();
				String appDir = Utils.APP_DIR;
				String configDir = Utils.CONFIG_DIR;
				dirPath = SDDIR + File.separator + appDir + File.separator + configDir + File.separator;
			} else{
				log.log(Level.SEVERE,"ContextPersister(): fileName cannot be null or empty");
				throw new IOException("ContextPersister(): fileName cannot be null");
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"ContextPersister.setFileName(): " + e.getMessage());
		}	
	}
	

/**
 * Stores the properties of a user context to the file indicated in the class
 * creation.
 * @param properties a HashMap of &lt;String, Object&gt; with the values to be
 *                   persisted
 * @return true if the file has been saved<p>
 *         false if it hasn't been possible to save
 * @throws IOException if 
 */
	public boolean saveContext(HashMap<String, Object> properties){
 
	String prop = "";
	BufferedWriter out = null;
	FileWriter logwriter = null;
	boolean res = false;
	
	try {
		//Open or create the file where to store the context
		File f = new File(dirPath + fileName);
		if (!f.exists()) {
			File dirFile = new File(dirPath);
			dirFile.mkdirs();
			f.createNewFile();
		} else { //If exists, let's delete it and create again
			f.delete();
			f.createNewFile();
		}

		logwriter = new FileWriter(f, false);
		out = new BufferedWriter(logwriter);
		
		//Let's persist all the properties of the hashmap
		Iterator iter = properties.keySet().iterator();
		while (iter.hasNext()){
			prop = (String) iter.next();
			out.write(prop);
			out.write("=");
			out.write(properties.get(prop).toString());
			out.write("\n");
		}
		res = true;		
	} catch (Exception e) {
		log.log(Level.SEVERE,"ContextPersister.saveContext(): " + e.getMessage());
		res = false;
	} finally {
		Utils.closeStream(out);
		Utils.closeStream(logwriter);
		return res;
	}

	}
	
	/**
	 * Loads the properties of a user context from the file indicated in the
	 * class creation or through the <code>setFileName</code> method.
	 * @return a HashMap of &lt;String, Object&gt; with the values to be loaded from
	 *         the persisted file
	 */
	public HashMap<String, Object> loadContext() {		
		FileReader configReader = null;
		BufferedReader reader = null;
		HashMap<String, Object> properties = null;
		try {
			//Open the file for reading the properties
			File f = new File(dirPath + fileName);
			if (f != null && f.exists()) {
				configReader = new FileReader(f);
				reader = new BufferedReader(configReader);	
								
				String line = null;
				String[] part;
				//Assign the pairs key-values to a HashMap 
				properties = new HashMap<String, Object>();
				while ((line = reader.readLine()) != null) {
					part = line.split("=");				
					properties.put(part[0], part[1]);				
				}
			} //else would return null as it'll happen in finally{, as properties remains null
			  // if the "if" condition is not accomplished
		} catch (Exception e) {
			log.log(Level.SEVERE,"ContextPersister.loadContext(): " + e.getMessage());
		} finally {
			Utils.closeStream(configReader);
			Utils.closeStream(reader);
			return properties;
		}
	}
}


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
package es.prodevelop.gvsig.mini.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;





/**
 * Singleton for storing all user contexts and saving and retrieving its
 * properties<p>
 * User contexts must comply with {@link UserContextable} interface. 
 * @author mmontesinos
 *
 */
public class UserContextManager {

	private static UserContextManager instance = null;
	 
    private final static Logger log = Logger.getLogger(ContextPersister.class.getName());
	private ArrayList<UserContextable> contexts;
	private ContextPersister persister;
	
	
	//Class constructor
	private UserContextManager(){	
//		log.setLevel(Utils.LOG_LEVEL);
//		log.setClientID(this.toString());
		contexts = new ArrayList<UserContextable>();
		persister = new ContextPersister();
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Let's make the singleton thread-safe just in case it's being used in
	//a multi-thread case
	private synchronized static void createInstance() {
        if (instance == null) { 
        	instance = new UserContextManager();
        }
    }
	
	/**
	 * Access to the singleton UserContextManager.
	 * @return the unique UserContextManager available. If not existing, a
	 *         new <code>UserContextManager</code> is created and returned
	 */
	public static UserContextManager getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

	/**
	 * Register a context (<code>UserContextable</code> interface compliant)
	 * @param context the context to register in the manager
	 * @see UserContextable
	 */
	public void Register(UserContextable context){
		try {
			if (contexts.add(context) == false) {
				log.log(Level.SEVERE,"UserContextManager.Register(): cannot register UserContextable object");
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,e.getMessage());
		}
	}
	
	/**
	 * Stores all context (<code>UserContextable</code> interface compliant)
	 * registered objects to disk
	 * @see UserContextable
	 */
	public void saveContexts(){
		String file;
		HashMap<String, Object> hash;
		try {
			// Go through all registered UserContextable objects and save the 
			// context for everyone
			for (int i = 0; i < contexts.size(); i++){
				file = contexts.get(i).getFileName();
				if ((file != "") && (file != null)) {
					// Get the hash table from the object with its attributes to save
					hash = contexts.get(i).saveContext();
					/*We make an inner try statement for catching a possible IOException,
					 *and managing it, allowing the rest of contexts registered in the manager
					 *to be saved although one fails
					 */
					try {
						// Set the file name where to save to
						persister.setFileName(file);
						// Do save the data
						if (persister.saveContext(hash) == false) {
							log.log(Level.SEVERE,"UserContextManager.saveContexts(): Context file " + file + " not saved");
						}
					} catch (IOException io) {
						log.log(Level.SEVERE,"UserContextManager.saveContexts(): IOException catched. Context file " + file + " not saved");
					}
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"UserContextManager.saveContexts(): " + e.getMessage());
		}
	}

	/**
	 * Loads all registered context (<code>UserContextable</code> interface compliant)
	 * data from disk to registered objects
	 * @see UserContextable
	 */
	public void loadContexts(){
		String file;
		HashMap<String, Object> hash;
		try {
			// Go through all registered UserContextable objects and load the 
			// context for everyone
			for (int i = 0; i < contexts.size(); i++){
				file = contexts.get(i).getFileName();
				if ((file != "") && (file != null)) {
					/*We make an inner try statement for catching a possible IOException,
					 *and managing it, allowing the rest of contexts registered in the manager
					 *to be loaded although one fails
					 */
					try {
						// Set the file name where to load from
						persister.setFileName(file);
						hash = persister.loadContext(); 
						// If hash were null, it's passed as null anyway to loadContext()
						if (contexts.get(i).loadContext(hash) == false) {
							// Problem while loading (maybe hash is null)
							log.log(Level.SEVERE,"UserContextManager.loadContexts(): Context could not be loaded for " + file);
						}
					} catch (IOException io) {
						log.log(Level.SEVERE,"UserContextManager.loadContexts(): IOException catched. Context file " + file + " not saved");
					}
						
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"UserContextManager.loadContexts(): " + e.getMessage());
		}
	}

}

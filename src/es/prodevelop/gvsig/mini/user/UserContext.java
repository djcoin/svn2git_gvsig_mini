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

import java.util.HashMap;

import es.prodevelop.gvsig.mini.R;

/**
 * Class for managing user context, like application usage along different sessions,
 * that is, it's aimed for keeping the application usage updated every time the app
 * is started, exited, ...
 * @author mmontesinos
 *
 */
public class UserContext implements UserContextable{

	//User context attributes
	private boolean usedCircleMenu = false;
	private boolean usedRoutes = false;
	private boolean usedWeather = false;
	private boolean usedTwitter = false;
	private boolean usedScaleBar = false;

	private int executionCounter = 0; 
	private int lastExecCircle = 0;
	private int lastExecRoutes = 0;
	private int lastExecWeather = 0;
	private int lastExecTwitter = 0;
	private int lastExecScaleBar = 0;
	
	//Class internal attributes
	private String fileName = "UserContext.txt";
	
	//Class temporal attributes (not persisted)
	private int lastExecutionCounter = 0; 
	
	/**
	* Class initializer. Does nothing
	*/
	public UserContext() {
	}
	
	/**
	 * Class initializer.
	 * @param _fileName A file name for persisting the object's attributes.
	 *        It must include the extension, but not the file path
	 *        Another initializer is available without parameters, using a 
	 *        default file name ("UserContext.txt")
	 * @see UserContextable
	 */
	public UserContext(String _fileName) {
		fileName = _fileName;
	}

	public String getFileName(){
		return fileName;
	}
	
	public HashMap<String, Object> saveContext(){
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("UsedCircleMenu", usedCircleMenu);
		hash.put("UsedRoutes", usedRoutes);
		hash.put("UsedWeather", usedWeather);
		hash.put("UsedTwitter", usedTwitter);
		hash.put("UsedScaleBar", usedScaleBar);
		hash.put("ExecCounter", executionCounter);
		hash.put("ExecCircle", lastExecCircle);
		hash.put("ExecRoutes", lastExecRoutes);
		hash.put("ExecTwitter", lastExecTwitter);
		hash.put("ExecWeather", lastExecWeather);
		hash.put("ExecScaleBar", lastExecScaleBar);
		return hash;
	}
	
	public boolean loadContext(HashMap<String, Object> hash){
		if (hash != null) {
			if (!hash.isEmpty()) { //Double if, because if null .isEmpty throws an exception
				this.usedCircleMenu = getHashBoolean(hash, "UsedCircleMenu");
				this.usedRoutes = getHashBoolean(hash, "UsedRoutes");
				this.usedWeather = getHashBoolean(hash, "UsedWeather");
				this.usedTwitter = getHashBoolean(hash, "UsedTwitter");
				this.usedScaleBar = getHashBoolean(hash, "UsedScaleBar");
				this.executionCounter = getHashInt(hash, "ExecCounter");
				lastExecutionCounter = executionCounter;
				this.lastExecRoutes = getHashInt(hash, "ExecRoutes");
				this.lastExecCircle = getHashInt(hash, "ExecCircle");
				this.lastExecRoutes = getHashInt(hash, "ExecRoutes");
				this.lastExecTwitter = getHashInt(hash, "ExecTwitter");
				this.lastExecWeather = getHashInt(hash, "ExecWeather");
				this.lastExecScaleBar = getHashInt(hash, "ExecScaleBar");
				return true;
			}
		} 
		this.usedCircleMenu = false;
		this.usedRoutes = false;
		this.usedWeather = false;
		this.usedTwitter = false;
		this.usedScaleBar = false;
		this.executionCounter = 0;
		this.lastExecRoutes = 0;
		this.lastExecCircle = 0;
		this.lastExecRoutes = 0;
		this.lastExecTwitter = 0;
		this.lastExecWeather = 0;
		this.lastExecScaleBar = 0;
		return false;
	}	
	
	/**
	 * Gets a string to be displayed as a hint to the user	
	 * The hints are based on the user past behaviour
	 * @return ResId of the string to be displayed
	 */
	public int getHintMessage() {
		if (!this.usedCircleMenu) {
			// The Circle Context Menu (Roulette) has never been displayed,
			// so let's give the user a hint about it
			return R.string.Map_23;
		} else if (!this.usedScaleBar) {
			// The Zoom Scale Bar has never been displayed,
			// so let's give the user a hint about it
			return R.string.Map_34;
		} else {
			return 0;
		}
			
	}
	
	/**
	 * Gets if the context circle menu has been used at least once in any
	 * application execution
	 * @return true if it ever has been executed
	 */
	public boolean isUsedCircleMenu() {
		return usedCircleMenu;
	}

	/**
	 * Gets if the route feature has been used at least once in any
	 * application execution
	 * @return true if it ever has been executed
	 */
	public boolean isUsedRoutes() {
		return usedRoutes;
	}
	
	/**
	 * Gets if the weather query feature has been used at least once in any
	 * application execution
	 * @return true if it ever has been queried
	 */
	public boolean isUsedWeather() {
		return usedWeather;
	}
	
	/**
	 * Gets if the tweetme feature has been used at least once in any
	 * application execution
	 * @return true if it ever has been executed
	 */
	public boolean isUsedTwitter() {
		return usedTwitter;
	}
	
	/**
	 * Gets if the zoom scale bar feature has been used (zoom has been changed
	 * manually to a zoom level directly) at least once in any application execution
	 * @return true if it ever has been executed
	 */
	public boolean isUsedScaleBar() {
		return usedScaleBar;
	}
	
	/**
	 * Sets the context circle menu to have been used at least once or not
	 * @param usedCircleMenu Set it to true if it ever has been used
	 */
	public void setUsedCircleMenu(boolean usedCircleMenu) {
		this.usedCircleMenu = usedCircleMenu;
	}
	
	/**
	 * Sets the context route feature to have been used at least once or not
	 * @param usedRoutes Set it to true if it ever has been used
	 */
	public void setUsedRoutes(boolean usedRoutes) {
		this.usedRoutes = usedRoutes;
	}
	
	/**
	 * Sets the context weather query feature to have been used at least once or not
	 * @param usedWeather Set it to true if it ever has been used
	 */
	public void setUsedWeather(boolean usedWeather) {
		this.usedWeather = usedWeather;
	}
	
	/**
	 * Sets the context tweetme feature to have been used at least once or not
	 * @param usedTwitter Set it to true if it ever has been used
	 */
	public void setUsedTwitter(boolean usedTwitter) {
		this.usedTwitter = usedTwitter;
	}
	
	/**
	 * Sets the zoom scale bar feature (zoom has been changed
	 * manually to a zoom level directly) to have been used at least once or not
	 * @param usedScaleBar Set it to true if it ever has been used
	 */
	public void setUsedScaleBar(boolean usedScaleBar) {
		this.usedScaleBar = usedScaleBar;
	}

	/**
	 * Accesses the number of times gvSIG Mini has been executed
	 * @return the number of times, once per application execution
	 */
	public int getExecutionCounter() {
		return executionCounter;
	}

	/**
	 * Increments the number of times gvSIG Mini has been executed.<p>
	 * It will be incremented only once for an application execution, even if
	 * called several times
	 */
	public void incExecutionCounter() {
		if (lastExecutionCounter == executionCounter) {
			executionCounter++;
		}
	}

	/**
	 * Accesses the last execution number the circular context menu (Roulete) has been 
	 * executed.<p>
	 * Execution number is measured by the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution
	 * @return the last execution number the circular context menu was executed
	 */
	public int getLastExecCircle() {
		return lastExecCircle;
	}

	/**
	 * Updates the last execution number the circular context menu (Roulete) has been 
	 * executed, with the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution<p>
	 * After calling this method, <code>lastExecCircle</code> is equal to <code>executionCounter</code>
	 */
	public void setLastExecCircle() {
		this.lastExecCircle = executionCounter;
	}

	/**
	 * Accesses the last execution number the route feature has been executed.<p>
	 * Execution number is measured by the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution
	 * @return the last execution number the route feature was executed
	 */
	public int getLastExecRoutes() {
		return lastExecRoutes;
	}

	/**
	 * Updates the last execution number the route feature has been executed,
	 * with the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution<p>
	 * After calling this method, <code>lastExecRoutes</code> is equal to <code>executionCounter</code>
	 */
	public void setLastExecRoutes() {
		this.lastExecRoutes = executionCounter;
	}

	/**
	 * Accesses the last execution number the weather query feature has been executed.<p>
	 * Execution number is measured by the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution
	 * @return the last execution number the weather query feature was executed
	 */
	public int getLastExecWeather() {
		return lastExecWeather;
	}

	/**
	 * Updates the last execution number the weather query feature has been executed,
	 * with the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution<p>
	 * After calling this method, <code>lastExecWeather</code> is equal to <code>executionCounter</code>
	 */
	public void setLastExecWeather() {
		this.lastExecWeather = executionCounter;
	}

	/**
	 * Accesses the last execution number the Tweetme feature has been executed.<p>
	 * Execution number is measured by the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution
	 * @return the last execution number the Tweetme feature was executed
	 */
	public int getLastExecTwitter() {
		return lastExecTwitter;
	}

	/**
	 * Updates the last execution number the Tweetme feature has been executed,
	 * with the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution<p>
	 * After calling this method, <code>lastExecTwitter</code> is equal to <code>executionCounter</code>
	 */
	public void setLastExecTwitter() {
		this.lastExecTwitter = executionCounter;
	}

	/**
	 * Accesses the last execution number the zoom scale bar feature (zoom has been changed
	 * manually to a zoom level directly) has been executed.<p>
	 * Execution number is measured by the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution
	 * @return the last execution number the zoom scale bar feature was executed
	 */
	public int getLastExecScaleBar() {
		return lastExecScaleBar;
	}

	/**
	 * Updates the last execution number the zoom scale bar feature (zoom has been changed
	 * manually to a zoom level directly) has been executed,
	 * with the internal attribute <code>executionCounter</code>
	 * which is incremented once per gvSIG Mini execution<p>
	 * After calling this method, <code>lastExecScaleBar</code> is equal to <code>executionCounter</code>
	 */
	public void setLastExecScaleBar() {
		this.lastExecScaleBar = executionCounter;
	}
	
	//Given a hash and a key extracts its int value in a safe way
	private int getHashInt(HashMap<String, Object> hash, String key){
		try {
			if (hash.get(key) != null) {
				return Integer.valueOf(hash.get(key).toString()).intValue();
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	//Given a hash and a key extracts its bolean value in a safe way
	private boolean getHashBoolean(HashMap<String, Object> hash, String key){
		try {
			if (hash.get(key) != null) {
				return Boolean.valueOf(hash.get(key).toString()).booleanValue();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;		
		}
	}
	
}

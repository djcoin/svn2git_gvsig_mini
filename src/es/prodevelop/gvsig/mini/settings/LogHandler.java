/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 *	 2010.
 *   author Alberto Romeu - aromeu@prodevelop.es
 *   
 */

package es.prodevelop.gvsig.mini.settings;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.os.Environment;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * Utility class to manage and configure the logging files, sending log reports,
 * clean logs, etc.
 * 
 * @author aromeu
 * 
 */
public class LogHandler {

	/**
	 * Log file size
	 */
	public static final int FILE_SIZE = 1024;

	/**
	 * The base log level
	 */
	public static Level LOG_LEVEL = Level.FINE;

	/**
	 * The log level for tilecache
	 */
	public static Level FS_LEVEL = Level.FINE;

	private static LogHandler instance;

	private final static Logger logger = Logger.getLogger(LogHandler.class
			.getName());

	private FileHandler handler;

	public static LogHandler getInstance() {
		if (instance == null) {
			instance = new LogHandler();
		}
		return instance;
	}

	/**
	 * Creates a rolling file handler with a maximum of 5 log files of
	 * FILE_SIZE. Log files are stored at Environment
	 * .getExternalStorageDirectory() + File.separator + Utils.APP_DIR +
	 * File.separator + Utils.LOG_DIR, and a SimpleFormatter is used
	 */
	public void configureLog() {
		try {
			//
			// Creating an instance of FileHandler with 5 logging files
			// sequences.
			//			
			if (Utils.isSDMounted()) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + Utils.APP_DIR + File.separator
						+ Utils.LOG_DIR + File.separator);
				f.mkdirs();

				handler = new FileHandler(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ Utils.APP_DIR
						+ File.separator
						+ Utils.LOG_DIR + File.separator + "gvsig.log",
						FILE_SIZE, 5, true);

				handler.setFormatter(new SimpleFormatter());
				handler.setLevel(LOG_LEVEL);
				logger.addHandler(handler);
				logger.setUseParentHandlers(false);
			}

		} catch (IOException e) {
			logger.warning("Failed to initialize logger handler.");
		}

		logger.info("Logging information message.");
		logger.warning("Logging warning message.");
	}

	/**
	 * The current file handler
	 * 
	 * @return
	 */
	public FileHandler getHandler() {
		return handler;
	}
	
	/**
	 * 
	 * @param log
	 */
	public void setLogLevel(Logger log) {
		
	}

}

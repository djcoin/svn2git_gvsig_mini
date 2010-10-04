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

package es.prodevelop.gvsig.mini.tasks;

import android.os.Handler;

/**
 * Handles messages of Functionalities
 * @author aromeu 
 * @author rblanco
 *
 */
public class TaskHandler extends Handler implements TaskStatusListener {
	
	private Task task;
	public static final int CANCELED = -1;
	public static final int FINISHED = CANCELED - 1;
	public static final int INITED = FINISHED - 1;
	public static final int ERROR = INITED -1;	
	public static final int NO_RESPONSE = ERROR - 1;	
	public static final int BAD_RESPONSE = NO_RESPONSE -1;

	@Override
	public void taskCanceled(Task task) {		
		this.task = task;
		this.sendEmptyMessage(CANCELED);		
	}

	@Override
	public void taskFinished(Task task) {
		this.task = task;		
		this.sendEmptyMessage(task.getMessage());		
	}

	@Override
	public void taskInited(Task task) {
		this.task = task;
		this.sendEmptyMessage(INITED);		
	}

	@Override
	public void taskProgress(Task task, int progress) {
		this.task = task;		
	}
}
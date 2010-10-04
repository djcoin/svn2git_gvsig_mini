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
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.tasks;

import java.util.Vector;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;

/**
 * It does the work of adding and removing observers and broadcasting it's
 * initialization and termination
 * 
 * @author jcarras
 * 
 */
public abstract class Task implements Runnable, OnClickListener,
		DialogInterface.OnClickListener, DialogInterface.OnCancelListener  {

	Vector observers = new Vector();
	boolean active;
	boolean canceled = false;	

	public TaskStatusListener addObserver(TaskStatusListener observer) {
		observers.add(observer);
		return observer;
	}

	public TaskStatusListener removeObserver(TaskStatusListener observer) {
		observers.remove(observer);
		return observer;
	}

	public void removeAllObservers() {
		observers.clear();
	}

	public void run() {
		active = true;
		if (observers == null) return;
		final int size = observers.size();
		TaskStatusListener obs;
		for (int i = 0; i < size; i++) {
			obs = (TaskStatusListener) observers.elementAt(i);
			if(obs != null)
				obs.taskInited(this);
		}
	}

	public void stop() {
		active = false;
		if (observers == null) return;
		final int size = observers.size();
		TaskStatusListener obs;
		for (int i = 0; i < size; i++) {
			obs = (TaskStatusListener) observers.elementAt(i);
			if(obs != null)
				obs.taskFinished(this);
		}
	}

	public void cancel() {
		canceled = true;
		active = false;
		if (observers == null) return;
		final int size = observers.size();
		TaskStatusListener obs;
		for (int i = 0; i < size; i++) {
			obs = (TaskStatusListener) observers.elementAt(i);
			if(obs != null)
				obs.taskCanceled(this);
		}
	}

	public boolean isActive() {
		return active;
	}

	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void onClick(View v) {
		WorkQueue.getExclusiveInstance().execute(this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		WorkQueue.getExclusiveInstance().execute(this);		
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel();
	}
	
	public abstract int getMessage();

}

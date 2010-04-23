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

import java.util.LinkedList;
/**
 * It's a pool of threads used to run tasks
 * Currently it's a singleton
 * 
 * @author jcarras
 *
 */
public class WorkQueue
{
    //private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    private static final WorkQueue instance = new WorkQueue(4);
    private static final WorkQueue exclusiveInstance = new WorkQueue(2);
    
    public static WorkQueue getInstance(){
    	return instance;
    }
    
    public static WorkQueue getExclusiveInstance(){
    	return exclusiveInstance;
    }
    
    
    private WorkQueue(int nThreads)
    {
        //this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker("WorkQueue Worker - " + i);
            threads[i].start();
        }
    }

    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
        
    }

    private class PoolWorker extends Thread {
    	public PoolWorker(String name){
    		setName(name);
    	}
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        	// System.out.println("One of the worker threads was interrupted while it was waiting.");
                        }
                    }

                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
    
    public void clearPendingTasks() {
    	this.queue.clear();
    }
    
    public void finalize()  
    {
    	for (int i=0; i<threads.length; i++) {
    		try {
    			threads[i].interrupt();
    		} catch (Exception ex) {
    			System.err.println("While interrupting thread: " + ex.getMessage());
    		}
        }
   }

}
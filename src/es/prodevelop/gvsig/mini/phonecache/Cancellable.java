/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* Prodevelop, S.L.
* Pza. Juan de Villarrasa, 14 - 5
* Valencia
* Spain
* Tel: +34 963510612
* e-mail: prode@prodevelop.es
* http://www.prodevelop.es
*
*/
package es.prodevelop.gvsig.mini.phonecache;



/**
 *
 * @author aromeu 
 * @author rblanco
 */
public class Cancellable {

    public Integer groupID;

    /**
     * 
     * @param groupId the Id of the group to cancel
     * @param isCanceled true if we want to cancel
     */
    public void setCanceled(boolean isCanceled) {
        if (groupID == null) {
            return;
        }
        try {
            Utilities.canceledGroup.remove(groupID);
        } catch (Exception e) {
        }

        Utilities.canceledGroup.put(groupID, new Boolean(isCanceled));
    }

    /**
     * Checks if the <code>groupId</code> is cancelled
     * @param groupId 
     * @return true if it's been cancelled, false if not or if it not exists
     */
    public boolean getCanceled() {
        try {
            if (groupID == null) {
                return true;
            }
            Object obj = Utilities.canceledGroup.get(groupID);
            if (obj != null) {
                return ((Boolean) obj).booleanValue();
            }
            return false;
        } catch (final Exception e) {
            return false;
        }
    }
}

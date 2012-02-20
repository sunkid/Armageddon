/**
 * LICENSING
 * 
 * This software is copyright by sunkid <sunkid@iminurnetz.com> and is
 * distributed under a dual license:
 * 
 * Non-Commercial Use:
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Commercial Use:
 *    Please contact sunkid@iminurnetz.com
 */
package com.iminurnetz.bukkit.plugin.armageddon.listeners;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import com.iminurnetz.bukkit.plugin.armageddon.ArmageddonPlugin;
import com.iminurnetz.bukkit.plugin.armageddon.Cannon;
import com.iminurnetz.bukkit.util.BlockLocation;
import com.sycoprime.movecraft.Craft;
import com.sycoprime.movecraft.events.MoveCraftMoveEvent;
import com.sycoprime.movecraft.events.MoveCraftTurnEvent;

public class MoveCraftListener implements Listener {

    private final ArmageddonPlugin plugin;

    public MoveCraftListener(ArmageddonPlugin plugin) {
        this.plugin = plugin;
    }

    public void onCustomEvent(Event event) {
        if (event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            return;
        }

        if (event instanceof MoveCraftMoveEvent) {
            MoveCraftMoveEvent me = (MoveCraftMoveEvent) event;
            Craft craft = me.getCraft();
            Vector move = me.getMovement();

            // plugin.log("MoveCraftMoveEvent: " + move + " at " +
            // craft.getLocation());

            Hashtable<BlockLocation, Cannon> cannons = plugin.getCannons();
            Hashtable<BlockLocation, Cannon> movedCannons = new Hashtable<BlockLocation, Cannon>();
            synchronized (cannons) {
                Iterator<Entry<BlockLocation, Cannon>> iCannons = cannons.entrySet().iterator();
                while (iCannons.hasNext()) {
                    Entry<BlockLocation, Cannon> e = iCannons.next();
                    Cannon cannon = e.getValue();
                    BlockLocation loc = e.getKey();
                    if (craft.isIn(loc.getX(), loc.getY(), loc.getZ())) {
                        iCannons.remove();
                        // plugin.log("moved cannon at " + loc);
                        movedCannons.put(loc.move(move), cannon);
                    }
                }

                cannons.putAll(movedCannons);
            }
        } else if (event instanceof MoveCraftTurnEvent) {
            MoveCraftTurnEvent te = (MoveCraftTurnEvent) event;
            Craft craft = te.getCraft();
            int degrees = te.getDegrees();

            // plugin.log("MoveCraftTurnEvent: " + degrees + " at " +
            // craft.getLocation());
            int dx = 0;
            int dz = 0;
            Location l = craft.getLocation();
            while (craft.isIn(l.getBlockX(), l.getBlockY(), l.getBlockZ())) {
                l.setX(l.getX() + 1);
                dx++;
            }

            l.setX(l.getX() - dx);
            while (craft.isIn(l.getBlockX(), l.getBlockY(), l.getBlockZ())) {
                l.setZ(l.getZ() + 1);
                dz++;
            }

            // plugin.log("Craft size is " + dx + "x" + dz);
        }
    }
}

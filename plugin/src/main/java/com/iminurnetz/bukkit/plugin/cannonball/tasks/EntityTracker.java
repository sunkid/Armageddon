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
package com.iminurnetz.bukkit.plugin.cannonball.tasks;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.iminurnetz.bukkit.plugin.cannonball.CannonBallPlugin;
import com.iminurnetz.bukkit.plugin.cannonball.TrackedLivingEntity;

public class EntityTracker implements Runnable {

    private final CannonBallPlugin plugin;

    public EntityTracker(CannonBallPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<TrackedLivingEntity> trackees = plugin.getTrackedEntities();
        synchronized (trackees) {
            Iterator<TrackedLivingEntity> i = trackees.iterator();
            TrackedLivingEntity e;
            while (i.hasNext()) {
                e = i.next();
                if (!(e.getEntity() instanceof Player)) {
                    if (e.isStunned()) {
                        e.getEntity().teleport(e.getLocation());
                    } else if (e.isSnared() || e.isDoused()) {
                        Vector movement = e.getEntity().getLocation().toVector().subtract(e.getLocation().toVector());
                        movement.multiply(0.2);
                        Location destination = e.getLocation().add(movement.getX(), movement.getY(), movement.getZ());
                        e.getEntity().teleport(destination);

                        if (e.isDoused()) {

                        }
                    }
                }

                if (e.getEntity().isDead() || !e.isStunned() && !e.isSnared() && !e.isDoused()) {
                    i.remove();
                }
            }
        }
    }
}

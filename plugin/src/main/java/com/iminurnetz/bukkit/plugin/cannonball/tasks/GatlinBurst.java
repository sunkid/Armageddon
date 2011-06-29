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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import com.iminurnetz.bukkit.plugin.cannonball.CannonBallPlugin;
import com.iminurnetz.bukkit.plugin.cannonball.arsenal.Gun;
import com.iminurnetz.bukkit.util.InventoryUtil;
import com.iminurnetz.bukkit.util.LocationUtil;

public class GatlinBurst implements Runnable {

    private final Player player;
    private final CannonBallPlugin plugin;

    public GatlinBurst(Player player, CannonBallPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        List<Integer> shots = new ArrayList<Integer>();
        while (true) {
            Gun gun = plugin.getGun(player);
            if (gun.getType() == plugin.getDefaultGun().getType() || gun.getShotsFired() == 0) {
                break;
            }

            Location handLocation = LocationUtil.getHandLocation(player);
            Snowball snowball = player.getWorld().spawn(handLocation, Snowball.class);
            snowball.setVelocity(handLocation.getDirection().multiply(8));
            shots.add(snowball.getEntityId());
            
            Block blockShotAt = null;
            List<Block> blockList = player.getLastTwoTargetBlocks(null, 1000);
            if (blockList.size() == 2) {
                blockShotAt = blockList.get(1);
            }

            plugin.registerGunShot(snowball, gun, blockShotAt);
            player.getWorld().createExplosion(handLocation, 0);
            InventoryUtil.removeItemNearItemHeldInHand(player, gun.getBulletMaterial());

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // ignored
            }
        }
        
        // we spawn a lot of stuff so let's clean up after a snooze
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignored
        }

        for (Integer i : shots) {
            plugin.removeBullet(i);
        }
    }

}

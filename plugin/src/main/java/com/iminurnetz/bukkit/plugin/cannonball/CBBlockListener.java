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
package com.iminurnetz.bukkit.plugin.cannonball;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.util.Vector;

public class CBBlockListener extends BlockListener {

    private final CannonBallPlugin plugin;
    
    public CBBlockListener(CannonBallPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.DISPENSER) {
            plugin.removeCannon(block);
        }
    }
    
    @Override
    public void onBlockDispense(BlockDispenseEvent event) {        
        Block block = event.getBlock();
        if (block.getType() == Material.DISPENSER && event.getItem().getType() == Material.TNT) {
            
            Cannon cannon = plugin.getCannon(block, false);
            if (cannon == null) {
                return;
            }
            
            Dispenser dispenser = (Dispenser) block.getType().getNewData(block.getData());
            
            double vx = cannon.getVelocity() * Math.cos((double) (Math.PI * cannon.getAngle() / 180));            
            double y = cannon.getVelocity() * Math.sin((double) (Math.PI * cannon.getAngle() / 180));
            
            double x = 0;
            double z = 0;
            
            switch (dispenser.getFacing()) {
            case NORTH:
                x = -vx;
                break;
            
            case SOUTH:
                x = vx;
                break;
                
            case WEST:
                z = vx;
                break;
                
            case EAST:
                z = -vx;
                break;
            }
        
            Vector initialVelocity = new Vector(x, y, z);
            
            // plugin.log("Setting velocity to " + initialVelocity);
            
            Location location = block.getRelative(dispenser.getFacing()).getLocation();           
            
            TNTPrimed tnt = event.getBlock().getWorld().spawn(location, TNTPrimed.class);
            
            tnt.setVelocity(initialVelocity);
            
            ItemStack ashes = new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 7);
            event.setItem(ashes);
            event.setVelocity(initialVelocity.multiply(.1));
            event.getBlock().getWorld().createExplosion(location, 0);
            
            Inventory inventory = ((org.bukkit.block.Dispenser) block.getState()).getInventory();
            inventory.removeItem(new ItemStack(Material.TNT, 1));
        }
    }
}

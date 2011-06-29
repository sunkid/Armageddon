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
package com.iminurnetz.bukkit.plugin.cannonball.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.material.Dispenser;
import org.bukkit.util.Vector;

import com.iminurnetz.bukkit.plugin.cannonball.CBConfiguration;
import com.iminurnetz.bukkit.plugin.cannonball.Cannon;
import com.iminurnetz.bukkit.plugin.cannonball.CannonBallPlugin;
import com.iminurnetz.bukkit.plugin.cannonball.arsenal.Grenade;
import com.iminurnetz.bukkit.plugin.cannonball.arsenal.Grenade.Type;
import com.iminurnetz.bukkit.util.MaterialUtils;

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
        if (block.getType() == Material.DISPENSER) {

            Cannon cannon = plugin.getCannon(block, false);
            if (cannon == null) {
                return;
            }

            Material material = event.getItem().getType();

            Grenade grenade = plugin.getConfig().getGrenade(material);

            plugin.log("Material: " + material + ", action: " + grenade.getType() + ", " + grenade.isCannonUse());

            if (grenade.getType() == Type.DUD || !grenade.isCannonUse()) {
                return;
            }

            Dispenser dispenser = (Dispenser) block.getType().getNewData(block.getData());

            double vx = cannon.getVelocity() * Math.cos((double) (Math.PI * cannon.getAngle() / 180));
            double y = cannon.getVelocity() * Math.sin((double) (Math.PI * cannon.getAngle() / 180));

            double x = 0;
            double z = 0;

            int yaw = 0;

            switch (dispenser.getFacing()) {
                case NORTH:
                    x = -vx;
                    yaw = 90;
                    break;

                case SOUTH:
                    x = vx;
                    yaw = 270;
                    break;

                case WEST:
                    z = vx;
                    yaw = 0;
                    break;

                case EAST:
                    z = -vx;
                    yaw = 180;
                    break;
            }

            Vector initialVelocity = new Vector(x, y, z);
            Location location = block.getRelative(dispenser.getFacing()).getLocation().add(0.5, 0.5, 0.5);
            World world = block.getWorld();

            Entity entity = null;

            x = location.getX();
            y = location.getY();
            z = location.getZ();

            Location locClone = location.clone();

            switch (grenade.getType()) {
                case PIG:
                    entity = world.spawn(location, Pig.class);
                    break;

                case COW:
                    entity = world.spawn(location, Cow.class);
                    break;

                case SHEEP:
                    entity = world.spawn(location, Sheep.class);
                    break;

                case TNT:
                    entity = world.spawn(location, TNTPrimed.class);
                    break;

                case EXPLOSIVE:
                case NUCLEAR:
                case WATER_BALLOON:
                case SPIDER_WEB:
                case SNARE:
                case STUN:
                    entity = world.spawn(location, Snowball.class);
                    break;

                case MOLOTOV:
                    locClone.setPitch(0);
                    locClone.setYaw(yaw);
                    entity = world.spawn(locClone, Fireball.class);
                    break;

                default:
                    return;
            }

            plugin.log("projectile: " + entity);

            if (entity != null) {
                if (!(entity instanceof Fireball)) {
                    entity.setVelocity(initialVelocity);
                }

                if (!(entity instanceof LivingEntity)) {
                    plugin.registerGrenade(entity, grenade);
                }

                plugin.adjustInventoryAndUsage(((org.bukkit.block.Dispenser) block.getState()).getInventory(), cannon, material, grenade.getUses());

                event.setCancelled(true);
                world.createExplosion(location, 0);
            }
        }
    }

    @Override
    public void onBlockFromTo(BlockFromToEvent event) {
        if (MaterialUtils.isWater(event.getBlock().getType())) {
            plugin.addBlockFlow(event.getBlock(), event.getToBlock());
        }
    }
}

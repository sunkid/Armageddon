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

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.iminurnetz.bukkit.plugin.cannonball.ArsenalAction.Type;
import com.iminurnetz.bukkit.plugin.util.MessageUtils;
import com.iminurnetz.bukkit.util.LocationUtil;

public class CBPlayerListener extends PlayerListener {

    private final CannonBallPlugin plugin;

    public CBPlayerListener(CannonBallPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.doCancelIfNeccessary(event)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && block.getType() == Material.DISPENSER) {

            switch (player.getItemInHand().getType()) {

                case AIR:
                    if (plugin.getPermissionHandler().canDisplay(player)) {
                        Cannon cannon = plugin.getCannon(block, false);
                        if (cannon == null) {
                            MessageUtils.send(player, "This is a normal dispenser not configured as a cannon");
                            if (plugin.getPermissionHandler().canConfigure(player)) {
                                MessageUtils.send(player, ChatColor.GREEN, plugin.getHelpText());
                            }
                        } else {
                            MessageUtils.send(player, cannon.toString());
                        }
                        event.setCancelled(true);
                    }
                    break;

                case TORCH:
                    if (plugin.getPermissionHandler().canConfigure(player)) {
                        Cannon cannon = plugin.getCannon(block, true);
                        if (cannon.equals(plugin.getCannon(player))) {
                            MessageUtils.send(player, ChatColor.RED, "Settings were not changed!");
                        } else {
                            cannon.copy(plugin.getCannon(player));
                            MessageUtils.send(player, ChatColor.GREEN, "Settings changed!");
                        }

                        MessageUtils.send(player, ChatColor.GREEN, cannon.toString());
                        event.setCancelled(true);
                    }
                    break;

                case REDSTONE:
                    if (!plugin.getPermissionHandler().canToggle(player)) {
                        break;
                    }

                    if (plugin.isCannon(block)) {
                        plugin.removeCannon(block);
                        MessageUtils.send(player, ChatColor.GREEN, "This dispenser no longer is a cannon!");
                        event.setCancelled(true);
                    } else {
                        Cannon cannon = plugin.getCannon(block, true);
                        MessageUtils.send(player, ChatColor.GREEN, "This dispenser is now a cannon!");
                        if (plugin.getPermissionHandler().canDisplay(player)) {
                            MessageUtils.send(player, ChatColor.GREEN, cannon.toString());
                        }
                        event.setCancelled(true);
                    }
                    break;

            }
        } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
            Material material = player.getItemInHand().getType();
            ArsenalAction action = plugin.getConfig().getAction(material);

            if (action.getType() == Type.NOTHING || 
                    !action.canPlayerUse() || !plugin.getPermissionHandler().canEffect(player, action)) {
                return;
            }

            action.setCannon(false);

            Vector v = LocationUtil.getHandLocation(player);
            World world = player.getWorld();

            float pitch = player.getLocation().getPitch();
            float yaw = player.getLocation().getYaw();

            Location handLocation = new Location(world, v.getX(), v.getY(), v.getZ(), yaw, pitch);
            Vector direction = handLocation.getDirection();

            Entity entity = null;

            double speedFactor = 1.5;

            switch (action.getType()) {

                case MOLOTOV:
                    entity = world.spawn(handLocation, Fireball.class);
                    break;

                case LIGHTNING:
                    List<Block> targetBlocks = player.getLastTwoTargetBlocks(null, 500);
                    if (targetBlocks.size() == 2) {
                        Block target = targetBlocks.get(1);
                        for (int n = 0; n < action.getYield(); n++) {
                            Block neighbor = LocationUtil.getRandomNeighbor(target, action.getYield());
                            world.strikeLightning(neighbor.getLocation());
                        }

                        updateInventory(player, material, action);
                    }
                    break;

                case CLUSTER:
                case NUCLEAR:
                case WATER_BALLOON:
                case SPIDER_WEB:
                case STUN:
                    entity = world.spawn(handLocation, Snowball.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    break;

                case GRENADE:
                    entity = world.spawn(handLocation, TNTPrimed.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    break;

                case FISH:

                    break;

                case PIG:
                    entity = world.spawn(handLocation, Pig.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    break;

                case SHEEP:
                    entity = world.spawn(handLocation, Sheep.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    break;

                case COW:
                    entity = world.spawn(handLocation, Cow.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    break;

                case FLAME_THROWER: // TODO
                case NOTHING:
                default:
                    return;
            }

            if (entity != null) {
                plugin.registerShot(entity, action);
                updateInventory(player, material, action);
            }
        }
    }

    private void updateInventory(Player player, Material material, ArsenalAction action) {
        PlayerSettings settings = plugin.getPlayerSettings(player, false);
        if (plugin.adjustInventoryAndUsage(player.getInventory(), settings, material, action.getUses())) {
            player.updateInventory();
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @Override
    public void onPlayerPortal(PlayerPortalEvent event) {
        plugin.doCancelIfNeccessary(event);
    }
}

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

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.iminurnetz.bukkit.plugin.armageddon.ArmageddonConfiguration;
import com.iminurnetz.bukkit.plugin.armageddon.ArmageddonPlugin;
import com.iminurnetz.bukkit.plugin.armageddon.Cannon;
import com.iminurnetz.bukkit.plugin.armageddon.PlayerSettings;
import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Grenade;
import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Grenade.Type;
import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Gun;
import com.iminurnetz.bukkit.plugin.armageddon.tasks.GatlinBurst;
import com.iminurnetz.bukkit.plugin.util.MessageUtils;
import com.iminurnetz.bukkit.util.InventoryUtil;
import com.iminurnetz.bukkit.util.LocationUtil;

public class ArmageddonPlayerListener extends PlayerListener {

    private final ArmageddonPlugin plugin;
    private final ArmageddonConfiguration config;

    public ArmageddonPlayerListener(ArmageddonPlugin plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.doCancelIfNeccessary(event)) {
            return;
        }

        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Material material = player.getItemInHand().getType();

        World world = player.getWorld();

        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        Location handLocation = LocationUtil.getHandLocation(player);
        Vector direction = handLocation.getDirection();

        Block blockShotAt = null;
        List<Block> blockList = player.getLastTwoTargetBlocks(null, 1000);
        if (blockList.size() == 2) {
            blockShotAt = blockList.get(1);
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && block.getType() == Material.DISPENSER) {

            switch (material) {

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
                        Cannon cannon = plugin.getCannon(block, false);
                        MessageUtils.send(player, ChatColor.GREEN, "This dispenser is now a cannon!");
                        if (plugin.getPermissionHandler().canDisplay(player)) {
                            MessageUtils.send(player, ChatColor.GREEN, cannon.toString());
                        }
                        event.setCancelled(true);
                    }
                    break;

            }
        } else if (event.getAction() == Action.LEFT_CLICK_AIR && config.isGunItem(material)) {
            Gun gun = plugin.getGun(player);
            
            if (gun.getType() == Gun.Type.TOY || !plugin.getPermissionHandler().canShoot(player, gun)) {
                return;
            }

            Entity entity = null;
            switch (gun.getType()) {
                case CROSSBOW:
                    entity = world.spawn(handLocation, Arrow.class);
                    entity.setVelocity(direction.multiply(3));
                    world.playEffect(handLocation, Effect.BOW_FIRE, 0);
                    break;

                case REVOLVER:
                    if (gun.getShotsFired() < 6) {
                        gun.fire();
                        entity = world.spawn(handLocation, Snowball.class);
                        entity.setVelocity(direction.multiply(3));
                    }
                    break;
                    
                case SHOTGUN:
                    if (gun.getShotsFired() < 1) {
                        gun.fire();

                        Location loc = handLocation.clone();
                        Random random = new Random((long) loc.lengthSquared() * new Date().getTime());

                        for (int n = 0; n < 20; n++) {
                            Snowball pellet = world.spawn(handLocation, Snowball.class);
                            pellet.setShooter(player);
                            loc.setPitch((float) (pitch + nextRandom(random)));
                            loc.setYaw((float) (yaw + nextRandom(random)));
                            Vector d = loc.getDirection();
                            pellet.setVelocity(d.multiply(4));
                            plugin.registerGunShot(pellet, gun, blockShotAt);
                        }
                        world.createExplosion(handLocation, 0);
                        InventoryUtil.removeItemNearItemHeldInHand(player, gun.getBulletMaterial());
                    }
                    break;

                case SNIPER:
                    entity = world.spawn(handLocation, Snowball.class);
                    entity.setVelocity(direction.multiply(8));
                    break;

                case GATLIN:
                    if (gun.getShotsFired() > 0) {
                        gun.setShotsFired(0);
                        return;
                    }

                    gun.fire();
                    new Thread(new GatlinBurst(player, plugin)).start();
                    break;

                case FLAME_THROWER:
                default:
                    plugin.log(Level.SEVERE, "Gun not implemented: " + gun.getType());
                    return;
            }

            if (entity != null) {
                ((Projectile) entity).setShooter(player);
                plugin.registerGunShot(entity, gun, blockShotAt);
                if (entity instanceof Snowball) {
                    world.createExplosion(handLocation, 0);
                }

                InventoryUtil.removeItemNearItemHeldInHand(player, gun.getBulletMaterial());
            }

        } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
            Grenade grenade = config.getGrenade(material);

            if (grenade.getType() == Type.DUD || !grenade.isPlayerUse() || !plugin.getPermissionHandler().canEffect(player, grenade)) {
                return;
            }

            Entity entity = null;

            double speedFactor = 1.5;

            switch (grenade.getType()) {

                case MOLOTOV:
                    entity = world.spawn(handLocation, Fireball.class);
                    break;

                /*
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
                */
                
                case SNARE:
                case STUN:
                case EXPLOSIVE:
                case NUCLEAR:
                case WATER_BALLOON:
                case SPIDER_WEB:
                    entity = world.spawn(handLocation, Snowball.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    break;

                case TNT:
                    entity = world.spawn(handLocation, TNTPrimed.class);
                    entity.setVelocity(direction.multiply(speedFactor));
                    ((TNTPrimed) entity).setFuseTicks(plugin.getCannon(player).getFuse());
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

                case DUD:
                default:
                    return;
            }

            if (entity != null) {
                plugin.registerGrenade(entity, grenade);
                updateInventory(player, material, grenade.getUses());
            }
        }
    }

    private double nextRandom(Random random) {
        double factor = 3;
        return random.nextDouble() * factor - random.nextDouble() * factor;
    }

    private void updateInventory(Player player, Material material, int uses) {
        PlayerSettings settings = plugin.getPlayerSettings(player, false);
        if (plugin.adjustInventoryAndUsage(player.getInventory(), settings, material, uses)) {
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

    @Override
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        Gun gun = plugin.getGun(event.getPlayer());
        gun.setShotsFired(0);
        if (gun.getType() != Gun.Type.TOY) {
            MessageUtils.send(event.getPlayer(), ChatColor.GREEN, "locked and loaded");
        }
    }
}

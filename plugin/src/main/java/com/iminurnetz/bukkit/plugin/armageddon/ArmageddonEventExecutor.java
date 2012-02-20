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
package com.iminurnetz.bukkit.plugin.armageddon;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.EventExecutor;

import com.iminurnetz.bukkit.plugin.BukkitPlugin;
import com.iminurnetz.bukkit.plugin.armageddon.listeners.ArmageddonBlockListener;
import com.iminurnetz.bukkit.plugin.armageddon.listeners.ArmageddonEntityListener;
import com.iminurnetz.bukkit.plugin.armageddon.listeners.ArmageddonPlayerListener;

public class ArmageddonEventExecutor implements EventExecutor {

    private final BukkitPlugin plugin;

    public ArmageddonEventExecutor(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (listener.getClass().equals(ArmageddonBlockListener.class)) {
            ArmageddonBlockListener l = (ArmageddonBlockListener) listener;
            if (event.getClass().equals(BlockDispenseEvent.class)) {
                l.onBlockDispense((BlockDispenseEvent) event);
            } else if (event.getClass().equals(BlockBreakEvent.class)) {
                l.onBlockBreak((BlockBreakEvent) event);
            } else if (event.getClass().equals(BlockFromToEvent.class)) {
                l.onBlockFromTo((BlockFromToEvent) event);
            }
        } else if (listener.getClass().equals(ArmageddonPlayerListener.class)) {
            ArmageddonPlayerListener l = (ArmageddonPlayerListener) listener;
            if (event.getClass().equals(PlayerChatEvent.class)) {
                l.onPlayerChat((PlayerChatEvent) event);
            } else if (event.getClass().equals(PlayerCommandPreprocessEvent.class)) {
                l.onPlayerCommandPreprocess((PlayerCommandPreprocessEvent) event);
            } else if (event.getClass().equals(PlayerInteractEvent.class)) {
                l.onPlayerInteract((PlayerInteractEvent) event);
            } else if (event.getClass().equals(PlayerMoveEvent.class)) {
                l.onPlayerMove((PlayerMoveEvent) event);
            } else if (event.getClass().equals(PlayerPickupItemEvent.class)) {
                l.onPlayerPickupItem((PlayerPickupItemEvent) event);
            } else if (event.getClass().equals(PlayerPortalEvent.class)) {
                l.onPlayerPortal((PlayerPortalEvent) event);
            } else if (event.getClass().equals(PlayerTeleportEvent.class)) {
                l.onPlayerTeleport((PlayerTeleportEvent) event);
            } else if (event.getClass().equals(PlayerItemHeldEvent.class)) {
                l.onItemHeldChange((PlayerItemHeldEvent) event);
            }
        } else if (listener.getClass().equals(ArmageddonEntityListener.class)) {
            ArmageddonEntityListener l = (ArmageddonEntityListener) listener;
            if (event.getClass().equals(EntityDamageEvent.class)) {
                l.onEntityDamage((EntityDamageEvent) event);
            } else if (event.getClass().equals(EntityExplodeEvent.class)) {
                l.onEntityExplode((EntityExplodeEvent) event);
            } else if (event.getClass().equals(EntityInteractEvent.class)) {
                l.onEntityInteract((EntityInteractEvent) event);
            } else if (event.getClass().equals(ExplosionPrimeEvent.class)) {
                l.onExplosionPrime((ExplosionPrimeEvent) event);
            } else if (event.getClass().equals(ProjectileHitEvent.class)) {
                l.onProjectileHit((ProjectileHitEvent) event);
            } else if (event.getClass().equals(EntityTargetEvent.class)) {
                l.onEntityTarget((EntityTargetEvent) event);
            } else if (event.getClass().equals(CreeperPowerEvent.class)) {
                l.onCreeperPower((CreeperPowerEvent) event);
            }
        }
    }
}

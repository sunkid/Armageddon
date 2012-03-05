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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.iminurnetz.bukkit.plugin.armageddon.ArmageddonPlugin;

public class TrackedEntityListener implements Listener {

    private final ArmageddonPlugin plugin;

    public TrackedEntityListener(ArmageddonPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreeperPower(CreeperPowerEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract(EntityInteractEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(PlayerChatEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        plugin.doCancelIfNeccessary(event);
    }
}

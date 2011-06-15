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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.iminurnetz.bukkit.plugin.util.MessageUtils;

public class CBPlayerListener extends PlayerListener {

    private final CannonBallPlugin plugin;
    public CBPlayerListener(CannonBallPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && 
                block.getType() == Material.DISPENSER) {
            
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
        }
    }
    
}

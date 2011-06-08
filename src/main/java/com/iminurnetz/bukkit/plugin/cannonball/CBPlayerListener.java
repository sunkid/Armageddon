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
            
            if (player.getItemInHand().getType() == Material.AIR) {
                Cannon cannon = plugin.getCannon(block, false);
                if (cannon == null) {
                    MessageUtils.send(player, "This is a normal dispenser not configured as a cannon");
                    MessageUtils.send(player, ChatColor.GREEN, plugin.getHelpText());
                } else {
                    MessageUtils.send(player, cannon.toString());
                }
                event.setCancelled(true);
            } else if (player.getItemInHand().getType() == Material.TORCH) {
                Cannon cannon = plugin.getCannon(block, true);
                if (cannon.equals(plugin.getCannon(player))) {
                    MessageUtils.send(player, ChatColor.RED, "Settings were not changed!");
                } else {
                    cannon.copy(plugin.getCannon(player));
                    MessageUtils.send(player, ChatColor.GREEN, "Settings changed!");
                }
                
                MessageUtils.send(player, ChatColor.GREEN, cannon.toString());
                event.setCancelled(true);
            } else if (player.getItemInHand().getType() == Material.REDSTONE && plugin.isCannon(block)) {
                plugin.removeCannon(block);
                MessageUtils.send(player, ChatColor.GREEN, "This dispenser no longer is a cannon!");
                event.setCancelled(true);
            } else if (player.getItemInHand().getType() == Material.REDSTONE) {
                Cannon cannon = plugin.getCannon(block, true);
                MessageUtils.send(player, ChatColor.GREEN, "This dispenser is now a cannon!");
                MessageUtils.send(player, ChatColor.GREEN, cannon.toString());
                event.setCancelled(true);
            }
        }
    }
    
}

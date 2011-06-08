package com.iminurnetz.bukkit.plugin.cannonball;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

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
                plugin.showCannonData(player, block);
                event.setCancelled(true);
            } else if (player.getItemInHand().getType() == Material.TORCH) {
                plugin.setCannon(player, block);
                event.setCancelled(true);
            }
                
        }
    }
    
}

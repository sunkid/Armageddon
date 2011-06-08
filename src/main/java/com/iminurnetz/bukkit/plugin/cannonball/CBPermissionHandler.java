package com.iminurnetz.bukkit.plugin.cannonball;

import org.bukkit.entity.Player;

import com.iminurnetz.bukkit.plugin.BukkitPermissionHandler;

public class CBPermissionHandler extends BukkitPermissionHandler {

    private static final String CAN_CONFIGURE = "cannonball.configure";
    private static final String CAN_DISPLAY = "cannonball.display";
    private static final String CAN_TOGGLE = "cannonball.toggle";

    protected CBPermissionHandler(CannonBallPlugin plugin) {
        super(plugin);
    }
    
    public boolean canConfigure(Player player) {
        return hasPermission(player, CAN_CONFIGURE);
    }

    public boolean canDisplay(Player player) {
        return hasPermission(player, CAN_DISPLAY) || canConfigure(player);
    }
    
    public boolean canToggle(Player player) {
        return hasPermission(player, CAN_TOGGLE) || canConfigure(player);
    }
}

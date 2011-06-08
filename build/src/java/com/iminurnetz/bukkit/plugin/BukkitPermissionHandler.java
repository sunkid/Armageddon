package com.iminurnetz.bukkit.plugin;

import org.bukkit.entity.Player;

import com.iminurnetz.bukkit.permissions.PermissionHandler;
import com.iminurnetz.bukkit.permissions.PermissionHandlerService;

public class BukkitPermissionHandler implements PermissionHandler {
    private PermissionHandler permissionHandler;

    protected BukkitPermissionHandler(BukkitPlugin plugin) {
        permissionHandler = PermissionHandlerService.getHandler(plugin);
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return player.isOp() || permissionHandler.hasPermission(player, permission);
    }

    @Override
    public String getGroup(Player player) {
        return permissionHandler.getGroup(player);
    }

    @Override
    public boolean parentGroupsInclude(Player player, String group) {
        return permissionHandler.parentGroupsInclude(player, group);
    }

}

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

import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.iminurnetz.bukkit.plugin.BukkitPermissionHandler;
import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Grenade;
import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Gun;

public class ArmageddonPermissionHandler extends BukkitPermissionHandler {

    private static final String CAN_CONFIGURE = "armageddon.configure";
    private static final String CAN_DISPLAY = "armageddon.display";
    private static final String CAN_TOGGLE = "armageddon.toggle";
    
    private static final String GRENADE_NODE = "armageddon.grenades.";
    private static final String GUN_NODE = "armageddon.guns.";

    private final ArmageddonPlugin plugin;

    protected ArmageddonPermissionHandler(ArmageddonPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
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
    
    public boolean canEffect(Player player, Grenade action) {
        String node = action.getType().toString().toLowerCase();
        return hasPermission(player, GRENADE_NODE + "*") || hasPermission(player, GRENADE_NODE + node);
    }

    public boolean canShoot(Player player, Gun action) {
        String node = action.getType().toString().toLowerCase();
        return hasPermission(player, GUN_NODE + "*") || hasPermission(player, GUN_NODE + node);
    }

    @Override
    public boolean hasPermission(Player player, String node) {
        String oldNode = node.replace("armageddon", "cannonball");
        if (super.hasPermission(player, oldNode) && !super.hasPermission(player, "*")) {
            plugin.log(Level.SEVERE, "old permission node found, please change to " + node);
            return true;
        }

        return super.hasPermission(player, node);
    }
}

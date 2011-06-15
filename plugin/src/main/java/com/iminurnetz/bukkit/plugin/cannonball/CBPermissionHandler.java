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

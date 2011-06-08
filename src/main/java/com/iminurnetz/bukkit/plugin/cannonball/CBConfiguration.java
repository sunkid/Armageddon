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

import com.iminurnetz.bukkit.plugin.util.ConfigurationService;

public class CBConfiguration extends ConfigurationService {

    private static final String LAST_CHANGED_IN_VERSION = "0.1";
    private static final String SETTINGS_NODE = "settings";
    
    private static final double DEFAULT_ANGLE = 35;
    private static final double DEFAULT_VELOCITY = 2;
    private static final int DEFAULT_FUSE = 80;

    private final CannonBallPlugin plugin;
    
    public CBConfiguration(CannonBallPlugin plugin) {
        super(plugin, LAST_CHANGED_IN_VERSION);
        this.plugin = plugin;
    }
    
    public double getAngle() {
        return plugin.getConfiguration().getDouble(SETTINGS_NODE + ".angle", DEFAULT_ANGLE);
    }

    public double getVelocity() {
        return plugin.getConfiguration().getDouble(SETTINGS_NODE + ".velocity", DEFAULT_VELOCITY);
    }
    
    public int getFuse() {
        return plugin.getConfiguration().getInt(SETTINGS_NODE + ".fuse", DEFAULT_FUSE);
    }
}

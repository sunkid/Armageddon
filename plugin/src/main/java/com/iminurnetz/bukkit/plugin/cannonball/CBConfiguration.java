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

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;

import com.iminurnetz.bukkit.plugin.cannonball.ArsenalAction.Type;
import com.iminurnetz.bukkit.plugin.util.ConfigurationService;
import com.iminurnetz.bukkit.util.MaterialUtils;

public class CBConfiguration extends ConfigurationService {

    private static final String LAST_CHANGED_IN_VERSION = "1.0";
    private static final String SETTINGS_NODE = "settings";
    
    private static final double DEFAULT_ANGLE = 35;
    private static final double DEFAULT_VELOCITY = 2;
    private static final int DEFAULT_FUSE = 80;
    private static final int DEFAULT_STUN_TIME = 10;
    private static final int DEFAULT_CANNON_FACTOR = 5;
    private static final String ARSENAL_NODE = SETTINGS_NODE + ".arsenal";
    
    private static final List<Material> DEFAULT_MATERIALS = 
        Arrays.asList(Material.MILK_BUCKET,
                      Material.PORK,
                      Material.WOOL,
                      Material.CLAY_BALL,
                      Material.TNT,
                      Material.GOLDEN_APPLE, 
                      Material.LAVA_BUCKET, 
                      Material.FLINT_AND_STEEL, 
                      Material.WATER_BUCKET, 
                      Material.STRING, 
                      Material.GOLD_SWORD);
    
    protected static final ArsenalAction DEFAULT_ACTION = new ArsenalAction(Type.NOTHING, 0, 0, false, false);

    private final CannonBallPlugin plugin;
    
    private final Hashtable<Material, ArsenalAction> arsenal;
    
    public CBConfiguration(CannonBallPlugin plugin) {
        super(plugin, LAST_CHANGED_IN_VERSION);
        this.plugin = plugin;
        arsenal = new Hashtable<Material, ArsenalAction>();
        for (Type type : Type.values()) {
            arsenal.put(getDefaultMaterial(type), new ArsenalAction(type, getDefaultYield(type), getDefaultUses(type)));
        }
        
        for (String type : plugin.getConfiguration().getKeys(ARSENAL_NODE)) {
            Type t = ArsenalAction.Type.valueOf(type.toUpperCase());
            if (t == null || t == Type.NOTHING) {
                plugin.log(Level.SEVERE, "Unknown arsenal type '" + type + "'; IGNORED!");
                continue;
            }
            
            String m = plugin.getConfiguration().getString(ARSENAL_NODE + "." + type + ".material");
            Material material = MaterialUtils.getMaterial(m);
            if (material == null) {
                plugin.log(Level.SEVERE, "Unknown material '" + m + "' for arsenal type '" + type + "'; IGNORED!");
                material = getDefaultMaterial(t);
            }
            
            float yield = (float) plugin.getConfiguration().getDouble(ARSENAL_NODE + "." + type + ".yield", getDefaultYield(t));
            int uses = plugin.getConfiguration().getInt(ARSENAL_NODE + "." + type + ".uses", getDefaultUses(t));
            
            boolean cannonUse = plugin.getConfiguration().getBoolean(ARSENAL_NODE + "." + type + ".cannon", true);
            boolean playerUse = plugin.getConfiguration().getBoolean(ARSENAL_NODE + "." + type + ".player", true);
            
            if (uses > 0) {
                arsenal.put(material, new ArsenalAction(t, yield, uses, playerUse, cannonUse));
            } else {
                arsenal.remove(material); 
            }
        }
    }
    
    private int getDefaultUses(Type type) {
        switch (type) {
        case COW:
            return 1;
            
        case PIG:
            return 1;
            
        case SHEEP:
            return 1;
            
        case STUN:
            return 2;
            
        case GRENADE:
            return 1;
            
        case NUCLEAR:
            return 1;
            
        case MOLOTOV:
            return 10;
            
        case FLAME_THROWER:
            return 5;
            
        case WATER_BALLOON:
            return 10;
            
        case SPIDER_WEB:
            return 10;
            
        case LIGHTENING:
            return 2;
            
        case NOTHING:
        default:
            return 0;
        }
    }

    private float getDefaultYield(Type type) {
        switch (type) {
        case COW:
            return 1;
            
        case PIG:
            return 1;
            
        case SHEEP:
            return 1;
            
        case STUN:
            return 2;
            
        case GRENADE:
            return 4;
            
        case NUCLEAR:
            return 8;
            
        case MOLOTOV:
            return 1;
            
        case FLAME_THROWER:
            return 2;
            
        case WATER_BALLOON:
            return 1;
            
        case SPIDER_WEB:
            return 2;
            
        case LIGHTENING:
            return 1;
            
        case NOTHING:
        default:
            return 0;
        }
    }

    private Material getDefaultMaterial(Type type) {
        switch (type) {
        case COW:
            return Material.MILK_BUCKET;
            
        case PIG:
            return Material.PORK;
            
        case SHEEP:
            return Material.WOOL;
            
        case STUN:
            return Material.CLAY_BALL;
            
        case GRENADE:
            return Material.TNT;
            
        case NUCLEAR:
            return Material.GOLDEN_APPLE;
            
        case MOLOTOV:
            return Material.LAVA_BUCKET;
            
        case FLAME_THROWER:
            return Material.FLINT_AND_STEEL;
            
        case WATER_BALLOON:
            return Material.WATER_BUCKET;
            
        case SPIDER_WEB:
            return Material.STRING;
            
        case LIGHTENING:
            return Material.GOLD_SWORD;
            
        case NOTHING:
        default:
            return Material.AIR;
        }
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

    public int getStunTime() {
        return plugin.getConfiguration().getInt(SETTINGS_NODE + ".stun-time", DEFAULT_STUN_TIME);
    }

    public int getCannonFactor() {
        return plugin.getConfiguration().getInt(SETTINGS_NODE + ".cannon-factor", DEFAULT_CANNON_FACTOR);
    }
    
    public ArsenalAction getAction(Material material) {
        if (!arsenal.contains(material)) {
            return DEFAULT_ACTION;
        }
        
        return arsenal.get(material);
    }

    public Hashtable<Material, ArsenalAction> getArsenal() {
        return arsenal;
    }
}

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
import java.util.Collections;
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
    private static final int DEFAULT_CANNON_FACTOR = 2;
    private static final String ARSENAL_NODE = SETTINGS_NODE + ".arsenal";

    protected static final ArsenalAction DEFAULT_ACTION = new ArsenalAction(Type.NOTHING, 0, 0, false, false);

    private final CannonBallPlugin plugin;

    private final Hashtable<Material, ArsenalAction> arsenal;
    
    public static List<Material> MELTABLE = Arrays.asList(
            Material.STONE,
            Material.GRASS,
            Material.DIRT,
            Material.COBBLESTONE,
            Material.SAPLING,
            Material.WATER,
            Material.SAND,
            Material.GRAVEL,
            Material.GOLD_ORE,
            Material.IRON_ORE,
            Material.COAL_ORE,
            Material.LAPIS_ORE,
            Material.LAPIS_BLOCK,
            Material.DISPENSER,
            Material.SANDSTONE,
            Material.NOTE_BLOCK,
            Material.GOLD_BLOCK,
            Material.IRON_BLOCK,
            Material.DOUBLE_STEP,
            Material.STEP,
            Material.BRICK,
            Material.MOSSY_COBBLESTONE,
            Material.OBSIDIAN,
            Material.DIAMOND_ORE,
            Material.DIAMOND_BLOCK,
            Material.SOIL,
            Material.COBBLESTONE_STAIRS,
            Material.STONE_PLATE,
            Material.IRON_DOOR_BLOCK,
            Material.REDSTONE_ORE,
            Material.GLOWING_REDSTONE_ORE,
            Material.CLAY,
            Material.NETHERRACK,
            Material.SOUL_SAND,
            Material.GLOWSTONE
    );
    
    public static List<Material> BURNABLE = Arrays.asList(
            Material.WOOD,
            Material.SAPLING,
            Material.LOG,
            Material.LEAVES,
            Material.SPONGE,
            Material.GLASS,
            Material.DISPENSER,
            Material.NOTE_BLOCK,
            Material.BED_BLOCK,
            Material.POWERED_RAIL,
            Material.DETECTOR_RAIL,
            Material.WEB,
            Material.LONG_GRASS,
            Material.DEAD_BUSH,
            Material.WOOL,
            Material.YELLOW_FLOWER,
            Material.RED_ROSE,
            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,
            Material.TNT,
            Material.BOOKSHELF,
            Material.TORCH,
            Material.MOB_SPAWNER,
            Material.WOOD_STAIRS,
            Material.CHEST,
            Material.WORKBENCH,
            Material.CROPS,
            Material.FURNACE,
            Material.BURNING_FURNACE,
            Material.SIGN_POST,
            Material.WOODEN_DOOR,
            Material.LADDER,
            Material.RAILS,
            Material.WALL_SIGN,
            Material.LEVER,
            Material.IRON_DOOR_BLOCK,
            Material.WOOD_PLATE,
            Material.CACTUS,
            Material.SUGAR_CANE_BLOCK,
            Material.JUKEBOX,
            Material.FENCE,
            Material.PUMPKIN,
            Material.JACK_O_LANTERN,
            Material.CAKE_BLOCK,
            Material.DIODE_BLOCK_OFF,
            Material.DIODE_BLOCK_ON,
            Material.LOCKED_CHEST,
            Material.TRAP_DOOR
    );

    public CBConfiguration(CannonBallPlugin plugin) {
        super(plugin, LAST_CHANGED_IN_VERSION);
        this.plugin = plugin;
        arsenal = new Hashtable<Material, ArsenalAction>();
        for (Type type : Type.values()) {
            arsenal.put(getDefaultMaterial(type), new ArsenalAction(type, getDefaultYield(type), getDefaultUses(type)));
        }

        List<String> arsenalList = plugin.getConfiguration().getKeys(ARSENAL_NODE);
        if (arsenalList == null) {
            arsenalList = Collections.emptyList();
        }

        for (String type : arsenalList) {
            Type t = null;
            try {
                t = ArsenalAction.Type.valueOf(type.toUpperCase());
            } catch (Exception e) {
                // nada
            }

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
            case FISH: // Fish are not fish
                return 0;

            case COW:
            case PIG:
            case SHEEP:
            case GRENADE:
            case CLUSTER:
            case NUCLEAR:
            case STUN:
                return 1;

            case LIGHTNING:
                return 20;

            case MOLOTOV:
            case WATER_BALLOON:
            case SPIDER_WEB:
                return 10;

            case FLAME_THROWER:
                return 5;

            case NOTHING:
            default:
                return 0;
        }
    }

    protected float getDefaultYield(Type type) {
        switch (type) {
            case STUN:
            case FLAME_THROWER:
            case SPIDER_WEB:
                return 2;

            case GRENADE:
            case CLUSTER:
            case LIGHTNING:
                return 4;

            case NUCLEAR:
                return 8;

            case MOLOTOV:
            case WATER_BALLOON:
                return 1;

            case NOTHING:
            default:
                return 0;
        }
    }

    private Material getDefaultMaterial(Type type) {
        switch (type) {
            case FISH:
                return Material.FEATHER;

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

            case CLUSTER:
                return Material.SLIME_BALL;

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

            case LIGHTNING:
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
        if (!arsenal.containsKey(material)) {
            return DEFAULT_ACTION;
        }

        return arsenal.get(material);
    }

    public Hashtable<Material, ArsenalAction> getArsenal() {
        return arsenal;
    }

    public ArsenalAction getAction(Type type) {
        for (Material m : arsenal.keySet()) {
            if (arsenal.get(m).getType() == type) {
                return arsenal.get(m);
            }
        }
        return DEFAULT_ACTION;
    }
}

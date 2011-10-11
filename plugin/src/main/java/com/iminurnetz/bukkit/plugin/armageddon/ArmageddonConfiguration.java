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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Grenade;
import com.iminurnetz.bukkit.plugin.armageddon.arsenal.Gun;
import com.iminurnetz.bukkit.plugin.util.ConfigurationService;
import com.iminurnetz.bukkit.util.MaterialUtils;

public class ArmageddonConfiguration extends ConfigurationService {

    private static final double LAST_CHANGED_IN_VERSION = 1.4;
    private static final String SETTINGS_NODE = "settings";

    private static final double DEFAULT_ANGLE = 35;
    private static final double DEFAULT_VELOCITY = 2;
    private static final int DEFAULT_FUSE = 80;

    private static final int DEFAULT_STUN_TIME = 10;

    private static final int DEFAULT_CANNON_FACTOR = 2;
    private static final String GRENADE_NODE = SETTINGS_NODE + ".grenades";

    private static final String GUN_ITEM_NODE = SETTINGS_NODE + ".gun-item";
    private static final String GUNS_NODE = SETTINGS_NODE + ".guns";

    protected static final Grenade DEFAULT_GRENADE = new Grenade(Grenade.Type.DUD, 0, 0);
    protected static final Gun DEFAULT_GUN = new Gun(Gun.Type.TOY, Material.AIR, 0);

    private final Hashtable<Material, Grenade> grenades;
    private final Hashtable<Material, Gun> guns;
    private final Material gunItem;

    public static List<Material> MELTABLE = Arrays.asList(Material.STONE, Material.GRASS, Material.DIRT, Material.COBBLESTONE, Material.SAPLING, Material.WATER, Material.SAND, Material.GRAVEL, Material.GOLD_ORE, Material.IRON_ORE, Material.COAL_ORE, Material.LAPIS_ORE, Material.LAPIS_BLOCK, Material.DISPENSER, Material.SANDSTONE, Material.NOTE_BLOCK, Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.DOUBLE_STEP, Material.STEP, Material.BRICK, Material.MOSSY_COBBLESTONE, Material.OBSIDIAN, Material.DIAMOND_ORE, Material.DIAMOND_BLOCK, Material.SOIL, Material.COBBLESTONE_STAIRS, Material.STONE_PLATE, Material.IRON_DOOR_BLOCK, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE, Material.CLAY, Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE);

    public static List<Material> BURNABLE = Arrays.asList(Material.WOOD, Material.SAPLING, Material.LOG, Material.LEAVES, Material.SPONGE, Material.GLASS, Material.DISPENSER, Material.NOTE_BLOCK, Material.BED_BLOCK, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.WEB, Material.LONG_GRASS, Material.DEAD_BUSH, Material.WOOL, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.TNT, Material.BOOKSHELF, Material.TORCH, Material.MOB_SPAWNER, Material.WOOD_STAIRS, Material.CHEST, Material.WORKBENCH, Material.CROPS, Material.FURNACE, Material.BURNING_FURNACE, Material.SIGN_POST, Material.WOODEN_DOOR, Material.LADDER, Material.RAILS, Material.WALL_SIGN, Material.LEVER, Material.IRON_DOOR_BLOCK, Material.WOOD_PLATE, Material.CACTUS, Material.SUGAR_CANE_BLOCK, Material.JUKEBOX, Material.FENCE, Material.PUMPKIN, Material.JACK_O_LANTERN, Material.CAKE_BLOCK, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.LOCKED_CHEST, Material.TRAP_DOOR);

    public static List<Material> LETS_WATER_THROUGH = new ArrayList<Material>();
    static {
        for (Material m : Material.values()) {
            if (MaterialUtils.isTraversable(m) || MaterialUtils.isSameMaterial(m, Material.DEAD_BUSH, Material.SIGN_POST, Material.FENCE)) {
                LETS_WATER_THROUGH.add(m);
            }
        }
    }
    
    private final FileConfiguration config;

    public ArmageddonConfiguration(ArmageddonPlugin plugin) {
        super(plugin, LAST_CHANGED_IN_VERSION);
        grenades = new Hashtable<Material, Grenade>();
        
        config = getConfiguration();

        for (String m : getConfigurationNodes(GRENADE_NODE)) {
            Material material = MaterialUtils.getMaterial(m);
            if (material == null && !m.equals("disable-joke")) {
                plugin.log(Level.SEVERE, "Unknown material '" + m + "' - IGNORED!");
                continue;
            } else if (m.equals("disable-joke")) {
                continue;
            }

            String configNode = GRENADE_NODE + "." + m;

            String t = config.getString(configNode + ".action");
            Grenade.Type type = null;
            try {
                type = Grenade.Type.valueOf(t.toUpperCase());
            } catch (Exception e) {
                // handled below
            }

            if (type == null || type == Grenade.Type.DUD) {
                plugin.log(Level.SEVERE, "Unknown grenade type '" + material + "'; IGNORED!");
                continue;
            }

            float yield = (float) config.getDouble(configNode + ".yield", getDefaultYield(type));
            int clusterSize = config.getInt(configNode + ".cluster", 1);
            int uses = config.getInt(configNode + ".uses", getDefaultUses(type));

            boolean cannonUse = config.getBoolean(configNode + ".cannon", true);
            boolean playerUse = config.getBoolean(configNode + ".player", true);

            int cannonFactor = config.getInt(configNode + ".cannon-factor", 2);

            if (uses > 0) {
                grenades.put(material, new Grenade(type, clusterSize, yield, uses, playerUse, cannonUse, cannonFactor));
            } else {
                plugin.log(material + " disabled as 'uses' is set to " + uses);
            }
        }

        if (config.getBoolean(GRENADE_NODE + ".disable-joke", false)) {
            grenades.put(Material.MILK_BUCKET, new Grenade(Grenade.Type.COW, 1, 1));
            grenades.put(Material.PORK, new Grenade(Grenade.Type.PIG, 1, 1));
            grenades.put(Material.WOOL, new Grenade(Grenade.Type.SHEEP, 1, 1));
        }

        Material gunItemMaterial = MaterialUtils.getMaterial(config.getString(GUN_ITEM_NODE));
        if (grenades.containsKey(gunItemMaterial)) {
            plugin.log(Level.SEVERE, "Gun item is configured already as a grenade - GUNS ARE DISABLED!");
            gunItem = null;
        } else {
            gunItem = gunItemMaterial;
            if (gunItem == null) {
                plugin.log(Level.SEVERE, "Unknown gun item - GUNS ARE DISABLED!");
            }
        }

        guns = new Hashtable<Material, Gun>();
        for (String t : getConfigurationNodes(GUNS_NODE)) {
            Gun.Type type = null;
            try {
                type = Gun.Type.valueOf(t.toUpperCase());
            } catch (Exception e) {
                plugin.log(Level.SEVERE, "Unknown gun type '" + t + "'; IGNORED!");
                continue;
            }

            String configNode = GUNS_NODE + "." + t;

            String m = config.getString(configNode + ".material");
            Material material = MaterialUtils.getMaterial(m);
            if (material == null) {
                plugin.log(Level.SEVERE, "Unknown material '" + m + "' - IGNORED!");
                continue;
            }

            int damage = 0;
            switch (type) {
                case CROSSBOW:
                    damage = 2;
                    break;

                case SHOTGUN:
                    damage = 1;
                    break;

                case REVOLVER:
                case SNIPER:
                case GATLIN:
                    damage = 3;
                    break;
            }

            damage = (int) (2 * config.getDouble(configNode + ".damage", damage));
            guns.put(material, new Gun(type, material, damage));
        }
    }

    private int getDefaultUses(Grenade.Type type) {
        switch (type) {
            case PIG:
            case COW:
            case SHEEP:
            case SNARE:
            case NUCLEAR:
            case STUN:
            case EXPLOSIVE:
            case TNT:
                return 1;

            case MOLOTOV:
            case WATER_BALLOON:
            case SPIDER_WEB:
                return 10;

            case DUD:
            default:
                return 0;
        }
    }

    protected float getDefaultYield(Grenade.Type type) {
        switch (type) {
            case MOLOTOV:
                return 1;

            case SNARE:
            case STUN:
                return 2;

            case WATER_BALLOON:
                return 3;

            case SPIDER_WEB:
            case EXPLOSIVE:
            case TNT:
                return 4;

            case NUCLEAR:
                return 8;

            case DUD:
            default:
                return 0;
        }
    }

    public double getAngle() {
        return config.getDouble(SETTINGS_NODE + ".angle", DEFAULT_ANGLE);
    }

    public double getVelocity() {
        return config.getDouble(SETTINGS_NODE + ".velocity", DEFAULT_VELOCITY);
    }

    public int getFuse() {
        return config.getInt(SETTINGS_NODE + ".fuse", DEFAULT_FUSE);
    }

    public int getStunTime() {
        return config.getInt(SETTINGS_NODE + ".stun-time", DEFAULT_STUN_TIME);
    }

    public int getCannonFactor() {
        return config.getInt(SETTINGS_NODE + ".cannon-factor", DEFAULT_CANNON_FACTOR);
    }

    public Grenade getGrenade(Material material) {
        if (!grenades.containsKey(material)) {
            return DEFAULT_GRENADE;
        }

        return grenades.get(material);
    }

    public Gun getGun(Material material) {
        if (!guns.containsKey(material)) {
            return DEFAULT_GUN;
        }

        return guns.get(material);
    }
    public boolean isGunItem(Material material) {
        return gunItem != null ? gunItem == material : false;
    }
}

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.iminurnetz.bukkit.plugin.BukkitPlugin;
import com.iminurnetz.bukkit.plugin.util.MessageUtils;
import com.iminurnetz.bukkit.util.BlockLocation;

public class CannonBallPlugin extends BukkitPlugin {

    private Hashtable<BlockLocation, Cannon> cannons;
    private Hashtable<String, PlayerSettings> playerSettings;
    private List<StunnedLivingEntity> theStunned;
    private int stunnerTaskId = -1;

    private CBConfiguration config;
    private CBPermissionHandler permissionHandler;

    private Hashtable<Integer, ArsenalAction> shotsFired;
    private List<Location> nuclearExplosions;

    public int MIN_SERVER_VERSION = 875;

    public int getMinimumServerVersion() {
        return this.MIN_SERVER_VERSION;
    }

    @Override
    public void enablePlugin() throws Exception {

        shotsFired = new Hashtable<Integer, ArsenalAction>();
        theStunned = new ArrayList<StunnedLivingEntity>();

        nuclearExplosions = new ArrayList<Location>();

        config = new CBConfiguration(this);
        permissionHandler = new CBPermissionHandler(this);

        loadCannonsFile();

        PluginManager pm = getServer().getPluginManager();

        CBBlockListener blockListener = new CBBlockListener(this);

        pm.registerEvent(Event.Type.BLOCK_DISPENSE, blockListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Monitor, this);

        CBPlayerListener playerListener = new CBPlayerListener(this);

        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_PORTAL, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Highest, this);

        CBEntityListener entityListener = new CBEntityListener(this);

        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.ENTITY_INTERACT, entityListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.EXPLOSION_PRIME, entityListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PROJECTILE_HIT, entityListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.ENTITY_TARGET, entityListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.CREEPER_POWER, entityListener, Priority.Highest, this);

        log("enabled.");
    }

    @Override
    public void onDisable() {
        cancelStunnerTask();
        saveCannonsFile();
        log("disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage("This command is only accessible in-game");
            return false;
        }

        if (command.getName().equals("cb")) {
            if (!permissionHandler.canConfigure(player)) {
                MessageUtils.send(player, ChatColor.RED, "You are not allowed to use this command!");
                return true;
            }

            if (args.length == 0) {
                showCannonData(player);
                return true;
            } else if (args.length > 3) {
                return false;
            }

            if (playerSettings.get(player.getName()) == null) {
                playerSettings.put(player.getName(), new PlayerSettings(getDefaultCannon()));
            }

            Cannon cannon = playerSettings.get(player.getName()).getCannon();
            boolean success = true;

            if (args.length == 3) {
                try {
                    cannon.setFuse(Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    success = false;
                }
            }

            if (args.length > 1) {
                try {
                    cannon.setVelocity(Double.parseDouble(args[1]));
                } catch (NumberFormatException e) {
                    success = false;
                }
            }

            try {
                cannon.setAngle(Double.parseDouble(args[0]));
            } catch (NumberFormatException e) {
                success = false;
            }

            if (!success) {
                return false;
            }

            MessageUtils.send(player, ChatColor.RED, "Your settings were changed:");
            MessageUtils.send(player, ChatColor.RED, cannon.toString());
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void loadCannonsFile() {

        cannons = new Hashtable<BlockLocation, Cannon>();
        playerSettings = new Hashtable<String, PlayerSettings>();

        File cache = getCannonsFile();
        if (!cache.exists()) {
            return;
        }

        try {
            FileInputStream fis = new FileInputStream(cache);
            ObjectInputStream in = new ObjectInputStream(fis);
            cannons = (Hashtable<BlockLocation, Cannon>) in.readObject();
            playerSettings = (Hashtable<String, PlayerSettings>) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            log(Level.SEVERE, "Cannot load cached cannons and settings, starting from scratch", e);
            cannons = new Hashtable<BlockLocation, Cannon>();
            playerSettings = new Hashtable<String, PlayerSettings>();
        }
    }

    private File getCannonsFile() {
        return new File(this.getDataFolder(), "cannons.ser");
    }

    private void saveCannonsFile() {
        File cache = getCannonsFile();

        File dataDir = cache.getParentFile();
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(cache);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(cannons);
            out.writeObject(playerSettings);
            out.close();
            fos.close();
        } catch (Exception e) {
            log(Level.SEVERE, "Cannot cache cannons and settings", e);
        }
    }

    public Cannon getCannon(Block block, boolean createIfNotExist) {
        BlockLocation location = new BlockLocation(block);
        synchronized (cannons) {
            if (!cannons.containsKey(location) && createIfNotExist) {
                Cannon cannon = getDefaultCannon();
                cannons.put(location, cannon);
            }

            return cannons.get(location);
        }
    }

    public Cannon getCannon(Player player) {
        PlayerSettings settings = getPlayerSettings(player, true);
        return settings.getCannon();
    }

    public PlayerSettings getPlayerSettings(Player player, boolean alertPlayer) {
        synchronized (playerSettings) {
            String name = player.getName();
            if (!playerSettings.containsKey(name)) {
                playerSettings.put(name, new PlayerSettings(getDefaultCannon()));
                if (alertPlayer) {
                    MessageUtils.send(player, ChatColor.RED, "Default cannon settings used!\n" + getHelpText());
                }
            }
            return playerSettings.get(name);
        }
    }

    public String getHelpText() {
        return "Use '/cb [angle [velocity [fuse]]]' to configure your settings!\n" + "Display a cannon's settings by left-clicking it bare handed\n" + "Toggle dispensers/cannons by left-clicking with red stone dust\n" + "Set a cannon to your settings by left-clicking it with a torch\n";
    }

    public Cannon getDefaultCannon() {
        return new Cannon(getConfig().getAngle(), getConfig().getVelocity(), getConfig().getFuse());
    }

    public void showCannonData(Player player) {
        Cannon cannon = getCannon(player);
        MessageUtils.send(player, cannon.toString());
    }

    protected CBConfiguration getConfig() {
        return config;
    }

    protected CBPermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void removeCannon(Block block) {
        BlockLocation location = new BlockLocation(block);
        cannons.remove(location);
    }

    public boolean isCannon(Block block) {
        BlockLocation location = new BlockLocation(block);
        return cannons.containsKey(location);
    }

    public void registerShot(Entity projectile, ArsenalAction action) {
        shotsFired.put(projectile.getEntityId(), action);
    }

    public boolean wasFired(Entity projectile) {
        return projectile != null && shotsFired.containsKey(projectile.getEntityId());
    }

    public void stun(Entity entity, double yield) {
        synchronized (theStunned) {
            for (Entity e : entity.getNearbyEntities(yield, yield, yield)) {
                if (!(e instanceof LivingEntity)) {
                    continue;
                }

                StunnedLivingEntity stunnee = new StunnedLivingEntity((LivingEntity) e);
                if (theStunned.contains(stunnee)) {
                    // log("Now with more stunning!");
                    stunnee = theStunned.get(theStunned.indexOf(stunnee));
                } else {
                    theStunned.add(stunnee);
                }

                stunnee.stun(config.getStunTime());
                // log("Stunning " + stunnee.getEntity() + "(" + stunnee.getEntity().getEntityId() + ") until " + stunnee.getReleaseDate());
            }

            if (theStunned.size() != 0 && stunnerTaskId == -1) {
                stunnerTaskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Stunner(this), 0, 1);
                log("Scheduled new task " + stunnerTaskId);
            }
        }
    }

    public List<StunnedLivingEntity> getTheStunned() {
        return theStunned;
    }

    protected void cancelStunnerTask() {
        log("Cancelling task " + stunnerTaskId);
        getServer().getScheduler().cancelTask(stunnerTaskId);
        stunnerTaskId = -1;
    }

    public boolean isStunned(LivingEntity entity) {
        return getStunnee(entity) != null;
    }

    public StunnedLivingEntity getStunnee(LivingEntity entity) {
        StunnedLivingEntity stunnee = new StunnedLivingEntity(entity);
        if (theStunned.contains(stunnee)) {
            return theStunned.get(theStunned.indexOf(stunnee));
        }
        return null;
    }

    protected boolean doCancelIfNeccessary(Event event) {
        if (theStunned.size() == 0 || !(event instanceof Cancellable)) {
            return false;
        }

        LivingEntity entity = null;
        if (event instanceof PlayerEvent) {
            entity = ((PlayerEvent) event).getPlayer();
        } else if (event instanceof EntityEvent && ((EntityEvent) event).getEntity() instanceof LivingEntity) {
            entity = (LivingEntity) ((EntityEvent) event).getEntity();
        } else {
            return false;
        }

        StunnedLivingEntity stunnee = getStunnee(entity);
        if (stunnee != null) {
            ((Cancellable) event).setCancelled(true);
            if (entity instanceof Player && !(event instanceof PlayerMoveEvent)) {
                MessageUtils.send((Player) entity, ChatColor.RED, "No can do, you're stunned!");
            } else if (event instanceof PlayerMoveEvent) { // shouldn't have to do this!
                ((Cancellable) event).setCancelled(false);
                ((PlayerMoveEvent) event).setTo(stunnee.getLocation());
            }

            return true;
        }

        return false;
    }

    public ArsenalAction getAction(Entity projectile) {
        if (!wasFired(projectile)) {
            return CBConfiguration.DEFAULT_ACTION;
        }

        return shotsFired.get(projectile.getEntityId());
    }

    public boolean adjustInventoryAndUsage(Inventory inventory, UsageTracker settings, Material material, int uses) {
        if (settings.getUsage(material) <= 0) {
            settings.setUsage(material, uses);
        }

        settings.use(material);

        if (settings.getUsage(material) == 0) {
            inventory.removeItem(new ItemStack(material, 1));
            if (material.name().endsWith("_BUCKET")) {
                inventory.addItem(new ItemStack(Material.BUCKET, 1));
            }
            return true;
        }

        return false;
    }

    public void removeShot(Entity entity) {
        shotsFired.remove(entity);
    }

    protected void goBoom(Entity entity) {
        if (!wasFired(entity)) {
            return;
        }

        float yield = 0;
        boolean setFire = false;

        ArsenalAction action = getAction(entity);
        Location loc = entity.getLocation();
        // log("Action: " + action.getType() + ", " + action.getYield());
        removeShot(entity);

        Location locClone = loc.clone();

        World world = entity.getWorld();

        switch (action.getType()) {
            case STUN:
                stun(entity, action.getYield());
                break;

            case GRENADE:
                yield = action.getYield();
                break;

            case CLUSTER:
                Snowball cb;
                Random random = new Random((long) (loc.lengthSquared() * loc.getPitch() * loc.getYaw()));

                ArsenalAction cbAction = config.getAction(ArsenalAction.Type.GRENADE);
                if (cbAction.getType() == ArsenalAction.Type.NOTHING) {
                    cbAction = new ArsenalAction(ArsenalAction.Type.GRENADE, config.getDefaultYield(ArsenalAction.Type.GRENADE), 1);
                }

                locClone.setPitch(-45);
                for (int n = 0; n < action.getYield(); n++) {
                    locClone.setYaw(random.nextFloat() * 360);
                    cb = world.spawn(loc, Snowball.class);
                    cb.setVelocity(locClone.getDirection().multiply(random.nextDouble()));
                    registerShot(cb, cbAction);
                }
                break;

            case MOLOTOV:
                yield = action.getYield();
                setFire = true;
                break;

            case NUCLEAR:
                yield = action.getYield();
                setFire = true;
                locClone.setPitch(0);
                locClone.setYaw(0);
                nuclearExplosions.add(locClone);
                break;

            case SPIDER_WEB:
                world.playEffect(loc, Effect.EXTINGUISH, 0);
                // TODO: turn all air blocks around impact area to webs
                break;

            case FLAME_THROWER:
                world.playEffect(loc, Effect.EXTINGUISH, 0);
                // TODO: light'er up!
                break;

            case WATER_BALLOON:
                world.playEffect(loc, Effect.EXTINGUISH, 0);
                // TODO: splash!
                break;

            case LIGHTNING:
            case NOTHING:
            default:
                return;
        }

        world.createExplosion(entity.getLocation(), yield, setFire);
    }

    protected void goNuclear(Location location, List<Block> blocks) {
        if (nuclearExplosions.remove(location)) {
            for (Block target : blocks) {
                for (BlockFace dir : Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN)) {
                    Block neighbor = target.getRelative(dir);
                    if (!blocks.contains(neighbor) && neighbor.getType() != Material.AIR) {
                        neighbor.setType(Material.LAVA);
                    }
                }
            }
        }
    }
}

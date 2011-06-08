package com.iminurnetz.bukkit.plugin.cannonball;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.iminurnetz.bukkit.plugin.BukkitPlugin;
import com.iminurnetz.bukkit.plugin.util.MessageUtils;
import com.iminurnetz.bukkit.util.BlockLocation;

public class CannonBallPlugin extends BukkitPlugin {

    private Hashtable<BlockLocation, Cannon> cannons;
    private Hashtable<String, Cannon> playerSettings;
    
    @Override
    public void enablePlugin() throws Exception {        
        loadCannonsFile();       
        
        CBPlayerListener playerListener = new CBPlayerListener(this);
        CBBlockListener blockListener = new CBBlockListener(this);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_DISPENSE, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);

        log("enabled.");
    }
    
    @Override
    public void onDisable() {
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
            if (args.length == 0) {
                showCannonData(player);
                return true;
            } else if (args.length > 3) {
                return false;
            }
            
            Cannon cannon = getDefaultCannon();
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
            
            playerSettings.put(player.getName(), cannon);
            MessageUtils.send(player, ChatColor.RED, "Your settings were changed:");
            MessageUtils.send(player, ChatColor.RED, cannon.toString());
            return true;
        }
        
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void loadCannonsFile() {
        
        File cache = getCannonsFile();
        if (!cache.exists()) {
            cannons = new Hashtable<BlockLocation, Cannon>();
            return;
        }
        
        try {
            FileInputStream fis = new FileInputStream(cache);
            ObjectInputStream in = new ObjectInputStream(fis);
            cannons = (Hashtable<BlockLocation, Cannon>) in.readObject();
            playerSettings = (Hashtable<String, Cannon>) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            log(Level.SEVERE, "Cannot load cached cannons and settings, starting from scratch", e);
            cannons = new Hashtable<BlockLocation, Cannon>();
            playerSettings = new Hashtable<String, Cannon>();
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
        synchronized (playerSettings) {
            String name = player.getName();
            if (! playerSettings.containsKey(name)) {
                playerSettings.put(name, getDefaultCannon());
                MessageUtils.send(player, ChatColor.RED, "Default cannon settings used!\n" + getHelpText());
            }
            
            return playerSettings.get(name);
        }
    }

    private String getHelpText() {
        return "Use '/cb [angle [velocity [fuse]]]' to configure your settings!\n" +
            "Set a cannon to these settings by left-clicking it with a torch\n" +
            "Display a cannons settings by left-clicking it bare handed";
    }

    public void setCannon(Player player, Block block) {
        Cannon cannon = getCannon(block, true);
        if (cannon.equals(getCannon(player))) {
            MessageUtils.send(player, ChatColor.RED, "Settings were not changed!");
        } else {
            cannon.copy(getCannon(player));
            MessageUtils.send(player, ChatColor.RED, "Settings changed!");
        }
        
        MessageUtils.send(player, ChatColor.RED, cannon.toString());
    }
    
    public Cannon getDefaultCannon() {
        return new Cannon(30, 2, 80);
    }

    public void showCannonData(Player player) {
        Cannon cannon = getCannon(player);
        MessageUtils.send(player, cannon.toString());
    }
    
    public void showCannonData(Player player, Block block) {
        Cannon cannon = getCannon(block, false);
        if (cannon == null) {
            MessageUtils.send(player, "This is a normal dispenser not configured as a cannon");
            MessageUtils.send(player, ChatColor.GREEN, getHelpText());
        } else {
            MessageUtils.send(player, cannon.toString());
        }
    }
}

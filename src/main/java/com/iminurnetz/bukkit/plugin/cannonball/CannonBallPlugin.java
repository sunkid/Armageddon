package com.iminurnetz.bukkit.plugin.cannonball;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.logging.Level;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.iminurnetz.bukkit.plugin.BukkitPlugin;
import com.iminurnetz.bukkit.util.BlockLocation;

public class CannonBallPlugin extends BukkitPlugin {

    private Hashtable<BlockLocation, Cannon> cannons;
    
    @Override
    public void enablePlugin() throws Exception {
        
        loadCannons();
        
        CBBlockListener blockListener = new CBBlockListener(this);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_DISPENSE, blockListener, Priority.Lowest, this);

        log("enabled.");
    }
    
    @Override
    public void onDisable() {
        saveCannonsFile();
        log("disabled.");
    }
    
    @SuppressWarnings("unchecked")
    private void loadCannons() {
        
        File cache = getCannonsFile();
        if (!cache.exists()) {
            cannons = new Hashtable<BlockLocation, Cannon>();
            return;
        }
        
        try {
            FileInputStream fis = new FileInputStream(cache);
            ObjectInputStream in = new ObjectInputStream(fis);
            cannons = (Hashtable<BlockLocation, Cannon>) in.readObject();
            in.close();
            fis.close();
        } catch (Exception e) {
            log(Level.SEVERE, "Cannot load cached chunks, starting from scratch", e);
            cannons = new Hashtable<BlockLocation, Cannon>();
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
            out.close();
            fos.close();
        } catch (Exception e) {
            log(Level.SEVERE, "Cannot cache chunks", e);
        }
    }

    public Cannon getCannon(Block block) {
        BlockLocation location = new BlockLocation(block);        
        synchronized (cannons) {
            if (!cannons.contains(location)) {
                Cannon cannon = new Cannon(30, 2, 80);
                cannons.put(location, cannon);
            }

            return cannons.get(location);
        }
    }
}

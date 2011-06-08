package com.iminurnetz.bukkit.plugin.cannonball;

import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.NBTTagCompound;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.util.Vector;

public class CBBlockListener extends BlockListener {

    private final CannonBallPlugin plugin;
    
    public CBBlockListener(CannonBallPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.DISPENSER) {
            plugin.removeCannon(block);
        }
    }
    
    @Override
    public void onBlockDispense(BlockDispenseEvent event) {        
        Block block = event.getBlock();
        if (block.getType() == Material.DISPENSER && event.getItem().getType() == Material.TNT) {
            
            Cannon cannon = plugin.getCannon(block, false);
            if (cannon == null) {
                return;
            }
            
            Dispenser dispenser = (Dispenser) block.getType().getNewData(block.getData());
            
            double vx = cannon.getVelocity() * Math.cos((double) (Math.PI * cannon.getAngle() / 180));            
            double y = cannon.getVelocity() * Math.sin((double) (Math.PI * cannon.getAngle() / 180));
            
            double x = 0;
            double z = 0;
            
            switch (dispenser.getFacing()) {
            case NORTH:
                x = -vx;
                break;
            
            case SOUTH:
                x = vx;
                break;
                
            case WEST:
                z = vx;
                break;
                
            case EAST:
                z = -vx;
                break;
            }
        
            Vector initialVelocity = new Vector(x, y, z);
            
            plugin.log("Setting velocity to " + initialVelocity);
            
            Location location = block.getRelative(dispenser.getFacing()).getLocation();           
            
            // TNTPrimed tnt = event.getBlock().getWorld().spawn(location, TNTPrimed.class);
            
            CraftWorld world = (CraftWorld) block.getWorld();
            EntityTNTPrimed tntEntity = new EntityTNTPrimed(world.getHandle(), location.getX(), location.getY(), location.getZ());
            
            /*
            NBTTagCompound tag = new NBTTagCompound();
            tag.a("Fuse", cannon.getFuse());            
            tntEntity.a(tag);
            */
            
            world.getHandle().addEntity(tntEntity);
            
            TNTPrimed tnt = (TNTPrimed) tntEntity.getBukkitEntity();
            
            tnt.setVelocity(initialVelocity);
            
            ItemStack ashes = new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 7);
            event.setItem(ashes);
            event.setVelocity(new Vector(0,0,0));
            
            Inventory inventory = ((org.bukkit.block.Dispenser) block.getState()).getInventory();
            inventory.removeItem(new ItemStack(Material.TNT, 1));
        }
    }
}

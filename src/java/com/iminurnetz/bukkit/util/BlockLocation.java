package com.iminurnetz.bukkit.util;

import java.io.Serializable;

public class BlockLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String world;

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    private final int x;
    private final int y;
    private final int z;
    
    public BlockLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
}
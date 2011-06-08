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

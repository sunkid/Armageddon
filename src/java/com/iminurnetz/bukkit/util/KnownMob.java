package com.iminurnetz.bukkit.util;

import java.io.Serializable;

import org.bukkit.entity.LivingEntity;

import com.iminurnetz.util.StringUtils;

public enum KnownMob implements Serializable {
    CHICKEN,
    COW,
    CREEPER,
    FISH,
    GHAST,
    GIANT,
    PIG,
    PIG_ZOMBIE,
    PLAYER,
    SHEEP,
    SLIME,
    SPIDER,
    SQUID,
    WOLF,
    ZOMBIE;
    
    @SuppressWarnings("unchecked")
    public Class<? extends LivingEntity> getEntityClass() {
        Class<? extends LivingEntity> clazz = null;
        String pkg = "org.bukkit.entity.";
        String className = StringUtils.toCamelCase(name());

        try {
            clazz = (Class<? extends LivingEntity>) Class.forName(pkg + className);
        } catch (Exception e) {
            System.err.println("Programming error!");
            e.printStackTrace(System.err);
        }
        
        return clazz;
    }
    
    public static KnownMob getMobForEntity(LivingEntity entity) {
        if (entity == null)
            return null;
        
        for (KnownMob mob : values()) {
            if (mob.getEntityClass().isInstance(entity)) {
                return mob;
            }
        }
        
        return null;
    }
}

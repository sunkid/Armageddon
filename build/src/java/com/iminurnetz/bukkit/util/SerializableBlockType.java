package com.iminurnetz.bukkit.util;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SerializableBlockType implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String material;
    
    public SerializableBlockType(Block block) {
        this(block.getType(), block.getData());
    }
    
    public SerializableBlockType(Material type, byte data) {
        this(type.name(), data);
    }

    public SerializableBlockType(String material, byte data) {
        this.material = material;
        this.data = data;
    }
    
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }
    public byte getData() {
        return data;
    }
    public void setData(byte data) {
        this.data = data;
    }
    private byte data;

    
    

}

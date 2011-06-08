package com.iminurnetz.bukkit.plugin.cannonball;

import java.io.Serializable;

public class Cannon implements Serializable {
    private static final long serialVersionUID = 1L;

    private double angle;
    private double velocity;
    private int fuse;

    public Cannon(double angle, double velocity, int fuse) {
        this.angle = angle;
        this.velocity = velocity;
        this.fuse = fuse;
    }
    
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public int getFuse() {
        return fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }
}

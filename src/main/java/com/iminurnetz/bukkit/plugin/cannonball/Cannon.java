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
    
    public void copy(Cannon cannon) {
        setAngle(cannon.getAngle());
        setVelocity(cannon.getVelocity());
        setFuse(cannon.getFuse());
    }
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Cannon)) {
            return false;
        }
        
        Cannon that = (Cannon) o;
        return (that.getAngle() == this.getAngle() &&
                that.getVelocity() == this.getVelocity() &&
                that.getFuse() == this.getFuse());
    }
    
    @Override
    public int hashCode() {
        return (int) ((Double.doubleToLongBits(this.getAngle()) * 37) +
                Double.doubleToLongBits(this.getVelocity())) * 31 +
                this.getFuse();
    }
    
    @Override
    public String toString() {
        return "Cannon set to " + String.format("%.1f", getAngle()) +
            " deg. at " + String.format("%.1f", getVelocity()) +
            "m/s, with a " + getFuse() + " tick fuse";
    }
}

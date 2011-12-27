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
package com.iminurnetz.bukkit.plugin.armageddon;

import java.io.Serializable;

public class Cannon extends UsageTracker implements Serializable {
    private static final long serialVersionUID = 1L;

    private double angle;
    private double velocity;
    private int fuse;
    private String owner;
    
    public Cannon(double angle, double velocity, int fuse) {
        this(angle, velocity, fuse, null);
    }

    public Cannon(double angle, double velocity, int fuse, String owner) {
        super();
        this.angle = angle;
        this.velocity = velocity;
        this.fuse = fuse;
        this.owner = owner;
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
    
    @Override
    public Cannon clone() {
        Cannon c = new Cannon(angle, velocity, fuse, owner);
        return c;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

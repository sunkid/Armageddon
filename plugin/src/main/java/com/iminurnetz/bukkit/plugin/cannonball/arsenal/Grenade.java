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
package com.iminurnetz.bukkit.plugin.cannonball.arsenal;

public class Grenade {

    public enum Type {
        COW,
        PIG,
        SHEEP,
        DUD,
        STUN, 
        SNARE,
        EXPLOSIVE,
        TNT,
        NUCLEAR,
        MOLOTOV,
        WATER_BALLOON,
        SPIDER_WEB;
    }

    private final Type type;
    private final int clusterSize;
    private final float yield;
    private final int uses;
    private final boolean isPlayerUse;
    private final boolean isCannonUse;
    private final int cannonFactor;

    public Grenade(Type type, int clusterSize, float yield) {
        this(type, clusterSize, yield, 1, true, true, 2);
    }

    public Grenade(Type type, int clusterSize, float yield, int uses, boolean isPlayerUse, boolean isCannonUse, int cannonFactor) {
        this.type = type;
        this.clusterSize = clusterSize;
        this.yield = yield;
        this.uses = uses;
        this.isPlayerUse = isPlayerUse;
        this.isCannonUse = isCannonUse;
        this.cannonFactor = cannonFactor;
    }

    public Type getType() {
        return type;
    }

    public int getClusterSize() {
        return clusterSize;
    }

    public float getYield() {
        return yield;
    }

    public int getUses() {
        return uses;
    }

    public boolean isPlayerUse() {
        return isPlayerUse;
    }

    public boolean isCannonUse() {
        return isCannonUse;
    }

    public int getCannonFactor() {
        return cannonFactor;
    }
}

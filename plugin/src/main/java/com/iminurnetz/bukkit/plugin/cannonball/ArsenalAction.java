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
package com.iminurnetz.bukkit.plugin.cannonball;

public class ArsenalAction {
    
    public enum Type {
        NOTHING,
        COW,
        PIG,
        SHEEP,
        STUN,
        GRENADE,
        NUCLEAR,
        MOLOTOV,
        FLAME_THROWER,
        WATER_BALLOON,
        SPIDER_WEB, 
        LIGHTENING;
    }

    private final Type type;
    private final int uses;
    private final boolean canPlayerUse;
    private final boolean canCannonUse;
    
    private float yield;
    
    public ArsenalAction(Type type, float yield, int uses) {
        this(type, yield, uses, true, true);
    }

    public ArsenalAction(Type type, float yield, int uses, boolean canPlayerUse, boolean canCannonUse) {
        this.type = type;
        this.yield = yield;
        this.uses = uses;
        this.canPlayerUse = canPlayerUse;
        this.canCannonUse = canCannonUse;
    }

    public Type getType() {
        return type;
    }

    public int getUses() {
        return uses;
    }

    public float getYield() {
        return yield;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }

    public boolean canPlayerUse() {
        return uses > 0 && canPlayerUse;
    }

    public boolean canCannonUse() {
        return uses > 0 && canCannonUse;
    }
}

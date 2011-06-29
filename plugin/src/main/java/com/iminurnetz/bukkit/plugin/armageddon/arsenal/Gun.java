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
package com.iminurnetz.bukkit.plugin.armageddon.arsenal;

import java.io.Serializable;

import org.bukkit.Material;

import com.iminurnetz.bukkit.util.MaterialUtils;

public class Gun implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type {
        TOY,
        FLAME_THROWER,
        CROSSBOW,
        REVOLVER,
        SHOTGUN,
        SNIPER,
        GATLIN;
    }

    private final Type type;
    private final int damage;
    private final String bulletMaterial;

    private int shotsFired;

    public Gun(Type type, Material bulletMaterial, int damage) {
        this.type = type;
        this.bulletMaterial = bulletMaterial.name();
        this.damage = damage;
    }

    public Type getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public void setShotsFired(int shotsFired) {
        this.shotsFired = shotsFired;
    }

    public void fire() {
        shotsFired++;
    }

    public Material getBulletMaterial() {
        return MaterialUtils.getMaterial(bulletMaterial);
    }
}

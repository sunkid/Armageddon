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

import com.iminurnetz.bukkit.plugin.cannonball.arsenal.Gun;

public class PlayerSettings extends UsageTracker {
    private static final long serialVersionUID = 1L;
    private Cannon cannon;
    private Gun gun;
    
    public PlayerSettings(Cannon cannon, Gun gun) {
        super();
        this.cannon = cannon;
        this.gun = gun;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public Cannon getCannon() {
        return cannon;
    }

    public void setGun(Gun gun) {
        this.gun = gun;
    }

    public Gun getGun() {
        return gun;
    }
}

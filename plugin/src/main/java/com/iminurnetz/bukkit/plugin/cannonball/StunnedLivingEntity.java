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

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class StunnedLivingEntity {
    private Date releaseDate;

    private final Location location;
    private final LivingEntity entity;

    public StunnedLivingEntity(LivingEntity entity) {
        this.entity = entity;
        this.location = entity.getLocation().clone();

        releaseDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StunnedLivingEntity) {
            return ((StunnedLivingEntity) o).entity.getUniqueId().equals(entity.getUniqueId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return entity.getUniqueId().hashCode();
    }

    public void stun(int stunTime) {
        if (!isStunned()) {
            releaseDate = new Date();
        }

        releaseDate.setTime(releaseDate.getTime() + 1000 * stunTime);
    }

    public boolean isStunned() {
        return releaseDate.after(new Date());
    }

    public Location getLocation() {
        return location;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}

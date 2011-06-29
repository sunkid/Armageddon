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

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class TrackedLivingEntity {
    private Date stunReleaseDate;
    private Date snareReleaseDate;
    private Date douseReleaseDate;

    private Location location;
    private final LivingEntity entity;

    public TrackedLivingEntity(LivingEntity entity) {
        this.entity = entity;
        this.location = entity.getLocation().clone();

        stunReleaseDate = new Date();
        snareReleaseDate = new Date();
        douseReleaseDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TrackedLivingEntity) {
            return ((TrackedLivingEntity) o).entity.getUniqueId().equals(entity.getUniqueId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return entity.getUniqueId().hashCode();
    }

    public void stun(int stunTime) {
        hit(stunReleaseDate, stunTime, isStunned());
    }

    public void snare(int snareTime) {
        hit(snareReleaseDate, snareTime, isSnared());
    }

    public void douse(int douseTime) {
        hit(douseReleaseDate, douseTime, isDoused());
    }

    private void hit(final Date date, int time, boolean additive) {
        if (!additive) {
            date.setTime(new Date().getTime());
        }

        date.setTime(date.getTime() + 1000 * time);
    }

    public boolean isStunned() {
        return stunReleaseDate.after(new Date());
    }

    public boolean isSnared() {
        return snareReleaseDate.after(new Date());
    }

    public boolean isDoused() {
        return douseReleaseDate.after(new Date());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Date getReleaseDate() {
        return stunReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.stunReleaseDate = releaseDate;
    }
}

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

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class CBEntityListener extends EntityListener {

    private final CannonBallPlugin plugin;

    public CBEntityListener(CannonBallPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEntityDamage(EntityDamageEvent e) {
        if (e instanceof EntityDamageByProjectileEvent) {
            EntityDamageByProjectileEvent event = (EntityDamageByProjectileEvent) e;
            Projectile projectile = event.getProjectile();
            if (plugin.wasFired(projectile)) {
                ((CraftWorld) projectile.getWorld()).getHandle().createExplosion(((CraftEntity) projectile).getHandle(), projectile.getLocation().getX(), projectile.getLocation().getY(), projectile.getLocation().getZ(), 0, false);
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Entity entity = event.getEntity();
        ArsenalAction action = plugin.getAction(entity);
        Location loc = entity.getLocation();

        plugin.removeShot(entity);

        switch (action.getType()) {
        case STUN:
            plugin.stun(entity, action.getYield());
            event.setYield(0);
            break;

        case GRENADE:
            if (entity instanceof TNTPrimed) {
                event.setYield(action.getYield());
            } else {
                event.setCancelled(true);
                ((CraftWorld) entity.getWorld()).getHandle().createExplosion(((CraftEntity) entity).getHandle(), loc.getX(), loc.getY(), loc.getZ(), action.getYield(), false);
            }
            break;

        case MOLOTOV:
            if (entity instanceof Fireball) {
                event.setYield(action.getYield());
            } else {
                event.setCancelled(true);
                ((CraftWorld) entity.getWorld()).getHandle().createExplosion(((CraftEntity) entity).getHandle(), loc.getX(), loc.getY(), loc.getZ(), action.getYield(), true);
            }
            break;

        case NUCLEAR:
            event.setYield(action.getYield());
            break;

        case LIGHTENING:
            event.setCancelled(true);
            // TODO add yield
            entity.getWorld().strikeLightning(loc);
            break;

        case SPIDER_WEB:
            event.setCancelled(true);
            entity.getWorld().playEffect(loc, Effect.EXTINGUISH, 0);
            // TODO: turn all air blocks around impact area to webs
            break;

        case FLAME_THROWER:
            event.setCancelled(true);
            entity.getWorld().playEffect(loc, Effect.EXTINGUISH, 0);
            // TODO: light'er up!
            break;

        case WATER_BALLOON:
            event.setCancelled(true);
            entity.getWorld().playEffect(loc, Effect.EXTINGUISH, 0);
            // TODO: splash!
            break;

        case NOTHING:
        default:
            return;
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (plugin.wasFired(event.getEntity())) {
            Projectile projectile = (Projectile) event.getEntity();
            ((CraftWorld) projectile.getWorld()).getHandle().createExplosion(((CraftEntity) projectile).getHandle(), projectile.getLocation().getX(), projectile.getLocation().getY(), projectile.getLocation().getZ(), 0, false);
        }
    }

    public void onEntityTarget(EntityTargetEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    public void onCreeperPower(CreeperPowerEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    public void onExplosionPrime(ExplosionPrimeEvent event) {
        plugin.doCancelIfNeccessary(event);
    }

    public void onEntityInteract(EntityInteractEvent event) {
        plugin.doCancelIfNeccessary(event);
    }
}

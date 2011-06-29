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
package com.iminurnetz.bukkit.plugin.cannonball.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Attachable;

import com.iminurnetz.bukkit.plugin.cannonball.CannonBallPlugin;
import com.iminurnetz.bukkit.util.MaterialUtils;

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
            if (e.isCancelled()) {
                plugin.removeGrenade(projectile);
                plugin.removeBullet(projectile);
                return;
            }

            if (plugin.isGrenade(projectile)) {
                plugin.explodeGrenade(projectile);
                event.setCancelled(true);
            } else if (plugin.isBullet(projectile)) {
                int damage = plugin.getBulletDamage(projectile);
                // plugin.log("Damaged " + event.getEntity() + " (" + damage + ")");
                if (event.getEntity() instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) event.getEntity();
                    le.setNoDamageTicks(le.getMaximumNoDamageTicks());
                    le.setLastDamage(0);
                }
                event.setDamage(damage);
                plugin.removeBullet(projectile);
            }
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        plugin.goNuclear(event.getLocation(), event.blockList());

        Entity entity = event.getEntity();
        if (plugin.isGrenade(entity)) {
            plugin.explodeGrenade(entity);
            event.setCancelled(true);
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = (Projectile) event.getEntity();
        if (plugin.isGrenade(projectile)) {
            plugin.explodeGrenade(projectile);
        } else if (plugin.isBullet(projectile)) {
            Block block = plugin.getBlockShotAt(projectile);
            if (block != null) {
                Material material = block.getType();
                if (MaterialUtils.isHarvestablePlant(material) || (material.getData() != null && Attachable.class.isAssignableFrom(material.getData())) || material == Material.GLASS) {
                    BlockBreakEvent e = new BlockBreakEvent(block, (Player) projectile.getShooter());
                    plugin.getServer().getPluginManager().callEvent(e);
                    if (!e.isCancelled()) {
                        BlockState state = block.getState();
                        block.setType(Material.AIR);
                        for (ItemStack stack : MaterialUtils.getDroppedMaterial(state)) {
                            block.getWorld().dropItemNaturally(block.getLocation(), stack);
                        }
                    }
                }
            }
            plugin.removeBullet(projectile);
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

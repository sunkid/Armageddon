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
package com.iminurnetz.bukkit.plugin.armageddon.tasks;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.iminurnetz.bukkit.plugin.armageddon.ArmageddonPlugin;
import com.iminurnetz.bukkit.util.MaterialUtils;

public class WaterTracker implements Runnable {

    private final Set<Block> blocks;
    private final ArmageddonPlugin plugin;
    private int id;

    public WaterTracker(ArmageddonPlugin plugin, Set<Block> blocks) {
        this.plugin = plugin;
        this.blocks = blocks;
    }

    @Override
    public void run() {
        synchronized (blocks) {
            plugin.removeWaterTracker(id);
            Iterator<Block> i = blocks.iterator();
            while (i.hasNext()) {
                Block b = i.next();
                i.remove();
                if (MaterialUtils.isWater(b.getType())) {
                    b.setType(Material.AIR);
                }
            }
        }
    }

    public void addBlock(Block from, Block to) {
        if (blocks.contains(from)) {
            blocks.add(to);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

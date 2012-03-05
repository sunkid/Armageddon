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

public class SpiderWebTracker implements Runnable {

    private final Set<Block> webs;
    private final ArmageddonPlugin plugin;

    public SpiderWebTracker(ArmageddonPlugin plugin, Set<Block> webs) {
        this.plugin = plugin;
        this.webs = webs;
    }

    @Override
    public void run() {
        synchronized (webs) {
            Iterator<Block> i = webs.iterator();
            while (i.hasNext()) {
                Block b = i.next();
                i.remove();
                plugin.removeWeb(b);
                if (b.getType() == Material.WEB) {
                    b.setType(Material.AIR);
                }
            }
        }
    }
}

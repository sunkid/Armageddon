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

import java.io.Serializable;
import java.util.Hashtable;

import org.bukkit.Material;

public abstract class UsageTracker implements Serializable {
    private static final long serialVersionUID = 1L;
    private Hashtable<String, Integer> usage;

    public UsageTracker() {
        usage = new Hashtable<String, Integer>();
    }

    public int getUsage(Material material) {
        if (!usage.containsKey(material.name())) {
            return -1;
        }
        return usage.get(material.name());
    }
    
    public void setUsage(Material material, int uses) {
        usage.put(material.name(), uses);
    }
    
    public void use(Material material) {
        setUsage(material, getUsage(material) - 1);
    }
    
}

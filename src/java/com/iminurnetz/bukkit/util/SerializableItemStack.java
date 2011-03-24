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
package com.iminurnetz.bukkit.util;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SerializableItemStack implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String material;
	private final short damage;
	private final int amount;
	private final byte data;

	public SerializableItemStack(ItemStack stack) {
		this.material = stack.getType().name();
		this.damage = stack.getDurability();
		this.amount = stack.getAmount();
		this.data = stack.getData() != null ? stack.getData().getData()
				: (byte) 0;
	}

	public ItemStack getStack() {
		return new ItemStack(Material.getMaterial(material), amount, damage,
				data);
	}

}

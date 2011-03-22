package com.iminurnetz.bukkit.util;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PersistentItemStack implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String material;
	private final short damage;
	private final int amount;
	private final byte data;

	public PersistentItemStack(ItemStack stack) {
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

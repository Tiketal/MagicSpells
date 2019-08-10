package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class MagicItemMaterial extends MagicMaterial {
	Material type;

	public MagicItemMaterial(Material type) {
		this.type = type;
	}
	
	@Override
	public Material getMaterial() {
		return type;
	}

	@Override
	public BlockData getBlockData() {
		return null;
	}

	@Override
	public ItemStack toItemStack(int quantity) {
		ItemStack item = new ItemStack(getMaterial(), quantity);
		return item;
	}

	@Override
	public boolean equals(ItemStack item) {
		return type == item.getType();
	}
	
}
package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.nisovin.magicspells.util.Util;

public class MagicItemMaterial extends MagicMaterial {
	Material type;
	short duraData;

	public MagicItemMaterial(Material type, short data) {
		this.type = type;
		this.duraData = data;
	}
	
	public short getDurability() {
		return duraData;
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
		Util.setItemDamage(item, getDurability());
		return item;
	}

	@Override
	public boolean equals(ItemStack item) {
		return type == item.getType() && duraData == (short)Util.getItemDamage(item);
	}
	
}
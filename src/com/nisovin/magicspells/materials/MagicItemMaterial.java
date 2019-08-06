package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

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
		if (item.getItemMeta() instanceof Damageable) {
			((Damageable)item.getItemMeta()).setDamage(getDurability());
		}
		return item;
	}

	@Override
	public boolean equals(ItemStack item) {
		return type == item.getType() && ((item.getItemMeta() instanceof Damageable) 
				? duraData == (short)((Damageable)item.getItemMeta()).getDamage()
				: true);
	}
	
}
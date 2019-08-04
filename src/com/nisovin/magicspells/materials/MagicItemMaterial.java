package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class MagicItemMaterial extends MagicMaterial {
	short duraData;

	public MagicItemMaterial(Material type, short data) {
		this.type = type;
		this.duraData = data;
	}
	
	public short getDurability() {
		return duraData;
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
		return type == item.getType() && (item.getItemMeta() instanceof Damageable) 
				? duraData == ((Damageable)item.getItemMeta()).getDamage()
				: true;
	}
	
}
package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MagicItemAnyDataMaterial extends MagicItemMaterial {
	
	public MagicItemAnyDataMaterial(Material type) {
		super(type, (short)0);
	}
	
	@Override
	public boolean equals(ItemStack item) {
		return type == item.getType();
	}
	
}

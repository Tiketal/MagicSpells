package com.nisovin.magicspells.materials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagicItemWithNameMaterial extends MagicMaterial {

	MagicMaterial material;
	String name;
	
	public MagicItemWithNameMaterial(MagicMaterial material, String name) {
		this.material = material;
		this.name = ChatColor.translateAlternateColorCodes('&', name);
	}
	
	@Override
	public Material getMaterial() {
		return material.getMaterial();
	}
	
	@Override
	public BlockData getBlockData() {
		return material.getBlockData();
	}

	@Override
	public ItemStack toItemStack(int quantity) {
		ItemStack item = material.toItemStack(quantity);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(ItemStack item) {
		if (!material.equals(item)) return false;
		String iname = item.getItemMeta().getDisplayName();
		if (iname == null || iname.isEmpty()) return false;
		return iname.equals(name);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MagicItemWithNameMaterial) {
			MagicItemWithNameMaterial m = (MagicItemWithNameMaterial)o;
			return m.getMaterial() == getMaterial() && m.name.equals(name)
					&& ((m.getBlockData() == null) 
						? getBlockData() == null
						: m.getBlockData().equals(getBlockData()));
		}
		return false;
	}

}

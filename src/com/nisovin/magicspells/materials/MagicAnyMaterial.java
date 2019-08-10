package com.nisovin.magicspells.materials;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class MagicAnyMaterial extends MagicMaterial {
	private String pattern;
	private BlockData base;
	
	public MagicAnyMaterial(BlockData base, String regex) {
		this.base = base;
		this.pattern = regex.toLowerCase();
	}

	@Override
	public Material getMaterial() {
		return base.getMaterial();
	}

	@Override
	public BlockData getBlockData() {
		return base;
	}

	@Override
	public ItemStack toItemStack(int quantity) {
		return new ItemStack(getMaterial(), quantity);
	}

	@Override
	public boolean equals(ItemStack itemStack) {
		return isSimilar(itemStack.getType().name());
	}
	
	@Override
	public boolean equals(BlockData data) {
		return isSimilar(data.getMaterial().name());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MagicMaterial) {
			MagicMaterial m = (MagicMaterial)o;
			return isSimilar(m.getMaterial().name());
		}
		return false;
	}
	
	private boolean isSimilar(String name) {
		return name.toLowerCase().matches(pattern);
	}

}

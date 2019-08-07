package com.nisovin.magicspells.materials;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;

import com.nisovin.magicspells.util.Util;

public abstract class MagicMaterial {
	public abstract Material getMaterial();
	
	public abstract BlockData getBlockData();
	
	public final void setBlock(Block block) {
		setBlock(block, true);
	}
	
	public void setBlock(Block block, boolean applyPhysics) {}
	
	public FallingBlock spawnFallingBlock(Location location) { return null; }
	
	public final ItemStack toItemStack() {
		return toItemStack(1);
	}
	
	public abstract ItemStack toItemStack(int quantity);
	
	public final boolean equals(Block block) {
		return equals(block.getState().getBlockData());
	}
	
	public boolean equals(BlockData data) {
		BlockData d = getBlockData();
		if (d != null) {
			return d.equals(data);
		} else {
			return false;
		}
	}
	
	public abstract boolean equals(ItemStack itemStack);
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MagicMaterial) {
			MagicMaterial m = (MagicMaterial)o;
			return m.getMaterial() == getMaterial() && ((getBlockData() != null)
					? getBlockData().equals(m.getBlockData())
					: m.getBlockData() == null);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getMaterial().hashCode();
	}
	
	public static MagicMaterial fromItemStack(ItemStack item) {
		if (item.getType().isBlock()) {
			return new MagicBlockMaterial(item.getType().createBlockData());
		}
		return new MagicItemMaterial(item.getType(), (short)Util.getItemDamage(item));
	}
	
	public static MagicMaterial fromBlock(Block block) {
		return new MagicBlockMaterial(block.getBlockData());
	}
}
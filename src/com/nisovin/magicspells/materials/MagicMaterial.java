package com.nisovin.magicspells.materials;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public abstract class MagicMaterial {
	Material type;
	
	public Material getMaterial() {
		return type;
	}
	
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
		return equals(block.getState().getData());
	}
	
	/*public boolean equals(MaterialData matData) {
		MaterialData d = getMaterialData();
		if (d != null) {
			return d.equals(matData);
		} else {
			return false;
		}
	}*/
	
	/*public boolean equals(ItemStack itemStack) {
		MaterialData d = getMaterialData();
		if (d != null) {
			ItemStack i = d.toItemStack();
			return i.getType() == itemStack.getType() && i.getDurability() == itemStack.getDurability();
		} else {
			return false;
		}
	}*/
	
	public boolean equals(ItemStack itemStack) {
		boolean result = itemStack.getType() == type;
		if (itemStack.getItemMeta() instanceof Damageable) {
			result = result 
					&& (((Damageable)itemStack.getItemMeta()).getDamage() 
							== ((MagicItemMaterial)this).getDurability());
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MagicMaterial) {
			MagicMaterial m = (MagicMaterial)o;
			return m.getMaterial() == getMaterial();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getMaterial().hashCode();
	}
	
	public static MagicMaterial fromItemStack(ItemStack item) {
		if (item.getType().isBlock()) {
			return new MagicBlockMaterial(item.getType());
		}
		return new MagicItemMaterial(item.getType(), 
				(item.getItemMeta() instanceof Damageable)
					? (short)((Damageable)item.getItemMeta()).getDamage()
					: null);
	}
	
	public static MagicMaterial fromBlock(Block block) {
		return new MagicBlockMaterial(block.getType(), block.getBlockData());
	}
}